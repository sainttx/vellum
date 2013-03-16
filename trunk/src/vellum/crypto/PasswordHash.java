/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import vellum.datatype.Millis;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Args;

/**
 *
 * @author evan
 */
public class PasswordHash {
    private static Logr logger = LogrFactory.getLogger(PasswordHash.class);

    public static final byte VERSION = 0x00;
    
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
        readObject(new ObjectInputStream(new ByteArrayInputStream(bytes)));
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

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writeObject(new ObjectOutputStream(stream));
        return stream.toByteArray();
    }
                    
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.write(VERSION);
        stream.writeInt(iterationCount);
        stream.writeShort(keySize);
        if (hash.length > 255) {
            throw new IOException();
        }
        stream.write(hash.length);
        stream.write(salt.length);
        stream.write(iv.length);
        stream.write(hash);
        stream.write(salt);
        stream.write(iv);        
        stream.flush();
    }

    private void readObject(ObjectInputStream stream) throws IOException {
        if (stream.read() != VERSION) {
            throw new IOException("version mismatch");
        }
        iterationCount = stream.readInt();
        keySize = stream.readShort();
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        stream.read(hash);
        stream.read(salt);
        stream.read(iv);        
    }
    
    public static boolean verifyBytes(byte[] bytes) {
        return bytes.length >= 42;
    }    
}
