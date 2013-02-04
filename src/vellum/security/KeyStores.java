/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.ssl.internal.pkcs12.PKCS12KeyStore;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.pkcs.PKCS10;
import sun.security.pkcs.PKCS10Attribute;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.provider.X509Factory;
import sun.security.x509.*;
import vellum.exception.Exceptions;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * snippets from OpenJDK7 KeyTool etc.
 *
 * @author evan
 */
public class KeyStores {

    static Logr logger = LogrFactory.getLogger(KeyStores.class);
    public static final String BEGIN_PRIVATE_KEY = formatPem("BEGIN PRIVATE KEY");
    public static final String END_PRIVATE_KEY = formatPem("END PRIVATE KEY");
    public static final String BEGIN_CERT = formatPem("BEGIN CERTIFICATE");
    public static final String END_CERT = formatPem("END CERTIFICATE");
    public static final String BEGIN_CERT_REQ = formatPem("BEGIN CERTIFICATE REQUEST");
    public static final String END_CERT_REQ = formatPem("END CERTIFICATE REQUEST");
    public static final String BEGIN_NEW_CERT_REQ = formatPem("BEGIN NEW CERTIFICATE REQUEST");
    public static final String END_NEW_CERT_REQ = formatPem("END NEW CERTIFICATE REQUEST");
    public static final String LOCAL_DNAME = "CN=localhost, OU=local, O=local, L=local, S=local, C=local";
    private static final String dashes = "-----";
    
    private static String formatPem(String label) {
        return dashes + label + dashes;
    }

    public static X509TrustManager loadTrustManager(TrustManagerFactory trustManagerFactory) throws Exception {
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new RuntimeException();
    }

    public static SSLSocketFactory createSSLSocketFactory(KeyManagerFactory keyManagerFactory, 
            TrustManagerFactory trustManagerFactory) throws Exception {
        return createSSLContext(keyManagerFactory, trustManagerFactory).getSocketFactory();
    }
    
    public static SSLContext createSSLContext(KeyManagerFactory keyManagerFactory, 
            TrustManagerFactory trustManagerFactory) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
    
