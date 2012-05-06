/*
 * Copyright Evan Summers
 * 
 */
package venigma.data;

import vellum.type.ComparableTuple;

/**
 *
 * @author evan
 */
public class KeyId extends AbstractIdEntity {
    String keyAlias;
    int keyRevisionNumber;
    int keySize;
    KeyType keyType;
    byte[] salt;
    byte[] iv;
    
    public KeyId() {
    }
        
    public KeyId(String keyAlias, int revisionNumber, int keySize) {
        this.keyAlias = keyAlias;
        this.keyRevisionNumber = revisionNumber;
        this.keySize = keySize;
    }
    
    @Override
    public Comparable getId() {
        return ComparableTuple.newInstance(keyAlias, keyRevisionNumber);
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
    
    public byte[] getIv() {
        return iv;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getSalt() {
        return salt;
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

    public int getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    public void setKeyRevisionNumber(int keyRevisionNumber) {
        this.keyRevisionNumber = keyRevisionNumber;
    }
    
    public void incrementRevisionNumber() {
        keyRevisionNumber++;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public KeyType getKeyType() {
        return keyType;
    }
}
