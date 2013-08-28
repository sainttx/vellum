/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import vellum.security.Digests;
import vellum.util.Chars;

/**
 *
 * @author evans
 */
public class DualControlConsole {

    private final static int PORT = 4444;
    private final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        Socket socket = DualControlSSLContextFactory.createSSLContext(
                VellumProperties.systemProperties).getSocketFactory().
                createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String purpose = dis.readUTF();
        char[] password = System.console().readPassword(
                "Enter password for " + purpose + ": ");
        String invalidMessage = new DualControlPasswordVerifier(
                VellumProperties.systemProperties).getInvalidMessage(password);
        if (invalidMessage != null) {
            System.err.println(invalidMessage);
        } else {
            String hash = Digests.sha1String(Chars.getBytes(password));
            Arrays.fill(password, (char) 0);
            password = System.console().readPassword(
                    "Re-enter password for " + purpose + ": ");
            if (!Digests.sha1String(Chars.getBytes(password)).equals(hash)) {
                System.err.println("Passwords don't match.");
            } else {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                writeChars(dos, password);
                String message = dis.readUTF();
                System.console().writer().println(message);
            }
            Arrays.fill(password, (char) 0);
        }
        socket.close();
    }

    public static char[] writeChars(DataOutputStream dos, char[] chars) throws IOException {
        dos.writeShort(chars.length);
        for (int i = 0; i < chars.length; i++) {
            dos.writeChar(chars[i]);
        }
        return chars;
    }
}
