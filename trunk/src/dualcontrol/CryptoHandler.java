
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class CryptoHandler {
    static Logger logger = Logger.getLogger(CryptoHandler.class);
    DualControlSession dualControl;
    KeyStore keyStore;
    DataOutputStream dos;
    
    public void handle(DualControlSession dualControl, Socket socket) throws Exception {        
        this.dualControl = dualControl;
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int length = dis.readShort();
        byte[] bytes = new byte[length];
        dis.readFully(bytes);
        String data = new String(bytes);
        logger.debug(String.format("read %d bytes: %s", length, data));
        String[] fields = data.split(":");
        logger.debug(String.format("handle %d fields: %s", fields.length, Arrays.toString(fields)));
        this.dos = new DataOutputStream(socket.getOutputStream());
        cipher(fields[0], fields[1], fields[2], fields[3], fields[4]);
        dos.close();
    }
    
    private void cipher(String alias, String transformation, String mode,
            String ivString, String dataString) throws Exception {
        SecretKey key = dualControl.loadKey(alias);
        logger.debug("keyalg " + key.getAlgorithm());
        Cipher cipher = Cipher.getInstance(transformation);
        logger.debug("mode " + mode);
        if (mode.equals("DECRYPT")) {
            byte[] ivBytes = Base64.decodeBase64(ivString);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(dataString));
            logger.debug("decrypted " + new String(bytes));
            write(ivBytes, bytes);
        } else if (mode.equals("ENCRYPT")) {
            byte[] ivBytes = getIv(ivString);
            logger.debug("iv " + Base64.encodeBase64String(ivBytes));
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);    
            byte[] bytes = cipher.doFinal(dataString.getBytes());
            logger.debug("encrypted " + Base64.encodeBase64String(bytes));
            write(ivBytes, bytes);
        }
    }

    private void write(byte[] ivBytes, byte[] bytes) throws Exception {
        dos.writeShort(ivBytes.length);
        dos.write(ivBytes);
        dos.writeShort(bytes.length);
        dos.write(bytes);
    }
    
    private static byte[] getIv(String string) {
        if (string.length() == 0 || string.length() > 2) {
            return Base64.decodeBase64(string);
        }
        int length = Integer.parseInt(string);
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;        
    }
}

