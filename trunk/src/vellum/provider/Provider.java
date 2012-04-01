/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */

public class Provider extends java.security.Provider {
    
    public Provider() {
        super("VProvider", 1.0, "Provides KeyStore.JCEKS");
        put("KeyStore.JCEKS", KeyStoreSpi.class.getName());
        put("Cipher.AES", CipherSpi.class.getName());
    }

    
}
