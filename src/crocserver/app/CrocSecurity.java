/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

/**
 *
 * @author evan
 */
public class CrocSecurity {
 
    public String createDname(String cn, String ou, String o, String l, String s, String c) {
        return String.format("CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s", cn, ou, o, l, s, c);
    }

    
}
