/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VTestProperties {
    String providerCert = "/evans/test/provider.cert";
    String providerKeyStoreFile = "/evans/test/providerKeyStore.ks";
    String providerTrustStoreFile = "/evans/test/providerTrustStore.ks";

    String cipherCert = "/evans/test/cipher.cert";
    String cipherPrivateKeyStoreFile = "/evans/test/cipherPrivateKeyStore.ks";
    String cipherTrustStoreFile = "/evans/test/cipherTrustStore.ks";
    String cipherSecretKeyStoreFile = "/evans/test/cipherSecretKeyStore.ks";

    String keyStoreType = "JCEKS";
    String keyStorePass = "storepass";
    String trustStorePass = "storepass";
    String secretKeyStorePass = "storepass";
    String privateKeyPass = "keypass";
    String secretKeyPass = "keypass";
    String keyAlg = "DSA";
    String secretKeyAlg = "AES";
    int secretKeySize = 256;
    
    
}
