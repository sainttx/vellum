/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author evan
 */
public class PasswordHash {
    int iterationCountExponent;
    int keySize;
    byte[] salt;
    byte[] hash;

    public PasswordHash(char[] password, int iterationCountExponent, int keySize) {
        this.iterationCountExponent = iterationCountExponent;
        this.keySize = keySize;
        this.salt = PasswordSalts.nextSalt();
        this.hash = Passwords.hashPassword(password, salt, iterationCountExponent, keySize);
    }
    
    public PasswordHash(byte[] hash, byte[] salt, int iterationCountExponent, int keySize) {
        this.hash = hash;
        this.salt = salt;
        this.iterationCountExponent = iterationCountExponent;
        this.keySize = keySize;
    }
    
    public PasswordHash(byte[] packedBytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (packedBytes.length != stream.read()) {
            throw new IOException();
        }
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
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
            stream.write(salt.length + hash.length + 5);
            stream.write(hash.length);
            stream.write(salt.length);
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
        int length = stream.read();
        int hashLength = stream.read();
        int saltLength = stream.read();
        if (packedBytes.length != length || length != hashLength + saltLength + 5) {
            return false;
        }
        return true;
    }
}
