
package dualcontrol;

import java.io.DataOutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public abstract class DummyDualControlClient {
    final static Logger logger = Logger.getLogger(DummyDualControlClient.class);
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            logger.error("usage: passwd");
        } else {
            write(args[0].toCharArray());
        }
    }

    public static void write(char[] chars) throws Exception {
        Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                createSocket(HOST, PORT);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(new String(chars));
        socket.close();
    }
}
