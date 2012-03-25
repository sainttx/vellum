/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import sun.security.tools.KeyTool;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.Base64;
import vellum.util.Lists;
import vellum.util.Streams;

/**
 *
 * @author evan
 */
public class VTest implements Runnable {
    Logr logger = LogrFactory.getLogger(getClass());    

    VTestProperties properties = new VTestProperties(); 
    SecureRandom sr = new SecureRandom();
    
    VProviderProperties providerProperties = new VProviderProperties();
    VProviderContext providerContext = VProviderContext.instance;
    VProvider provider = new VProvider();
    
    VCipherProperties cipherProperties = new VCipherProperties();
    VCipherContext cipherContext = new VCipherContext();
    VCipherServer server = new VCipherServer();

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void setProperties() {
        providerProperties.keyStore = properties.providerKeyStoreFile;
        providerProperties.trustStore = properties.providerTrustStoreFile;
        cipherProperties.keyStore = properties.cipherPrivateKeyStoreFile;        
        cipherProperties.trustStore = properties.cipherTrustStoreFile;
        cipherProperties.cipherKeyStore = properties.cipherSecretKeyStoreFile;                
    }
    
    private void deleteKeyStores() {
        new File(providerProperties.keyStore).delete();
        new File(providerProperties.trustStore).delete();
        new File(cipherProperties.keyStore).delete();
        new File(cipherProperties.trustStore).delete();
        new File(cipherProperties.cipherKeyStore).delete();        
    }

    private void testProvider() throws Exception {
        KeyTool.main(buildKeyToolGenKeyPairArgs(providerProperties.keyStore, providerProperties.keyAlias, "provider"));
        KeyTool.main(buildKeyToolExportCertArgs(providerProperties.keyStore, providerProperties.keyAlias, properties.providerCert));
        KeyTool.main(buildKeyToolGenKeyPairArgs(cipherProperties.keyStore, cipherProperties.privateAlias, "cipher"));
        KeyTool.main(buildKeyToolExportCertArgs(cipherProperties.keyStore, cipherProperties.privateAlias, properties.cipherCert));
        KeyTool.main(buildKeyToolImportCertArgs(cipherProperties.trustStore, cipherProperties.trustAlias, properties.providerCert));
        KeyTool.main(buildKeyToolImportCertArgs(providerProperties.trustStore, providerProperties.trustAlias, properties.cipherCert));
        KeyTool.main(buildKeyToolListArgs(cipherProperties.keyStore));
        KeyTool.main(buildKeyToolListArgs(cipherProperties.trustStore));
        KeyTool.main(buildKeyToolListArgs(providerProperties.keyStore));
        KeyTool.main(buildKeyToolListArgs(providerProperties.trustStore));
        Key key = generateKey();
        if (false) {
            importKey(cipherProperties.cipherKeyStore, cipherProperties.secretAlias, key);
        }
        cipherContext.config(cipherProperties, 
                properties.keyStorePass.toCharArray(), properties.privateKeyPass.toCharArray(),
                properties.trustStorePass.toCharArray());
        server.config(cipherContext);
        listProviders();
        if (false) {
            Cipher cipher = Cipher.getInstance("AES", provider);
            logger.info(cipher.getProvider().getClass());
            cipher.init(0, (Key) null);
        }
        loadKey(cipherProperties.keyStore, cipherProperties.privateAlias);
        providerContext.config(providerProperties, 
                properties.keyStorePass.toCharArray(), properties.privateKeyPass.toCharArray(),
                properties.keyStorePass.toCharArray());
        server.start();
        VCipherSpi cipher = new VCipherSpi();
        String datum = "12345678901234567890";
        logger.info(datum);
        byte[] bytes = datum.getBytes();
        byte[] responseBytes = cipher.engineDoFinal(bytes, 0, bytes.length);
        logger.info(responseBytes.length);
        server.close();
    }

