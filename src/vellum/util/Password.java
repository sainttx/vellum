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
public class Password {
    byte[] password;
    byte[] salt;
    int revisionIndex;

    public Password(byte[] packedBytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(packedBytes);
        if (packedBytes.length != stream.read()) {
            throw new IOException();
        }
        password = new byte[stream.read()];
        salt = new byte[stream.read()];
        revisionIndex = stream.read();
        if (packedBytes.length != salt.length + password.length + 4) {
            throw new IOException();
        }
        stream.read(password);
        stream.read(salt);
    }
    
    public Password(byte[] password, byte[] salt, int revisionIndex) {
        this.password = password;
        this.salt = salt;
        this.revisionIndex = revisionIndex;
    }

    public byte[] getPassword() {
        return password;
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
            stream.write(salt.length + password.length + 4);
            stream.write(password.length);
            stream.write(salt.length);
            stream.write(revisionIndex);
            stream.write(password);
            stream.write(salt);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }       
}
