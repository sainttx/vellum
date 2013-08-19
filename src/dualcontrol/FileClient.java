
package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author evans
 */
public class FileClient {

    public static byte[] read(String hostAddress, int port) throws Exception {
        Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                createSocket(hostAddress, port);
        byte[] bytes = readBytes(socket.getInputStream());
        socket.close();
        return bytes;
    }    
    
    public static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int b = inputStream.read();
            if (b < 0) {
                return baos.toByteArray();
            }
            baos.write(b);
        }
    }
    
}
