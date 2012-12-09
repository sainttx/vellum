/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import java.io.IOException;
import sun.security.x509.X500Name;

/**
 *
 * @author evan
 */
public class Certificates {

    public static String createDname(String cn, String ou, String o, String l, String s, String c) {
        return String.format("CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s", cn, ou, o, l, s, c);
    }

    public static String getCommonName(String subject) {
        try {
            return new X500Name(subject).getCommonName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
