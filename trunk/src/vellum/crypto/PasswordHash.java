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

    private int iterationCount;
    private int keySize;
    private byte[] hash;
    private byte[] salt;
    private byte[] iv;
    private long millis;
    
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
        int version = 1;
        if (version == 0) {
            stream.write(0);
            writeObject0(new ObjectOutputStream(stream));
        } else {
            stream.write(1);
            writeObject1(stream);
        }
    }

    private void readObject(InputStream stream) throws IOException {
        int version = stream.read();
        if (version == 0) {
            readObject0(new ObjectInputStream(stream));
        } else if (version == 1) {
            readObject1(stream);
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

    private void writeObject0(ObjectOutputStream stream) throws IOException {
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

    private void readObject0(ObjectInputStream stream) throws IOException {
        iterationCount = stream.readInt();
        keySize = stream.readShort();
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        stream.read(hash);
        stream.read(salt);
        stream.read(iv);
    }

    private static final int[] ITERATION_COUNT_MAP = {
        1000, 2000, 10000, 20000, 30000, 60000, 300000
    };
        
    public void writeObject1(OutputStream stream) throws IOException {
        if (keySize % 8 != 0) {
            throw new IOException("Invalid key size " + keySize);
        }
        int iterationCountIndex = Arrays.binarySearch(ITERATION_COUNT_MAP, iterationCount);
        if (iterationCountIndex < 0) {
            throw new IOException("Invalid iteration count " + iterationCount);
        }
        stream.write(hash.length);
        stream.write(salt.length);
        stream.write(iv.length);
        stream.write(iterationCountIndex);
        stream.write(keySize / 8);
        stream.write(hash);
        stream.write(salt);
        stream.write(iv);
    }

    public void readObject1(InputStream stream) throws IOException {
        hash = new byte[stream.read()];
        salt = new byte[stream.read()];
        iv = new byte[stream.read()];
        iterationCount = ITERATION_COUNT_MAP[stream.read()];
        keySize = stream.read() * 8;
        stream.read(hash);
        stream.read(salt);
        stream.read(iv);
    }
}
