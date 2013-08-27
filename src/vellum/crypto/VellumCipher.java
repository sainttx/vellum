/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import java.security.GeneralSecurityException;

/**
 *
 * @author evan
 */
public interface VellumCipher {
    public Encrypted encrypt(byte[] bytes) throws GeneralSecurityException;
    public byte[] decrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException;
    public byte[] encrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException;
}
