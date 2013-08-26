
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;
import vellum.security.Digests;
import vellum.util.Chars;

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
        String hash = Digests.sha1String(Chars.getBytes(password));
        Arrays.fill(password, (char) 0);
        password = System.console().readPassword("Re-enter password: ");
        if (!Digests.sha1String(Chars.getBytes(password)).equals(hash)) {
            System.err.println("Passwords don't match.");
        } else {
            String errorMessage = DualControlPasswordVerifier.getErrorMessage(password);
            if (errorMessage != null) {
                System.err.println(errorMessage);
            } else {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(new String(password));
                String message = dis.readUTF();
                System.console().writer().println(message);
            }
        }
        Arrays.fill(password, (char) 0);
        socket.close();        
    }
}
