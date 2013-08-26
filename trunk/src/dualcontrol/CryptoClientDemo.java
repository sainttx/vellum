/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author evans
 */
public class CryptoClientDemo {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("CryptoClientDemo usage: hostAddress port text");
        } else {
            new CryptoClientDemo().run(args[0], Integer.parseInt(args[1]), args[2].getBytes());
        }
    }

    private void run(String hostAddress, int port, byte[] data) throws Exception {
        Socket socket = DualControlSSLContextFactory.createSSLContext().getSocketFactory().
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
        if (new String(data).contains("DECRYPT")) {
            System.err.printf("INFO CryptoClientDemo decrypted %s\n", new String(bytes)); 
        } else {
            System.out.printf("%s:%s\n", Base64.encodeBase64String(ivBytes), Base64.encodeBase64String(bytes));            
        }
        socket.close();
    }
}
