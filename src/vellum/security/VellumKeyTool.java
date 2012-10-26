/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import java.io.File;
import sun.security.tools.KeyTool;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;
import vellum.util.Strings;

/**
 *
 * @author evan
 */
public class VellumKeyTool {

    Logr logger = LogrFactory.getLogger(getClass());
    String keyStoreType = "JKS";
    String providerName = null;
    String keyAlgName = "RSA";
    String dname = "CN=localhost, OU=local, O=local, L=local, S=local, C=local";
    String keyStorePath;
    String trustStorePath;
    String keyStorePassword;
    String trustStorePassword;

    private void main() throws Exception {
        keyStorePath = System.getProperty("javax.net.ssl.keyStore");
        trustStorePath = System.getProperty("javax.net.ssl.trustStore");
        keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
        trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
        dname = System.getProperty("dname");
        File keyStoreFile = new File(keyStorePath);
        File trustStoreFile = new File(trustStorePath);
        String keyAlias = Streams.removeFileNameExtension(keyStoreFile);
        String certFilePath = keyStoreFile.getParent() + File.separator + keyAlias + ".pem";
        File certFile = new File(certFilePath);
        logger.info("certFile", certFilePath);
        if (keyStoreFile.exists() || trustStoreFile.exists() || certFile.exists()) {
            if (dname == null) {
                throw new Exception("require dname");
            }
            if (!keyStoreFile.delete()) {
                throw new Exception("unable to delete " + keyStoreFile.getPath());
            }
            trustStoreFile.delete();
            certFile.delete();
        }
        logger.info("keyStoreFile", keyStoreFile.getPath(), keyStoreFile.exists());
        genKeyPair(keyStorePath, keyAlias);
        exportCert(keyStorePath, keyAlias, certFilePath);
        importCert(trustStorePath, keyAlias, certFilePath);
        list(keyStorePath);
        list(trustStorePath);
    }

    void genKeyPair(String keyStoreFile, String keyAlias) throws Exception {
        keyTool(new String[]{
                    "-genkeypair",
                    "-keyalg", keyAlgName,
                    "-keystore", keyStoreFile,
                    "-storetype", keyStoreType,
                    "-storepass", keyStorePassword,
                    "-alias", keyAlias,
                    "-keypass", keyStorePassword,
                    "-dname", dname
                });
    }

    void exportCert(String keyStore, String alias, String certFile) throws Exception {
        keyTool(new String[]{
                    "-export",
                    "-keystore", keyStore,
                    "-storetype", keyStoreType,
                    "-storepass", keyStorePassword,
                    "-alias", alias,
                    "-keypass", keyStorePassword,
                    "-file", certFile,
                    "-rfc"
                });
    }

    void importCert(String trustStore, String trustAlias, String certFile) throws Exception {
        keyTool(new String[]{
                    "-import",
                    "-noprompt",
                    "-keystore", trustStore,
                    "-storetype", keyStoreType,
                    "-storepass", trustStorePassword,
                    "-alias", trustAlias,
                    "-file", certFile
                });
    }

    void list(String keyStore) throws Exception {
        keyTool(new String[]{
                    "-list",
                    "-keystore", keyStore,
                    "-storetype", keyStoreType,
                    "-storepass", keyStorePassword
                });
    }

    void keyTool(String[] args) throws Exception {
        System.out.println(Strings.joinArray(" ", args));
        KeyTool.main(args);
    }

    public static void main(String[] args) throws Exception {
        try {
            new VellumKeyTool().main();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
