package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author evans
 */
public class CryptoClient {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("usage: hostAddress port text");
        } else {
            new CryptoClient().run(args[0], Integer.parseInt(args[1]), args[2].getBytes());
        }
    }

    private void run(String hostAddress, int port, byte[] data) throws Exception {
        System.err.printf("CryptoClient %s:%d, %d bytes: %s\n", hostAddress, port, data.length, new String(data));
        Socket socket = DualControl.createSSLContext().getSocketFactory().
                createSocket(hostAddress, port);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeShort(data.length);
        dos.write(data);
        dos.flush();
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        byte[] ivBytes = new byte[dis.readShort()];
        dis.readFully(ivBytes);
        byte[] bytes = new byte[dis.readShort()];
        dis.readFully(bytes);
        System.err.printf("iv %d: %s\n", ivBytes.length, Base64.encodeBase64String(ivBytes));
        System.err.printf("bytes %d: %s\n", bytes.length, Base64.encodeBase64String(bytes));
        if (new String(data).contains("ENCRYPT")) {
            System.out.printf("%s:%s\n", Base64.encodeBase64String(ivBytes), Base64.encodeBase64String(bytes));
        }
        socket.close();
    }
}
