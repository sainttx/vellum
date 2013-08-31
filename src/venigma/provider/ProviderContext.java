/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package venigma.provider;

/**
 *
 * @author evan.summers
 */
public class ProviderContext extends ClientContext {

    String keyAlias = "default";

    public ProviderContext() {
    }
    
    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }
        
}
