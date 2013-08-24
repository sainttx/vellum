
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlConsole {
    final static Logger logger = Logger.getLogger(DualControlConsole.class);
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        new DualControlConsole().run();
        
    }
    public void run() throws Exception {
        Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                    createSocket(HOST, PORT);
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String prompt = dis.readUTF();
            char[] password = readPassword(prompt);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(new String(password));
            String message = dis.readUTF();
            System.console().writer().println(message);
        } finally {
            socket.close();
        }
    }    

    char[] readPassword(String prompt) throws Exception {
        char[] password = System.console().readPassword(prompt);
        DualControlPasswords.verifyPassword(password);
        return password;
    }

}
