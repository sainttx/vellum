/*
 * Copyright Evan Summers
 * 
 */
package venigma.provider;

import venigma.server.VCipherSpi;

/**
 *
 * @author evan
 */

public class VProvider extends java.security.Provider {
    public static final ClientContext providerContext  = new ClientContext();
    
    public VProvider() {
        super("VProvider", 1.0, "Provides KeyStore.JCEKS");
        put("KeyStore.JCEKS", KeyStoreSpi.class.getName());
        put("Cipher.AES", VCipherSpi.class.getName());
    }

    
}
