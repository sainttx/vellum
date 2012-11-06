/*
 * Copyright Evan Summers
 * 
 */
package vellumdemo.totp;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Strings;

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
        String chl = "otpauth%3A%2F%2Ftotp%2F" + user + '@' + host + "%3Fsecret%3D" + secret;
        System.out.println(Strings.decodeUrl(chl));
        return "http://chart.apis.google.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=" + chl;
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
