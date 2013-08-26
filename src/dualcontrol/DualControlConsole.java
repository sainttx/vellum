
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
        Socket socket = DualControlSSLContextFactory.createSSLContext().getSocketFactory().
                createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String alias = dis.readUTF();
        char[] password = System.console().readPassword(
                "Enter password for remote key " + alias + ": ");
        String errorMessage = DualControlPasswords.getErrorMessage(password);
        if (errorMessage != null) {
            System.err.println(errorMessage);
        } else {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(new String(password));
            String message = dis.readUTF();
            System.console().writer().println(message);
        }
        socket.close();
    }
}
