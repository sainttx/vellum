
set -u 

cd
cd tmp

pass=test1234

export CLASSPATH=/home/evans/NetBeansProjects/vellum/build/test/classes:/home/evans/NetBeansProjects/vellum/build/classes:/home/evans/NetBeansProjects/vellum/dist/lib/log4j-1.2.15.jar:/home/evans/NetBeansProjects/vellum/dist/lib/slf4j-log4j12-1.5.10.jar:/home/evans/tmp/netbeans-7.3.1/platform/modules/ext/junit-4.10.jar

keytool=~/jdk7/jre/bin/keytool

command1_initks() {
  alias=$1
  rm -f $alias.jks
  rm -f $alias.trust.jks
  $keytool -keystore $alias.jks -storepass $pass -keypass $pass -alias $alias \
    -genkeypair -keyalg rsa -keysize 2048 -validity 999 -dname "CN=$alias"
  $keytool -keystore $alias.jks -storepass $pass -alias $alias \
    -exportcert -rfc -file $alias.pem
}

command1_initca() {
  alias=$1
  rm -f $alias.jks
  rm -f $alias.trust.jks
  $keytool -keystore $alias.jks -storepass $pass -keypass $pass -alias $alias \
    -genkeypair -keyalg rsa -keysize 2048 -validity 999 -dname "CN=$alias" \
    -ext
  $keytool -keystore $alias.jks -storepass $pass -alias $alias \
    -exportcert -rfc -file $alias.pem
}

command0_initks() {
  command1_initks ca
  command1_initks server
  command1_initks client
  $keytool -keystore server.trust.jks -storepass $pass -importcert -noprompt \
    -alias client -file client.pem
  $keytool -keystore client.trust.jks -storepass $pass -importcert -noprompt \
    -alias server -file server.pem
}

command0_connect() {
  java localca.LocalCaMain server.jks server.jks server.jks server.jks test1234
  java localca.LocalCaMain server.jks server.trust.jks client.jks client.trust.jks test1234
  java localca.LocalCaMain ca.jks server.trust.jks client.jks ca.jks test1234
}

command0_test() {
  command0_initks
  command0_connect
}


if [ $# -gt 0 ]
then
  command=$1
  shift
  command$#_$command $@
else 
  cat $0 | grep ^command
fi



