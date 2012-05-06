/*
 * Copyright Evan Summers
 * 
 */
package venigma.data;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import vellum.util.Args;
import venigma.server.CipherContext;
import venigma.server.storage.StorageException;
import venigma.server.storage.StorageExceptionType;

/**
 *
 * @author evan
 */
public class KeyInfo extends KeyId {
    byte[] encryptedKey;
    SecretKey secretKey;
    
    public KeyInfo() {
    }
        
    public KeyInfo(String keyAlias, int revisionNumber, int keySize) {
        super(keyAlias, revisionNumber, keySize);
    }

    public KeyInfo(KeyId keyId) {
        keyAlias = keyId.keyAlias;
        keyRevisionNumber = keyId.keyRevisionNumber;
        keySize = keyId.keySize;
    }
    
    public void setEncryptedKey(byte[] encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }

    public byte[] encrypt(SecretKey secretKey, char[] password, SecureRandom sr) throws Exception {
        salt = new byte[8];
        iv = new byte[16];
        sr.nextBytes(iv);
        sr.nextBytes(salt);
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, password);
        return cipher.doFinal(secretKey.getEncoded());
    }
    
    public void decrypt(char[] password) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, password);
        byte[] decryptedBytes = cipher.doFinal(encryptedKey);
        secretKey = new SecretKeySpec(decryptedBytes, "AES");        
    }
    
    private Cipher getCipher(int mode, char[] password) throws Exception {
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(CipherContext.PBKDF_ALGORITHM);
        SecretKey pbeKey = secretKeyFactory.generateSecret(spec);
        IvParameterSpec ips = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, pbeKey, ips);
        return cipher;
    }
        
    public SecretKey getSecretKey() throws StorageException {
        if (secretKey == null) {
            throw new StorageException(StorageExceptionType.KEY_NOT_DECRYPTED);
        }
        return secretKey;
    }

}
