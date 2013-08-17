package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class CryptoClientDemo {
    private static Logger logger = Logger.getLogger(CryptoClientDemo.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("usage: hostAddress port text");
        } else {
            new CryptoClientDemo().run(args[0], Integer.parseInt(args[1]), args[2].getBytes());
        }
    }

    private void run(String hostAddress, int port, byte[] data) throws Exception {
        logger.debug(String.format("hostAddress %s, port %d, %d bytes: %s", hostAddress, port, data.length, new String(data)));
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
        logger.debug(String.format("iv %d: %s", ivBytes.length, Base64.encodeBase64String(ivBytes)));
        logger.debug(String.format("bytes %d: %s", bytes.length, Base64.encodeBase64String(bytes)));
        if (new String(data).contains("ENCRYPT")) {
            logger.debug(String.format("%s:%s", Base64.encodeBase64String(ivBytes), Base64.encodeBase64String(bytes)));
        }
        socket.close();
    }
}
