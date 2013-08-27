
set -u

cd

CLASSPATH=NetBeansProjects/vellum/build/classes
for jar in NetBeansProjects/vellum/dist/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$jar
done

export CLASSPATH=$CLASSPATH

#echo CLASSPATH=$CLASSPATH

tmp=tmp/`basename $0 .sh`
mkdir -p $tmp

seckeystore=$tmp/seckeystore.jceks
privatekeystore=$tmp/server.jks
truststore=$tmp/truststore.jks
cert=$tmp/dual.pem
pass=test1234
secalias=dek2013

aes() {
  keyAlg=AES
  keySize=256
  cipherTrans=AES/CBC/PKCS5Padding
}

des() {
  keyAlg=DES
  keySize=56
  cipherTrans=DES/CBC/PKCS5Padding
}

des3() {
  keyAlg=DESede
  keySize=168
  cipherTrans=DESede/CBC/PKCS5Padding
}

killservers() {
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
}

javaks() {
  keystore=$tmp/$1.jks
  shift
  java \
    -Ddualcontrol.ssl.keyStore=$keystore \
    -Ddualcontrol.ssl.keyStorePassword=$pass \
    -Ddualcontrol.ssl.keyPassword=$pass \
    -Ddualcontrol.ssl.trustStore=$truststore \
    -Ddualcontrol.ssl.trustStorePassword=$pass \
    -Ddualcontrol.minPasswordLength=4 \
    $@
  exitCode=$?
  if [ $exitCode -ne 0 ]
  then
    echo WARN javaks $keystore exitCode $exitCode $@
    exit 1
  fi
}

javaksc() {
  keystore=$tmp/$1.jks
  shift
  java \
    -Ddualcontrol.ssl.keyStore=$keystore \
    -Ddualcontrol.ssl.trustStore=$truststore \
    -Ddualcontrol.minPasswordLength=4 \
    $@
  exitCode=$?
  if [ $exitCode -ne 0 ]
  then
    echo WARN javaks $keystore exitCode $exitCode $@
    exit 1
  fi
}

jc() {
  sleep 1 
  javaks $1 dualcontrol.DummyDualControlConsole $@
}

jc2() {
    jc evanx eeee
    jc henty hhhh
}

jc2t() {
    jc evanx eeee
    jc travs tttt
}

jc3() {
    jc evanx eeee
    jc henty hhhh
    jc brent bbbb
}

jc3t() {
    jc evanx eeee
    jc henty hhhh
    jc travs tttt
}

command1_keytool() {
  keystore=$tmp/$1.jks
  alias=$1
  rm -f $keystore
  keytool -keystore $keystore -storepass "$pass" -keypass "$pass" -alias $alias \
     -genkeypair -dname "CN=$alias, OU=test, O=test, L=ct, S=wp, C=za"
  keytool -keystore $keystore -storepass "$pass" -list | grep Entry
  keytool -keystore $keystore -storepass "$pass" -alias $alias \
     -exportcert -rfc | openssl x509 -text | grep "Subject:"
  keytool -keystore $keystore -storepass "$pass" -alias $alias \
     -exportcert -rfc > $cert
  keytool -keystore $truststore -storepass "$pass" -alias $alias \
     -importcert -noprompt -file $cert
  keytool -keystore $truststore -storepass "$pass" -alias $alias \
     -exportcert -rfc | openssl x509 -text | grep 'CN='
}

command0_initks() {
  killservers
  serveralias="dualcontrol"
  dname="CN=dualcontrol, OU=test, O=test, L=ct, S=wp, C=za"
  rm -f $seckeystore
  rm -f $privatekeystore
  rm -f $truststore
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
  command1_keytool evanx
  command1_keytool henty
  command1_keytool brent
  command1_keytool travs
}

command1_genseckey() {
  javaks server -Ddualcontrol.submissions=3 \
     -Dkeystore=$seckeystore -Dstoretype=JCEKS -Dstorepass=$pass \
     -Dalias=$1 -Dkeyalg=$keyAlg -Dkeysize=$keySize \
     dualcontrol.DualControlGenSecKey
  keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry
  if [ `keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry | wc -l` -eq 3 ]
  then
    echo "INFO DualControlGenSecKey $1 $keyAlg"
  else
    echo "WARN DualControlGenSecKey "
  fi
}

