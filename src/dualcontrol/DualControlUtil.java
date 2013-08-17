
package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 *
 * @author evans
 */
public abstract class DualControlUtil {
    
    public static byte[] sha1(byte[] bytes) throws Exception {
        return MessageDigest.getInstance("SHA-1").digest(bytes);
    }

    public static void invokeMain(Class mainClass, String[] args) throws Exception {
        System.err.printf("DualControl.invokeMain(%s, %s)\n", mainClass, Arrays.toString(args));
        mainClass.getMethod("main", String[].class)
                .invoke(mainClass.newInstance(), (Object) args);
    }    

    public static void println(OutputStream outputStream, Object data) {
        new PrintWriter(outputStream).println(data);
    }
}
