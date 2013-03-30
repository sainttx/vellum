
java -Djavax.net.ssl.keyStore=security/croc.jks -Djavax.net.ssl.keyStorePassword=crocserver -Djavax.net.ssl.keyPassword=crocserver -Djavax.net.ssl.trustStore=security/croc.jks -Djavax.net.ssl.trustStorePassword=crocserver

keytool -keystore security/croc.jks -storepass crocserver -alias crocserver -genkey -keypass crocserver -dname "CN=crocserver, OU=crocserver-ou, O=crocserver-org, L=CPT, S=WP, C=ZA"

keytool -keystore security/croc.jks -storepass crocserver -list

curl -k "https://localhost:8443/enrollUser/evan.summers@gmail.com?displayName=Evan%20Summers"

curl -k "https://localhost:8443/enrollOrg/evan.summers@gmail.com/crocserver.org?displayName=CrocServer.Org"

curl -k "https://localhost:8443/enrollCert/evan.summers@gmail.com/crocserver.org/evanx@desktop"