command2_enroll() {
  echo "enroll username $1, alias $2"
  keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry
  javaks server -Ddualcontrol.submissions=3 -Ddualcontrol.username=$1 -Dalias=$2 \
     -Dkeystore=$seckeystore -Dstoretype=JCEKS -Dstorepass=$pass \
     dualcontrol.DualControlEnroll
  keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry
}

command2_revoke() {
  echo "revoke username $1, alias $2"
  keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry
  javaks server -Ddualcontrol.username=$1 -Dalias=$2 \
     -Dkeystore=$seckeystore -Dstoretype=JCEKS -Dstorepass=$pass \
     dualcontrol.DualControlRevoke
  keytool -keystore $seckeystore -storetype JCEKS -storepass $pass -list | grep Entry
}

command0_app() {
  javaks server -Ddualcontrol.submissions=2 dualcontrol.DualControlDemoApp $seckeystore $pass $secalias
}

command0_keystoreserver() {
  javaks server dualcontrol.FileServer 127.0.0.1 4445 1 1 127.0.0.1 $seckeystore
}

keystoreclient() {
  sleep 1
  javaks server dualcontrol.FileClientDemo 127.0.0.1 4445
}

command2_bruteforcetimer() {
  java dualcontrol.JCEKSBruteForceTimer $1 $2 $seckeystore $pass dek2013-evanx-henty eeeehhhh
}

command0_testkeystoreserver() {
  keystoreclient & command0_keystoreserver
  sleep 2
}

command2_teststore() {
  rm -f $seckeystore.enc
  javaksc server dualcontrol.EncryptedKeyStoreTest $seckeystore.enc dek2013-evanx-henty eeeehhhh $@
}

command0_teststore() {
  command2_teststore 999999 4
}

command1_cryptoserver() {
  javaks server dualcontrol.CryptoServer 127.0.0.1 4446 4 $1 127.0.0.1 $seckeystore $pass
}

command1_cryptoserver_remote() {
  echo "cryptoserver_remote $1"
  javaks server dualcontrol.CryptoServer 127.0.0.1 4446 4 $1 127.0.0.1 "127.0.0.1:4445:seckeystore" $pass
}

command0_cryptoclient_cipher() {
  datum=1111222233334444
  request="ENCRYPT:$secalias:$cipherTrans:8:$datum"
  echo "TRACE CryptoClientDemo request $request"
  data=`javaks server dualcontrol.CryptoClientDemo 127.0.0.1 4446 "$request"`
  echo "TRACE CryptoClientDemo response $data"
  request="DECRYPT:$secalias:$cipherTrans:$data"
  echo "TRACE CryptoClientDemo request $request"
  javaks server dualcontrol.CryptoClientDemo 127.0.0.1 4446 "$request"
}

command1_cryptoclient() {
  jc evanx eeee
  jc henty hhhh
  for iter in `seq $1`
  do
    echo "cryptoclient $iter"
    command0_cryptoclient_cipher
  done
}

command1_testcryptoserver() {
  count=$1  
  echo "command1_testcryptoserver $# $@"
  command1_cryptoclient $count & command1_cryptoserver `echo 2*$count | bc`
  sleep 2
}

command1_testcryptoserver_remote() {
  command0_keystoreserver &
  count=$1  
  echo "command1_testcryptoserver $# $@"
  command1_cryptoclient $count & command1_cryptoserver_remote `echo 2*$count | bc`
  sleep 2
}

command0_testgenseckey() {
  command0_initks 
  jc3 & command1_genseckey $secalias
  sleep 2
  if ! nc -z localhost 4444
  then
    jc2 & command0_app
    sleep 2
  fi
}

command0_testenroll() {
  jc3t & command2_enroll travs $secalias
  sleep 2
  if ! nc -z localhost 4444
  then
    jc2t & command0_app
    sleep 2
  fi
}

command1_testconsole() {
  javaksc $1 dualcontrol.DualControlConsole
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

greplog() {
  grep -i '^INFO\|^WARN\|^OK\|error\|Exception' 
}

command1_checklong() {
  command1_testlong $1 2>&1 | greplog | sort | uniq -c 
}

command0_checksingle() {
  command0_testsingle 2>&1 | greplog
}

des3

#set -x

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