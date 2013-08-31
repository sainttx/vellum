
package vellum.security;

import java.security.MessageDigest;

/**
 *
 * @author evan.summers
 */
public class Digests {

    public static byte[] sha1(byte[] bytes) throws Exception {
        return MessageDigest.getInstance("SHA-1").digest(bytes);
    }

    public static String sha1String(byte[] bytes) throws Exception {
        return new String(sha1(bytes));
    }
    
}
