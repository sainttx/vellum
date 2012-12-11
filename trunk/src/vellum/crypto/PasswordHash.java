/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 *
 * @author evan
 */
public class PasswordHash {
    private static final byte version = 0x00;
    
    int iterationCount;
    int keySize;
    byte[] hash;
    byte[] salt;
    byte[] iv;

    public PasswordHash(byte[] hash, byte[] salt, byte[] iv, 
            int iterationCount, int keySize) {
        this.hash = hash;
        this.salt = salt;
        this.iv = iv;
        this.iterationCount = iterationCount;
        this.keySize = keySize;
    }

    public PasswordHash(char[] password, int iterationCount, int keySize) 
            throws GeneralSecurityException {
        this.iterationCount = iterationCount;
        this.keySize = keySize;
        this.salt = PasswordSalts.nextSalt();
        this.hash = Passwords.hashPassword(password, salt, iterationCount, keySize);
        this.iv = new byte[0];
    }

    public PasswordHash(char[] secret, byte[] salt, byte[] iv, int iterationCount, int keySize) 
            throws GeneralSecurityException {
        this.iterationCount = iterationCount;
        this.keySize = keySize;
        this.salt = salt;
        this.hash = Passwords.hashPassword(secret, salt, iterationCount, keySize);
        this.iv = iv;
    }
    
    public PasswordHash(byte[] packedBytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (stream.read() != version || stream.read() != packedBytes.length) {
            throw new IOException();
        }
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        iterationCount = stream.read()*256 + stream.read();
        keySize = 16 * stream.read();
        stream.read(hash);
        stream.read(salt);
        stream.read(iv);
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getKeySize() {
        return keySize;
    }

    public boolean matches(char[] password) throws GeneralSecurityException {
        return Arrays.equals(hash, Passwords.hashPassword(password, salt, iterationCount, keySize));
    }

    public byte[] pack() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(version);
            stream.write(salt.length + hash.length + iv.length + 8);
            stream.write(hash.length);
            stream.write(salt.length);
            stream.write(iv.length);
            stream.write(iterationCount/256);
            stream.write(iterationCount%256);
            stream.write(keySize/16);
            stream.write(hash);
            stream.write(salt);
            stream.write(iv);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void encryptSalt(PBECipher cipher) throws GeneralSecurityException {
        assert iv.length == 0;
        Encrypted encryptedSalt = cipher.encrypt(salt);
        salt = encryptedSalt.getEncryptedBytes();
        iv = encryptedSalt.getIv();        
    }

    public void decryptSalt(PBECipher cipher) throws GeneralSecurityException {
        assert iv.length > 0;
        salt = cipher.decrypt(salt, iv);
        iv = new byte[0];
    }

    public static boolean isPacked(byte[] packedBytes) {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (stream.read() != version) {
            return false;
        }
        int length = stream.read();
        int hashLength = stream.read();
        int saltLength = stream.read();
        int ivLength = stream.read();
        if (packedBytes.length != length || 
                length != hashLength + saltLength + ivLength + 8) {
            return false;
        }
        return true;
    }        
}
