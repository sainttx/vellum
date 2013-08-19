
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

secstore=~/tmp/dual.sec.jceks
secalias=powerstate-2013
privatestore=~/tmp/dual.private.jks
truststore=~/tmp/dual.public.jks
cert=~/tmp/dual.pem
pass=test1234

javaks() {
  java \
    -Ddualcontrol.ssl.keyStore=$privatestore \
    -Ddualcontrol.ssl.keyStorePassword=$pass \
    -Ddualcontrol.ssl.keyPassword=$pass \
    -Ddualcontrol.ssl.trustStore=$truststore \
    -Ddualcontrol.ssl.trustStorePassword=$pass \
    $@
  exitCode=$?
  if [ $exitCode -ne 0 ]
  then
    echo WARN javaks exitCode $exitCode $@
  fi
}

jc() {
  javaks dualcontrol.DualControlClientDummy "$1"
}

jc2() {
    sleep 1 
    jc "evanx:eeee" 
    jc "henty:hhhh"
}

jc3() {
    sleep 1 
    jc "evanx:eeee" 
    jc "henty:hhhh"
    jc "brand:bbbb"
}

initks() {
  serveralias="dualcontrol"
  dname="CN=dualcontrol, OU=test, O=test, L=ct, S=wp, C=za"
  rm -f $privatestore
  rm -f $truststore
  rm -f $secstore
  keytool -keystore $privatestore -storepass "$pass" -keypass "$pass" \
     -alias "$serveralias" -genkeypair -dname "$dname"
  keytool -keystore $privatestore -storepass "$pass" -list | grep Entry
  keytool -keystore $privatestore -storepass "$pass" -alias $serveralias \
     -exportcert -rfc | openssl x509 -text | grep "Subject:"
  keytool -keystore $privatestore -storepass "$pass" -alias $serveralias \
     -exportcert -rfc > $cert
  keytool -keystore $truststore -storepass "$pass" -alias $serveralias \
     -importcert -noprompt -file $cert
  keytool -keystore $truststore -storepass "$pass" -list | grep Entry
}

command1_genseckey() {
  javaks -Ddualcontrol.alias=$1 -Ddualcontrol.submissions=3 dualcontrol.DualControlKeyTool \
     -keystore $secstore -storetype JCEKS -storepass $pass -genseckey -keyalg DESede -keysize 168
  keytool -keystore $secstore -storetype JCEKS -storepass $pass -list | grep Entry
}

command0_app() {
  javaks -Ddualcontrol.submissions=2 dualcontrol.AppDemo $secstore $pass $secalias
}

command0_keystoreserver() {
  javaks dualcontrol.FileServer 127.0.0.1 4445 1 1 127.0.0.1 $secstore
}

keystoreclient() {
  sleep 1
  javaks dualcontrol.FileClientDemo 127.0.0.1 4445
}

command0_testkeystoreserver() {
  keystoreclient & command0_keystoreserver
  sleep 2
}

command0_cryptoserver() {
  javaks dualcontrol.CryptoServer 127.0.0.1 4446 4 2 127.0.0.1 $secstore $pass
}

command1_cryptoserver_remote() {
  echo "cryptoserver_remote $1"
  javaks dualcontrol.CryptoServer 127.0.0.1 4446 4 $1 127.0.0.1 "127.0.0.1:4445:secstore:" $pass
}

cryptoclient_cipher() {
  data=`javaks dualcontrol.CryptoClientDemo 127.0.0.1 4446 \
     "$secalias:DESede/CBC/PKCS5Padding:ENCRYPT:8:1111222233334444"`
  exitCode=$?  
  echo "CryptoClientDemo ENCRYPT exitCode $exitCode"
  if [ $exitCode -ne 0 ]
  then 
    echo "ERROR CryptoClientDemo ENCRYPT exitCode $exitCode"
  else 
    javaks dualcontrol.CryptoClientDemo 127.0.0.1 4446 \
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
  jc "evanx:eeee" 
  jc "henty:hhhh"
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
  javaks dualcontrol.DualControlClient
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