    private void testSecretKey() throws Exception {
        Security.addProvider(provider);
        KeyTool.main(buildKeyToolGenSecKeyArgs(cipherProperties.cipherKeyStore, cipherProperties.secretAlias));
        KeyTool.main(buildKeyToolListArgs(cipherProperties.cipherKeyStore));
        Key key = loadKey(cipherProperties.cipherKeyStore, cipherProperties.secretAlias);
        Cipher aesEncryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesEncryptCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters params = aesEncryptCipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();        
        logger.info(Lists.formatHex(iv));
        Cipher aesDecryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv);
        aesDecryptCipher.init(Cipher.DECRYPT_MODE, key, ips);
        String message = "0123456789";
        byte[] encryptedBytes = aesEncryptCipher.doFinal(message.getBytes());
        byte[] decryptedBytes = aesDecryptCipher.doFinal(encryptedBytes);
        logger.info(new String(decryptedBytes));        
    }
    
    public void process() throws Exception {
        setProperties();
        deleteKeyStores();
        testSecretKey();
        if (false) {
            testProvider();
        }
    }
        
    private Key generateKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");        
        generator.init(256, sr);
        SecretKey key = generator.generateKey();
        logger.info(key.getAlgorithm(), key.getFormat(), key.getEncoded().length, Base64.encode(key.getEncoded()));
        return key;
    }

    private String[] buildKeyToolGenSecKeyArgs(String secretKeyStoreFile, String secretKeyAlias) {
        return new String[] {
            "-genseckey", 
            "-keyalg", properties.secretKeyAlg, 
            "-keysize", Integer.toString(properties.secretKeySize), 
            "-keystore", secretKeyStoreFile, 
            "-storetype", properties.keyStoreType, 
            "-storepass", properties.secretKeyStorePass,
            "-alias", secretKeyAlias, 
            "-keypass", properties.secretKeyPass
        };
    }
    
    private String[] buildKeyToolGenKeyPairArgs(String keyStoreFile, String keyAlias, String cn) {
        return new String[] {
            "-genkeypair", 
            "-keyalg", properties.keyAlg, 
            "-keystore", keyStoreFile, 
            "-storetype", properties.keyStoreType, 
            "-storepass", properties.keyStorePass,
            "-alias", keyAlias, 
            "-keypass", properties.privateKeyPass, 
            "-dname", String.format("CN=%s, OU=Development, O=venigmasecured.com, L=Cape Town, S=WP, C=za", cn)
        };
    }

    private String[] buildKeyToolExportCertArgs(String keyStore, String alias, String certFile) {
        return new String[] {
            "-export", 
            "-keystore", keyStore, 
            "-storetype", properties.keyStoreType, 
            "-storepass", properties.keyStorePass,
            "-alias", alias, 
            "-keypass", properties.privateKeyPass, 
            "-file", certFile, 
        };
    }
    
    private String[] buildKeyToolImportCertArgs(String trustStore, String trustAlias, String certFile) {
        return new String[] {
            "-import", 
            "-noprompt",
            "-keystore", trustStore, 
            "-storetype", properties.keyStoreType, 
            "-storepass", properties.trustStorePass,
            "-alias", trustAlias,
            "-file", certFile,
        };
    }

    private String[] buildKeyToolListArgs(String keyStore) throws Exception {
        return new String[] {
            "-list", 
            "-keystore", keyStore, 
            "-storetype", properties.keyStoreType, 
            "-storepass", properties.keyStorePass
        };
    }

    private Key loadKey(String keyStoreFile, String keyAlias) throws Exception {
        File file = new File(keyStoreFile);
        KeyStore keyStore = KeyStore.getInstance("JCEKS", "VProvider");
        keyStore.load(new FileInputStream(file), properties.keyStorePass.toCharArray());
        logger.info("loadKey", keyStore.getType(), keyStore.getProvider().getName());
        Key key = keyStore.getKey(keyAlias, properties.privateKeyPass.toCharArray());
        logger.info(key.getAlgorithm(), key.getFormat());
        return key;
    }

    private void importKey(String cipherKeyStore, String secretAlias, Key key) throws Exception {
        logger.info("importKey", key.getAlgorithm(), key.getFormat());
        File file = new File(cipherKeyStore);
        KeyStore keyStore = KeyStore.getInstance("JCEKS", "VProvider");
        keyStore.load(new FileInputStream(file), properties.keyStorePass.toCharArray());
        logger.info("Cipher KeyStore provider", keyStore.getProvider().getName());
    }

    
    private void listProviders() {
        for (Provider provider : Security.getProviders()) {
            logger.info(provider.getName());
        }
    }
    
    public static void main(String[] args) {
        VTest instance = new VTest() ;
        try {
            if (true) {
                new Thread(instance).start();
                Thread.sleep(4000);
                System.exit(0);
            } else {
                instance.process();
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            Streams.close(instance.server);
        }
    }

    
}
