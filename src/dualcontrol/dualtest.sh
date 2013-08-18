
set -u

cd

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
}

jc() {
  javaks dualcontrol.DualControlClient "$1"
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
  javaks -Ddualcontrol.alias=$1 -Ddualcontrol.inputs=3 dualcontrol.DualControlKeyTool \
     -keystore $secstore -storetype JCEKS -storepass $pass -genseckey -keyalg DESede
  keytool -keystore $secstore -storetype JCEKS -storepass $pass -list | grep Entry
}

command0_app() {
  javaks -Ddualcontrol.inputs=2 dualcontrol.AppDemo $secstore $pass $secalias
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
  javaks dualcontrol.CryptoServer 127.0.0.1 4446 1 2 127.0.0.1 $secstore $pass
}

command0_cryptoserverremote() {
  javaks dualcontrol.CryptoServer 127.0.0.1 4446 1 2 127.0.0.1 "127.0.0.1:4445:secstore:" $pass
}

cryptoclient() {
  sleep 1
  jc "evanx:eeee" 
  jc "henty:hhhh"
  sleep 1
  data=`javaks dualcontrol.CryptoClientDemo 127.0.0.1 4446 \
     "$secalias:DESede/CBC/PKCS5Padding:ENCRYPT:8:111122223333444"`
  javaks dualcontrol.CryptoClientDemo 127.0.0.1 4446 \
     "$secalias:DESede/CBC/PKCS5Padding:DECRYPT:$data"
}

command0_testcryptoserver() {
  command0_keystoreserver &
  cryptoclient & command0_cryptoserverremote
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

command0_client() {
  javaks dualcontrol.DualControlClient
}

command0_testgenseckey
command0_testkeystoreserver
command0_testcryptoserver
#command0_client

#sh /home/evans/NetBeansProjects/svn/vellum/trunk/src/dualcontrol/dualtest.sh > /home/evans/NetBeansProjects/svn/vellum/trunk/src/dualcontrol/dualtest.out 2>&1
