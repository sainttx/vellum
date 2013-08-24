
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author evans
 */
public class DualControlConsole {
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                    createSocket(HOST, PORT);
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String prompt = dis.readUTF();
            char[] password = System.console().readPassword(prompt + ": ");
            DualControlPasswords.assertValid(password);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(new String(password));
            String message = dis.readUTF();
            System.console().writer().println(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            socket.close();
        }
    }    
}
