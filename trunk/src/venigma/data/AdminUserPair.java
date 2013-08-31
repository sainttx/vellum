/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
