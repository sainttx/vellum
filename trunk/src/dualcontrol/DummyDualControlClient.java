
package dualcontrol;

import java.io.DataInputStream;
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
            System.err.println("usage: passwd");
        } else {
            send(args[0].toCharArray());
        }
    }

    public static void send(char[] password) throws Exception {
        try {
            Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                    createSocket(HOST, PORT);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String prompt = dis.readUTF();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(new String(password));
            socket.close();
            System.out.printf("OK send %s: %s\n", prompt, new String(password));
        } catch (Exception e) {
            System.out.println("ERROR send " + new String(password));
            throw e;
        }
    }
}
