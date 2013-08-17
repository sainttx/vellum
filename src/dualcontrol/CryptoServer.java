
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLServerSocketFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class CryptoServer {
    static Logger logger = Logger.getLogger(CryptoServer.class);
    KeyStore keyStore;
    
    public static void main(String[] args) throws Exception {
        logger.info("args: " + Arrays.toString(args));
        if (args.length != 7) {
            System.err.println("usage: localAddress port backlog count remoteAddress keystore storepass");
        } else {
            new CryptoServer().run(InetAddress.getByName(args[0]), Integer.parseInt(args[1]), 
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]), 
                    args[4], args[5], args[6].toCharArray());
        }
    }    
    
    private void run(InetAddress localAddress, int port, int backlog, int count, 
            String remoteHostAddress, String keyStoreName, char[] storepass) 
            throws Exception {
        logger.info(String.format("loading keysore %s", keyStoreName));
        keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(keyStoreName), storepass);
        DualControl.init();
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket serverSocket = ssf.createServerSocket(port, backlog, localAddress);
        while (true) {
            Socket socket = serverSocket.accept();
            logger.debug(socket.getInetAddress().getHostAddress());
            if (socket.getInetAddress().getHostAddress().equals(remoteHostAddress)) {
                handle(socket);
            }
            socket.close();
            if (count > 0 && --count == 0) break;
        }        
    }

    private void handle(Socket socket) throws Exception {        
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int length = dis.readShort();
        byte[] bytes = new byte[length];
        dis.readFully(bytes);
        String data = new String(bytes);
        logger.info(String.format("read %d bytes: %s", length, data));
        String[] fields = data.split(":");
        logger.info(String.format("handle %d fields: %s", fields.length, Arrays.toString(fields)));
        SecretKey key = DualControl.loadKey(keyStore, fields[0]);
        logger.info("keyalg " + key.getAlgorithm());
        Cipher cipher = Cipher.getInstance(fields[1]);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        logger.info("mode " + fields[2]);
        if (fields[2].equals("DECRYPT")) {
            byte[] ivBytes = Base64.decodeBase64(fields[3]);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            bytes = cipher.doFinal(Base64.decodeBase64(fields[4]));
            logger.info("decrypted " + new String(bytes));
            dos.writeShort(ivBytes.length);
            dos.write(ivBytes);
            dos.writeShort(bytes.length);
            dos.write(bytes);
        } else if (fields[2].equals("ENCRYPT")) {
            byte[] ivBytes = getIv(fields[3]);
            logger.info("iv " + Base64.encodeBase64String(ivBytes));
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);    
            bytes = cipher.doFinal(fields[4].getBytes());
            logger.info("encrypted " + Base64.encodeBase64String(bytes));
            dos.writeShort(ivBytes.length);
            dos.write(ivBytes);
            dos.writeShort(bytes.length);
            dos.write(bytes);
        }
    }
    
    private static byte[] getIv(String string) {
        if (string.length() > 2) {
            return Base64.decodeBase64(string);
        }
        int length = Integer.parseInt(string);
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;        
    }
}

