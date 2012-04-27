/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

import venigma.common.IdEntity;

/**
 *
 * @author evan
 */
public class KeyInfo implements IdEntity {
    String keyAlias;
    int revisionNumber;

    @Override
    public Comparable getId() {
        return keyAlias;
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
    
}
