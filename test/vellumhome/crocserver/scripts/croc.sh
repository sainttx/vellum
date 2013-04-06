
java -Djavax.net.ssl.keyStore=security/croc.jks -Djavax.net.ssl.keyStorePassword=crocserver -Djavax.net.ssl.keyPassword=crocserver -Djavax.net.ssl.trustStore=security/croc.jks -Djavax.net.ssl.trustStorePassword=crocserver

~/jdk7/jre/bin/keytool -keystore security/croc.jks -storepass crocserver -alias crocserver -genkeypair -keyalg RSA -keysize 2048 -keypass crocserver -dname "CN=crocserver, OU=crocserver-ou, O=crocserver-org, L=CPT, S=WP, C=ZA"

keytool -keystore security/croc.jks -storepass crocserver -list

curl -k "https://localhost:8443/enrollUser/evan.summers@gmail.com?displayName=Evan%20Summers"

curl -k "https://localhost:8443/enrollOrg/evan.summers@gmail.com/crocserver.org?displayName=CrocServer.Org"

curl -k "https://localhost:8443/enrollCert/evan.summers@gmail.com/evanx@desktop.crocserver.org" -o enroll-key.pem

curl -k "https://localhost:8443/getCert/evan.summers@gmail.com/evanx@desktop.crocserver.org" -o enroll-cert.pem

cat enroll-cert.pem | openssl x509 -text

curl -k "https://localhost:8444/post/crocserver.org/evanx@desktop.crocserver.org/aide/OUTPUT_CHANGED" --key enroll-key.pem --cert enroll-cert.pem -d 12345



