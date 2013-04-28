
set -x

cd ~/vellum/croc/tmp

command0_genkeypair() {
  ~/jdk7/jre/bin/keytool -keystore security/croc.jks -storepass crocserver -alias crocserver -keypass crocserver \
    -genkeypair -keyalg RSA -keysize 2048 \
    -dname "CN=crocserver, OU=crocserver-ou, O=crocserver-org, L=CPT, S=WP, C=ZA"
  keytool -keystore security/croc.jks -storepass crocserver -list
}

command0_run() {
  java -Djavax.net.ssl.keyStore=security/croc.jks -Djavax.net.ssl.keyStorePassword=crocserver -Djavax.net.ssl.keyPassword=crocserver \
    -Djavax.net.ssl.trustStore=security/croc.jks -Djavax.net.ssl.trustStorePassword=crocserver
}

command0_enroll() { # enroll cert
  curl -k "https://localhost:8443/enrollUser/evan.summers@gmail.com?displayName=Evan%20Summers"
  curl -k "https://localhost:8443/enrollOrg/evan.summers@gmail.com/crocserver.org?displayName=CrocServer.Org"
  curl -k "https://localhost:8443/enrollCert/evan.summers@gmail.com/crocserver.org/evanx@desktop.crocserver.org" -o enroll-key.pem
  curl -k "https://localhost:8443/getCert/evan.summers@gmail.com/evanx@desktop.crocserver.org" -o enroll-cert.pem
  cat enroll-cert.pem | openssl x509 -text
}

command0_post () {
  curl -k "https://localhost:8444/post/evanx@desktop.crocserver.org/aide" --key enroll-key.pem --cert enroll-cert.pem -d 12345
  curl -k "https://localhost:8444/post/evanx@desktop.crocserver.org/aide/NotifyType=OUTPUT_CHANGED" --key enroll-key.pem --cert enroll-cert.pem -d 12346
}

command0_default() {
  command0_enroll
  command0_post
}

if [ $# -gt 0 ]
then
  command=$1
  shift
else
  command=default
fi

command$#_$command

