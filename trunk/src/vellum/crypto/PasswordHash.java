/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author evan
 */
public class PasswordHash {
    private static final byte version = 0x00;
    int iterationCountExponent;
    int keySize;
    byte[] hash;
    byte[] salt;
    byte[] iv;

    public PasswordHash(byte[] hash, byte[] salt, byte[] iv, int iterationCountExponent, int keySize) {
        this.hash = hash;
        this.salt = salt;
        this.iv = iv;
        this.iterationCountExponent = iterationCountExponent;
        this.keySize = keySize;
    }

    public PasswordHash(char[] password, int iterationCountExponent, int keySize) {
        this.iterationCountExponent = iterationCountExponent;
        this.keySize = keySize;
        this.salt = PasswordSalts.nextSalt();
        this.hash = Passwords.hashPassword(password, salt, iterationCountExponent, keySize);
    }
        
    public PasswordHash(byte[] packedBytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (stream.read() != version || stream.read() != packedBytes.length) {
            throw new IOException();
        }
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        iterationCountExponent = stream.read();
        keySize = 16*stream.read();
        stream.read(hash);
        stream.read(salt);
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
    
    public int getIterationCountExponent() {
        return iterationCountExponent;
    }

    public int getKeySize() {
        return keySize;
    }

    public boolean matches(char[] password) {
        return Arrays.equals(hash, Passwords.hashPassword(password, salt, iterationCountExponent, keySize));
    }
        
    public byte[] pack() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(version);
            stream.write(salt.length + hash.length + 7);
            stream.write(hash.length);
            stream.write(salt.length);
            stream.write(iv.length);
            stream.write(iterationCountExponent);
            stream.write(keySize/16);
            stream.write(hash);
            stream.write(salt);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        if (packedBytes.length != length || length != hashLength + saltLength + ivLength + 7) {
            return false;
        }
        return true;
    }
}
