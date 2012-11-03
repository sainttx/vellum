/*
 * Copyright Evan Summers
 * 
 */
package vellumdemo.totp;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 * from
 * http://thegreyblog.blogspot.com/2011/12/google-authenticator-using-it-in-your.html
 *
 * @author evan
 */
public class GenerateGoogleTotpQrUrl {
    static Logr logger = LogrFactory.getLogger(GenerateGoogleTotpQrUrl.class);

    String secret = "OVEK7TIJ3A3DM3M6";
    String user = "evanx";
    String host = "beethoven";
    
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&"
                + "chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, host, secret);
    }
    
    void test() throws Exception {
        System.out.println(getQRBarcodeURL(user, host, secret));
    }

    public static void main(String[] args) {
        try {
            new GenerateGoogleTotpQrUrl().test();
        } catch (Exception e) {
            logger.warn(e);
        }
    }
}
