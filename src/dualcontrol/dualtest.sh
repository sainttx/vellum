
set -u

cd

if pgrep -f dualcontrol.FileServer
then
  echo WARN killing FileServer
  kill `pgrep -f dualcontrol.FileServer`
fi

if pgrep -f dualcontrol.CryptoServer
then
  echo WARN killing CryptoServer
  kill `pgrep -f dualcontrol.CryptoServer`
fi

CLASSPATH=NetBeansProjects/vellum/build/classes
for jar in NetBeansProjects/vellum/dist/lib/*.jar
do
  echo $jar
  CLASSPATH=$CLASSPATH:$jar
done

echo CLASSPATH=$CLASSPATH

set -x

tmp=~/tmp/`basename $0`
mkdir -p $tmp

seckeystore=$tmp/seckeystore.jceks
privatekeystore=$tmp/server.jks
truststore=$tmp/truststore.jks
cert=$tmp/dual.pem
pass=test1234
secalias=powerstate-2013

javaks() {
  keystore=$tmp/test.$1.jks
  shift
  java \
    -Ddualcontrol.ssl.keyStore=$keystore \
    -Ddualcontrol.ssl.keyStorePassword=$pass \
    -Ddualcontrol.ssl.keyPassword=$pass \
    -Ddualcontrol.ssl.trustStore=$truststore \
    -Ddualcontrol.ssl.trustStorePassword=$pass \
    $@
  exitCode=$?
  if [ $exitCode -ne 0 ]
  then
    echo WARN javaks $keystore exitCode $exitCode $@
  fi
}

jc() {
  javaks $tmp/$1.jks dualcontrol.DualControlClientDummy "$2"
}

jc2() {
    sleep 1 
    jc evanx eeee
    jc henty hhhh
}

jc3() {
    sleep 1 
    jc evanx eeee
    jc henty hhhh
    jc brand bbbb
}

keytool1() {
  keystore=$tmp/$1.jks
  alias=$1
  rm -f $keystore
  keytool -keystore $keystore -storepass "$pass" -keypass "$pass" -alias $alias \
     -genkeypair -dname "CN=$alias, OU=test, O=test, L=ct, S=wp, C=za"
  keytool -keystore $keystore -storepass "$pass" -alias $alias \
     -exportcert -rfc | openssl x509 -text | grep "Subject:"
  keytool -keystore $privatekeystore -storepass "$pass" -alias $alias \
     -exportcert -rfc > $cert
  keytool -keystore $truststore -storepass "$pass" -alias $alias \
     -importcert -noprompt -file $cert
}

initks() {
  serveralias="dualcontrol"
  dname="CN=dualcontrol, OU=test, O=test, L=ct, S=wp, C=za"
  rm -f $seckeystore
  rm -f $privatekeystore
  keytool -keystore $privatekeystore -storepass "$pass" -keypass "$pass" \
     -alias "$serveralias" -genkeypair -dname "$dname"
  keytool -keystore $privatekeystore -storepass "$pass" -list | grep Entry
  keytool -keystore $privatekeystore -storepass "$pass" -alias $serveralias \
     -exportcert -rfc | openssl x509 -text | grep "Subject:"
  keytool -keystore $privatekeystore -storepass "$pass" -alias $serveralias \
     -exportcert -rfc > $cert
  keytool -keystore $truststore -storepass "$pass" -alias $serveralias \
     -importcert -noprompt -file $cert
  keytool -keystore $truststore -storepass "$pass" -list | grep Entry
  keytool1 evanx
  keytool1 henty
  keytool1 brand
}

command1_genseckey() {
  javaks server -Ddualcontrol.submissions=3 \
     -Dkeystore=$seckeystore -Dstoretype=JCEKS -Dstorepass=$pass \
     -Dalias=$1 -Dkeyalg=AES -Dkeysize=256 \
     dualcontrol.DualControlGenSecKey
  keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry
}

command0_app() {
  javaks server -Ddualcontrol.submissions=2 dualcontrol.AppDemo $seckeystore $pass $secalias
}

command0_keystoreserver() {
  javaks server dualcontrol.FileServer 127.0.0.1 4445 1 1 127.0.0.1 $seckeystore
}

keystoreclient() {
  sleep 1
  javaks server dualcontrol.FileClientDemo 127.0.0.1 4445
}

command0_testkeystoreserver() {
  keystoreclient & command0_keystoreserver
  sleep 2
}

command0_cryptoserver() {
  javaks server dualcontrol.CryptoServer 127.0.0.1 4446 4 2 127.0.0.1 $seckeystore $pass
}

command1_cryptoserver_remote() {
  echo "cryptoserver_remote $1"
  javaks server dualcontrol.CryptoServer 127.0.0.1 4446 4 $1 127.0.0.1 "127.0.0.1:4445:seckeystore:" $pass
}

cryptoclient_cipher() {
  data=`javaks server dualcontrol.CryptoClientDemo 127.0.0.1 4446 \
     "$secalias:DESede/CBC/PKCS5Padding:ENCRYPT:8:1111222233334444"`
  exitCode=$?  
  echo "CryptoClientDemo ENCRYPT exitCode $exitCode"
  if [ $exitCode -ne 0 ]
  then 
    echo "ERROR CryptoClientDemo ENCRYPT exitCode $exitCode"
  else 
    javaks server dualcontrol.CryptoClientDemo 127.0.0.1 4446 \
       "$secalias:DESede/CBC/PKCS5Padding:DECRYPT:$data"
    exitCode=$? 
    if [ $exitCode -ne 0 ]
    then
      echo "ERROR CryptoClientDemo DECRYPT exitCode $exitCode"
     else 
      echo "INFO CryptoClientDemo OK"   
    fi
  fi
}

cryptoclient1() {
  sleep 1
  jc evanx eeee
  jc henty hhhh
  sleep 1
  for iter in `seq $1`
  do
    #sleep 1
    echo "cryptoclient $iter"
    cryptoclient_cipher
  done
}

command1_testcryptoserver() {
  command0_keystoreserver &
  count=$1  
  echo "command1_testcryptoserver $# $@"
  cryptoclient1 $count & command1_cryptoserver_remote `echo 2*$count | bc`
  sleep 2
}

command0_testgenseckey() {
  initks 
  jc3 & command1_genseckey $secalias
  sleep 2
  if ! nc -z localhost 4444
  then
    jc2 & command0_app
    sleep 2
  fi
}

command0_testclient() {
  javaks evanx dualcontrol.DualControlClient
}

command1_testlong() {
  command0_testgenseckey
  command0_testkeystoreserver
  command1_testcryptoserver 1
  command1_testcryptoserver $1
  command1_testcryptoserver 1
}

command0_testlong() {
  command1_testlong 100
}

command0_testcryptoserver() {
  command1_testcryptoserver 1
  command1_testcryptoserver 2
  command1_testcryptoserver 1
}

command0_testshort() {
  command0_testgenseckey
  command0_testkeystoreserver
  command0_testcryptoserver
}

command0_testsingle() {
  command0_testgenseckey
  command0_testkeystoreserver
  command1_testcryptoserver 1
}

#command0_testsingle
#command0_testshort
#command0_testlong
#command0_testcryptoserver
#command0_testclient

if [ $# -gt 0 ]
then
  command=$1
  shift
  command$#_$command $@
else
  command0_testshort
fi

#sh NetBeansProjects/svn/vellum/trunk/src/dualcontrol/dualtest.sh 2>&1 | grep -i '^WARN\|ERROR\|^INFO' | uniq -c