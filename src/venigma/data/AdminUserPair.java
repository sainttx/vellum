/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package venigma.data;

import venigma.entity.IdPair;
import venigma.entity.AbstractPair;

/**
 *
 * @author evan.summers
 */
public class AdminUserPair extends AbstractPair {
    char[] password;
    
    public AdminUserPair() {
    }

    public AdminUserPair(IdPair idPair) {
        super(idPair);
    }
    
    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }  
}
