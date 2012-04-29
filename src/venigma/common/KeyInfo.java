/*
 * Copyright Evan Summers
 * 
 */
package venigma.common;

import vellum.util.Args;

/**
 *
 * @author evan
 */
public class KeyInfo implements IdEntity {
    String keyAlias;
    int revisionNumber;
    int keySize;
    
    public KeyInfo(String keyAlias, int revisionNumber, int keySize) {
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

    @Override
    public String toString() {
        return Args.formatPrint(keyAlias, revisionNumber, keySize);
    }

    
}
