/*
 * Copyright Evan Summers
 * 
 */
package venigma.data;

import vellum.util.Args;

/**
 *
 * @author evan
 */
public class KeyEntity implements IdEntity {
    String keyAlias;
    int revisionNumber;
    int keySize;
    KeyType keyType;
    byte[] encryptedKey;
    byte[] decryptedKey;

    public KeyEntity() {
    }
        
    public KeyEntity(String keyAlias, int revisionNumber, int keySize) {
        this.keyAlias = keyAlias;
        this.revisionNumber = revisionNumber;
        this.keySize = keySize;
    }
    
    @Override
    public Comparable getId() {
        return keyAlias;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }
    
    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String buildKeystoreAlias() {
        return keyAlias + "." + revisionNumber;
    }

    public void incrementRevisionNumber() {
        revisionNumber++;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setEncryptedKey(byte[] encryptedKey) {
        this.encryptedKey = encryptedKey;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }

    public void setDecryptedKey(byte[] decryptedKey) {
        this.decryptedKey = decryptedKey;
    }

    public byte[] getDecryptedKey() {
        return decryptedKey;
    }
    
    @Override
    public String toString() {
        return Args.formatPrint(keyAlias, revisionNumber, keySize);
    }

}
