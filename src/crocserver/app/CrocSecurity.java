/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import java.util.Random;
import org.apache.commons.codec.binary.Base32;

/**
 *
 * @author evan
 */
public class CrocSecurity {
 
    public String createDname(String cn, String ou, String o, String l, String s, String c) {
        return String.format("CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s", cn, ou, o, l, s, c);
    }

    public static String generateSecret() {
        byte[] bytes = new byte[10];
        new Random().nextBytes(bytes);
        return new String(new Base32().encode(bytes));
    }

    public static String getQRBarcodeURL(String userName, String serverName, String secret) {
        return "https://chart.googleapis.com/chart?chs=200x200&chld=M|0&cht=qr&chl=" + 
                "otpauth%3A%2F%2Ftotp%2F" + userName + '@' + serverName +  
                "%3Fsecret%3D" + secret;
    }           
}
