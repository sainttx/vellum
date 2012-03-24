/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import sun.security.tools.KeyTool;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.Base64;

/**
 *
 * @author evan
 */
public class VTest {
    Logr logger = LogrFactory.getLogger(getClass());    
    
    VProviderContext providerContext = VProviderContext.instance;

    VProviderProperties providerProperties = new VProviderProperties();
            
    VTestProperties properties = new VTestProperties(); 
    
    VCipherProperties cipherProperties = new VCipherProperties();
    VCipherServer server = new VCipherServer();
    VCipherContext cipherContext = new VCipherContext();
    
    public void start() throws Exception {
        providerProperties.keyStore = properties.providerKeyStoreFile;
        providerProperties.trustStore = properties.providerTrustStoreFile;
        cipherProperties.keyStore = properties.cipherKeyStoreFile;        
        cipherProperties.trustStore = properties.cipherTrustStoreFile;
        new File(providerProperties.keyStore).delete();
        new File(providerProperties.trustStore).delete();
        new File(cipherProperties.keyStore).delete();
        new File(cipherProperties.trustStore).delete();
        KeyTool.main(buildKeyToolGenKeyPairArgs(providerProperties.keyStore, providerProperties.keyAlias, "provider"));
        KeyTool.main(buildKeyToolExportCertArgs(providerProperties.keyStore, providerProperties.keyAlias, properties.providerCert));
        KeyTool.main(buildKeyToolGenKeyPairArgs(cipherProperties.keyStore, cipherProperties.keyAlias, "cipher"));
        KeyTool.main(buildKeyToolExportCertArgs(cipherProperties.keyStore, cipherProperties.keyAlias, properties.cipherCert));
        KeyTool.main(buildKeyToolImportCertArgs(cipherProperties.trustStore, cipherProperties.trustAlias, properties.providerCert));
        KeyTool.main(buildKeyToolImportCertArgs(providerProperties.trustStore, providerProperties.trustAlias, properties.cipherCert));
        KeyTool.main(buildKeyToolListArgs(cipherProperties.keyStore));
        KeyTool.main(buildKeyToolListArgs(cipherProperties.trustStore));
        KeyTool.main(buildKeyToolListArgs(providerProperties.keyStore));
        KeyTool.main(buildKeyToolListArgs(providerProperties.trustStore));
        Security.addProvider(new VProvider());
        openKeystore(cipherProperties.keyStore, cipherProperties.keyAlias);
        generateKey();
        listProviders();
        cipherContext.config(cipherProperties, properties.keyStorePass.toCharArray(), properties.keyPass.toCharArray());
        server.config(cipherContext);
        server.start();
        providerContext.config(providerProperties, properties.keyStorePass.toCharArray(), properties.keyPass.toCharArray());
        server.close();
    }
        
    private void generateKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");        
        generator.init(256, new SecureRandom());
        SecretKey key = generator.generateKey();
        logger.info(key.getAlgorithm(), key.getFormat(), key.getEncoded().length, Base64.encode(key.getEncoded()));
    }
    
    private String[] buildKeyToolGenKeyPairArgs(String keyStoreFile, String keyAlias, String cn) throws Exception {
        return new String[] {
            "-genkeypair", 
            "-keyalg", properties.keyAlg, 
            "-keystore", keyStoreFile, 
            "-storepass", properties.keyStorePass,
            "-alias", keyAlias, 
            "-keypass", properties.keyPass, 
            "-dname", String.format("CN=%s, OU=Development, O=venigmasecured.com, L=Cape Town, S=WP, C=za", cn)
        };
    }

    private String[] buildKeyToolExportCertArgs(String keyStore, String alias, String certFile) {
        return new String[] {
            "-export", 
            "-keystore", keyStore, 
            "-storepass", properties.keyStorePass,
            "-alias", alias, 
            "-keypass", properties.keyPass, 
            "-file", certFile, 
        };
    }
    
    private String[] buildKeyToolImportCertArgs(String trustStore, String trustAlias, String certFile) {
        return new String[] {
            "-import", 
            "-noprompt",
            "-keystore", trustStore, 
            "-storepass", properties.keyStorePass,
            "-alias", trustAlias,
            "-file", certFile,
        };
    }

    private String[] buildKeyToolListArgs(String keyStore) throws Exception {
        return new String[] {
            "-list", 
            "-keystore", keyStore, 
            "-storepass", properties.keyStorePass
        };
    }
    
    private void openKeystore(String keyStoreFile, String keyAlias) throws Exception {
        File file = new File(keyStoreFile);
        KeyStore keyStore = KeyStore.getInstance("JKS", "VProvider");
        keyStore.load(new FileInputStream(file), properties.keyStorePass.toCharArray());
        logger.info("KeyStore provider", keyStore.getProvider().getName());
        Key key = keyStore.getKey(keyAlias, properties.keyPass.toCharArray());
        logger.info(key.getAlgorithm(), key.getFormat());
    }

    private void listProviders() {
        for (Provider provider : Security.getProviders()) {
            logger.info(provider.getName());
        }
    }
    
    public static void main(String[] args) {
        try {
            new VTest().start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    
}
