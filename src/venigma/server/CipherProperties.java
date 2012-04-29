/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.util.ArrayList;
import java.util.List;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class CipherProperties {
    public char[] dataStorePassword;
    public char[] dataUserPassword;
    public char[] keyStorePassword;
    public char[] privateKeyPassword;
    public char[] trustKeyStorePassword;
    public char[] secretKeyStorePassword;
    public char[] secretKeyPassword;
    public List<AdminUser> userList = new ArrayList();
    
}
