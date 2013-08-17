
package dualcontrol;

import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author evans
 */
public class FileClient {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: hostAddress port");
        } else {
            new FileClient().run(args[0], Integer.parseInt(args[1]));
        }
    }
    
    private void run(String hostAddress, int port) throws Exception {
        System.err.printf("FileClient %s:%d\n", hostAddress, port);
        byte[] bytes = readRemote(hostAddress, port);
        System.err.printf("FileClient read %d bytes\n", bytes.length);
    }
    
    public static byte[] readRemote(String hostAddress, int port) throws Exception {
        Socket socket = DualControl.createSSLContext().getSocketFactory().
                createSocket(hostAddress, port);
        byte[] bytes = DualControl.readBytes(socket.getInputStream());
        socket.close();
        return bytes;
    }    
    
}
