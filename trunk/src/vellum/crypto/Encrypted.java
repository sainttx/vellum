/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

/**
 *
 * @author evan
 */
public class Encrypted {
    private final byte[] iv;
    private final byte[] encryptedBytes;

    public Encrypted(byte[] iv, byte[] encryptedBytes) {
        this.iv = iv;
        this.encryptedBytes = encryptedBytes;
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getEncryptedBytes() {
        return encryptedBytes;
    }   
}