    public static TrustManagerFactory loadTrustManagerFactory(KeyStore trustStore) {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
            return tmf;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static KeyManagerFactory loadKeyManagerFactory(KeyStore keyStore, char[] keyPassword) {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keyPassword);
            return kmf;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static KeyStore loadKeyStore(String type, String filePath, char[] keyStorePassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance(type);
            FileInputStream inputStream = new FileInputStream(filePath);
            keyStore.load(inputStream, keyStorePassword);
            return keyStore;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    
    public static HttpsConfigurator createHttpsConfigurator(SSLContext sslContext, final boolean needClientAuth) throws Exception {
        return new HttpsConfigurator(sslContext) {

            @Override
            public void configure(HttpsParameters httpsParameters) {
                SSLContext sslContext = getSSLContext();
                InetSocketAddress remote = httpsParameters.getClientAddress();
                if (remote.getHostName().equals("localhost")) {
                }
                SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                defaultSSLParameters.setNeedClientAuth(needClientAuth);
                httpsParameters.setSSLParameters(defaultSSLParameters);
            }
        };
    }

    public static String buildPem(PKCS12KeyStore p12, String keyAlias, char[] password) throws Exception {
        PrivateKey key = (PrivateKey) p12.engineGetKey(keyAlias, password);
        StringBuilder builder = new StringBuilder();
        builder.append(buildKeyPem(key));
        Certificate[] chain = p12.engineGetCertificateChain(keyAlias);
        for (Certificate cert : chain) {
            builder.append(buildCertPem(cert));
        }
        return builder.toString();
    }
    
    public static String buildKeyPem(Key privateKey) throws Exception, CertificateException {
        StringBuilder builder = new StringBuilder();
        BASE64Encoder encoder = new BASE64Encoder();
        builder.append(BEGIN_PRIVATE_KEY);
        builder.append('\n');
        builder.append(encoder.encodeBuffer(privateKey.getEncoded()));
        builder.append(END_PRIVATE_KEY);
        builder.append('\n');
        return builder.toString();
    }

    public static String buildCertPem(Certificate cert) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(X509Factory.BEGIN_CERT);
            builder.append('\n');
            BASE64Encoder encoder = new BASE64Encoder();
            builder.append(encoder.encodeBuffer(cert.getEncoded()));
            builder.append(X509Factory.END_CERT);
            builder.append('\n');
            return builder.toString();
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static String buildCertReqPem(PKCS10 certReq) throws Exception, CertificateException {
        StringBuilder builder = new StringBuilder();
        builder.append(BEGIN_CERT_REQ);
        builder.append('\n');
        BASE64Encoder encoder = new BASE64Encoder();
        builder.append(encoder.encodeBuffer(certReq.getEncoded()));
        builder.append(END_CERT_REQ);
        builder.append('\n');
        return builder.toString();
    }

    public static String formatDname(String cn, String ou, String o, String l, String s, String c) {
        try {
            X500Name name = new X500Name(
                    coalesce(cn, "unknown"), 
                    coalesce(ou, "unknown"), 
                    coalesce(o, "unknown"), 
                    coalesce(l, "unknown"), 
                    coalesce(s, "unknown"), 
                    coalesce(c, "unknown")
            );
            String dname = name.toString();
            logger.info(dname);
            return dname;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    private static String coalesce(String value, String defaultValue) {
        if (value == null) return defaultValue;
        return value;
    }
    
    public static X509Certificate findRootCert(KeyStore keyStore, String alias) throws Exception {
        return findRootCert(keyStore.getCertificateChain(alias));
    }

    public static X509Certificate findRootCert(Certificate[] chain) throws Exception {
        for (Certificate cert : chain) {
            if (cert instanceof X509Certificate) {
                X509Certificate x509Cert = (X509Certificate) cert;
                if (x509Cert.getSubjectDN().equals(x509Cert.getIssuerDN())) {
                    return x509Cert;
                }
            }
        }
        return null;
    }

    public static byte[] decodePemDer(String pem) throws Exception {
        int index = pem.lastIndexOf(dashes);
        if (index > 0) {
            pem = pem.substring(0, index);
            index = pem.lastIndexOf(dashes);
            pem = pem.substring(0, index);
            index = pem.lastIndexOf(dashes);
            pem = pem.substring(index + dashes.length());
        }
        return new BASE64Decoder().decodeBuffer(pem);
    }

    public static PKCS10 createCertReq(String csr) throws Exception {
        return new PKCS10(decodePemDer(csr));
    }

    public static PKCS10 createCertReq(PrivateKey privateKey, X509Certificate cert) throws Exception {
        String sigAlgName = "SHA256WithRSA";
        PKCS10 request = new PKCS10(cert.getPublicKey());
        if (false) {
            CertificateExtensions ext = new CertificateExtensions();
            request.getAttributes().setAttribute(X509CertInfo.EXTENSIONS,
                    new PKCS10Attribute(PKCS9Attribute.EXTENSION_REQUEST_OID, ext));
        }
        Signature signature = Signature.getInstance(sigAlgName);
        signature.initSign(privateKey);
        X500Name subject = new X500Name(cert.getSubjectDN().toString());
        X500Signer x500Signer = new X500Signer(signature, subject);
        request.encodeAndSign(x500Signer);
        return request;
    }

    public static X509Certificate signCert(PrivateKey privateKey, X509Certificate signerCert,
            PKCS10 certReq, Date startDate, int validityDays) throws Exception {
        String sigAlgName = "SHA256WithRSA";
        Date endDate = new Date(startDate.getTime() + TimeUnit.DAYS.toMillis(validityDays));
        CertificateValidity interval = new CertificateValidity(startDate, endDate);
        byte[] encoded = signerCert.getEncoded();
        X509CertImpl signerCertImpl = new X509CertImpl(encoded);
        X509CertInfo signerCertInfo = (X509CertInfo) signerCertImpl.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
        X500Name issuer = (X500Name) signerCertInfo.get(X509CertInfo.SUBJECT + "." + CertificateSubjectName.DN_NAME);
        Signature signature = Signature.getInstance(sigAlgName);
        signature.initSign(privateKey);
        X509CertInfo info = new X509CertInfo();
        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(new java.util.Random().nextInt() & 0x7fffffff));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get(sigAlgName)));
        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(issuer));
        info.set(X509CertInfo.KEY, new CertificateX509Key(certReq.getSubjectPublicKeyInfo()));
        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(certReq.getSubjectName()));
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privateKey, sigAlgName);
        return cert;
    }

    public static String getSubjectDname(String pem) throws Exception {
        return parseCert(pem).getSubjectDN().toString();
    }
    
    public static String getIssuerDname(String pem) throws Exception {
        return parseCert(pem).getIssuerDN().toString();
    }

    public static X509Certificate parseCert(String pem) throws Exception {
        return new X509CertImpl(decodePemDer(pem));
    }
        
}
