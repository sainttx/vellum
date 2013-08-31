/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package venigma.provider.keytool;

import venigma.test.*;

/**
 *
 * @author evan
 */
public class KeyToolBuilderlProperties {

    String trustKeyStorePass;
    String secretKeyStorePass;
    String privateKeyPass;
    String secretKeyPass;

    public String getPrivateKeyPass() {
        return privateKeyPass;
    }

    public void setPrivateKeyPass(String privateKeyPass) {
        this.privateKeyPass = privateKeyPass;
    }

    public String getSecretKeyPass() {
        return secretKeyPass;
    }

    public void setSecretKeyPass(String secretKeyPass) {
        this.secretKeyPass = secretKeyPass;
    }

    public String getSecretKeyStorePass() {
        return secretKeyStorePass;
    }

    public void setSecretKeyStorePass(String secretKeyStorePass) {
        this.secretKeyStorePass = secretKeyStorePass;
    }

    public String getTrustKeyStorePass() {
        return trustKeyStorePass;
    }

    public void setTrustKeyStorePass(String trustKeyStorePass) {
        this.trustKeyStorePass = trustKeyStorePass;
    }

    
}
