/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import vellum.datatype.Millis;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Integers;

/**
 *
 * @author evan
 */
public class PasswordHash {
    private static Logr logger = LogrFactory.getLogger(PasswordHash.class);

    int iterationCount;
    int keySize;
    byte[] hash;
    byte[] salt;
    byte[] iv;
    long millis;
    
    public PasswordHash(char[] password, int iterationCount, int keySize) 
            throws GeneralSecurityException {
        this.iterationCount = iterationCount;
        this.keySize = keySize;
        this.salt = PasswordSalts.nextSalt();
        this.hash = Passwords.hashPassword(password, salt, iterationCount, keySize);
        this.iv = new byte[0];
    }

    public PasswordHash(byte[] bytes) throws IOException {
        readObject(new ByteArrayInputStream(bytes));
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        writeObject(stream);
        return stream.toByteArray();
    }
    
    private void writeObject(OutputStream stream) throws IOException {
        if (hash.length > 255) {
            throw new IOException("Invalid hash length");
        }
        int version = 0;
        if (version == 1) {
            stream.write(1);
            new PasswordHashSerializer1(this).writeObject(stream);
        } else {
            stream.write(0);
            writeObject(new ObjectOutputStream(stream));
        }
    }

    private void readObject(InputStream stream) throws IOException {
        int version = stream.read();
        if (version == 0) {
            readObject(new ObjectInputStream(stream));
        } else if (version == 1) {
            new PasswordHashSerializer1(this).readObject(stream);
        } else {
            throw new IOException("version not supported " + version);
        }
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
                    
    public static boolean verifyBytes(byte[] bytes) {
        return bytes.length > 24;
    }    

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeInt(iterationCount);
        stream.writeShort(keySize);
        stream.write(hash.length);
        stream.write(salt.length);
        stream.write(iv.length);
        stream.write(hash);
        stream.write(salt);
        stream.write(iv);
        stream.flush();
    }

    private void readObject(ObjectInputStream stream) throws IOException {
        iterationCount = stream.readInt();
        keySize = stream.readShort();
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        stream.read(hash);
        stream.read(salt);
        stream.read(iv);
    }
}
