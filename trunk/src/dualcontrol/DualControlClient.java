
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlClient {
    final static Logger logger = Logger.getLogger(DualControlClient.class);
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String prompt = dis.readUTF();
        char[] passwd = System.console().readPassword(prompt);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(new String(passwd));
        socket.close();
    }    
}
