/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author evan
 */
public class PasswordHash {
    byte[] hash;
    byte[] salt;
    int revisionIndex;

    public PasswordHash(byte[] packedBytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (packedBytes.length != stream.read()) {
            throw new IOException();
        }
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        revisionIndex = stream.read();
        stream.read(hash);
        stream.read(salt);
    }
    
    public PasswordHash(byte[] hash, byte[] salt, int revisionIndex) {
        this.hash = hash;
        this.salt = salt;
        this.revisionIndex = revisionIndex;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public int getRevisionIndex() {
        return revisionIndex;
    }
    
    public byte[] pack() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(salt.length + hash.length + 4);
            stream.write(hash.length);
            stream.write(salt.length);
            stream.write(revisionIndex);
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
        if (packedBytes.length != length || length != hashLength + saltLength + 4) {
            return false;
        }
        return true;
    }

    public static boolean isPackedLatest(byte[] packedBytes) {
        if (!isPacked(packedBytes)) return false;
        try {
            return new PasswordHash(packedBytes).getRevisionIndex() == Passwords.LATEST_REVISION_INDEX;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }    
}
