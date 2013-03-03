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
import vellum.datatype.Millis;

/**
 *
 * @author evan
 */
public class PasswordHash {
    private static final byte version = 0x00;
    
    private int iterationCount;
    private int keySize;
    private byte[] hash;
    private byte[] salt;
    private byte[] iv;
    private long millis;
    
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

    public PasswordHash(byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        if (stream.read() != version || stream.read() != bytes.length) {
            throw new IOException();
        }
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        iterationCount = stream.read()<<16;
        iterationCount |= stream.read()<<8;
        iterationCount |= stream.read();
        keySize = stream.read()<<8;
        keySize |= stream.read();
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

    public byte[] getBytes() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(version);
            stream.write(salt.length + hash.length + iv.length + 10);
            stream.write(hash.length);
            stream.write(salt.length);
            stream.write(iv.length);
            stream.write(iterationCount >>> 16);
            stream.write((iterationCount >>> 8) & 0xff);
            stream.write(iterationCount & 0xff);
            stream.write(keySize >>> 8);
            stream.write(keySize & 0xff);
            stream.write(hash);
            stream.write(salt);
            stream.write(iv);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public boolean matches(char[] password) throws GeneralSecurityException {
        assert iv.length == 0;
        millis = System.currentTimeMillis();
        try {
            return Arrays.equals(hash, Passwords.hashPassword(password, salt, iterationCount, keySize));
        } finally {
            millis = Millis.elapsed(millis);
        }
    }

    public long getMillis() {
        return millis;
    }
    
    public static boolean verifyBytes(byte[] packedBytes) {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (stream.read() != version) {
            return false;
        }
        int length = stream.read();
        int hashLength = stream.read();
        int saltLength = stream.read();
        int ivLength = stream.read();
        if (packedBytes.length != length || 
                length != hashLength + saltLength + ivLength + 10) {
            return false;
        }
        return true;
    }        
}
