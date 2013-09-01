/*
 *       Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package crocserver.app;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;

/**
 *
 * @author evan.summers
 */
public class CrocSecurity {

    public static String createSecret() {
        byte[] bytes = new byte[10];
        new SecureRandom().nextBytes(bytes);
        return new Base32().encodeAsString(bytes);
    }

    public static Mac createHmac(byte[] secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");
        mac.init(signKey);
        return mac;
    }

    public static String createCode(byte[] secret, String string, long value) throws Exception {
        Mac mac = createHmac(secret);
        mac.update(string.getBytes());
        return new Base32().encodeAsString(mac.doFinal(toByteArray(value)));
    }

    public static String createCode(byte[] secret, long value) throws Exception {
        Mac mac = createHmac(secret);
        return new Base32().encodeAsString(mac.doFinal(toByteArray(value)));
    }

    private static byte[] toByteArray(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        return buffer.array();
    }

    public static String getTotpUrl(String userName, String serverName, String secret) {
        return String.format("otpauth://totp/%s@%s?secret=%s", userName, serverName, secret);
    }

    public static String getQrCodeUrl(String userName, String serverName, String secret) {
        return "https://chart.googleapis.com/chart?chs=200x200&chld=M|0&cht=qr&chl="
                + "otpauth%3A%2F%2Ftotp%2F" + userName + '@' + serverName
                + "%3Fsecret%3D" + secret;
    }
}
