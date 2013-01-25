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
public class PBEHash {
    private static final byte version = 0x00;    
    private int iterationCount;
    private int keySize;
    private byte[] salt;
    private byte[] iv;
    private byte[] encrypted;

    public PBEHash(byte[] salt, int iterationCount, int keySize, 
            byte[] iv, byte[] encrypted) throws GeneralSecurityException {
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.keySize = keySize;
        this.iv = iv;
        this.encrypted = encrypted;
    }

    public PBEHash(byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        if (stream.read() != version || stream.read() != bytes.length) {
            throw new IOException();
        }
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        encrypted = new byte[stream.read()];
        iterationCount = stream.read()*256 + stream.read();
        keySize = 16 * stream.read();
        stream.read(salt);
        stream.read(iv);
        stream.read(encrypted);
    }

    public boolean isEncrypted() {
        return iv.length > 0;
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
    
    public int getIterationCount() {
        return iterationCount;
    }

    public int getKeySize() {
        return keySize;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getHash() {
        return encrypted;
    }
    
    public byte[] getBytes() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(version);
            stream.write(salt.length + iv.length + encrypted.length + 8);
            stream.write(salt.length);
            stream.write(iv.length);
            stream.write(encrypted.length);
            stream.write(iterationCount/256);
            stream.write(iterationCount%256);
            stream.write(keySize/16);
            stream.write(salt);
            stream.write(iv);
            stream.write(encrypted);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean matches(char[] password) throws GeneralSecurityException {
        return Arrays.equals(encrypted, Passwords.hashPassword(password, salt, iterationCount, keySize));
    }
    
    public static boolean verifyBytes(byte[] bytes) {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        if (stream.read() != version || 
            stream.read() != bytes.length || 
            stream.read() + stream.read() + stream.read() + 8 != bytes.length) {
            return false;
        }
        return true;
    }        
}
