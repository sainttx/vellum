
package dualcontrol;

import java.net.Socket;

/**
 *
 * @author evans
 */
public class FileClient {

    public static byte[] read(String hostAddress, int port) throws Exception {
        Socket socket = DualControl.createSSLContext().getSocketFactory().
                createSocket(hostAddress, port);
        byte[] bytes = DualControl.readBytes(socket.getInputStream());
        socket.close();
        return bytes;
    }    
    
}
