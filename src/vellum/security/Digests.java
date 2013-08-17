
package vellum.security;

import java.security.MessageDigest;

/**
 *
 * @author evans
 */
public class Digests {

    public static byte[] sha1(byte[] bytes) throws Exception {
        return MessageDigest.getInstance("SHA-1").digest(bytes);
    }
    
}
