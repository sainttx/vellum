
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
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
    byte[] ivBytes;
    byte[] dataBytes;
    DataOutputStream dos;
    
    public void handle(DualControlSession dualControl, Socket socket) throws Exception {        
        this.dualControl = dualControl;
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int length = dis.readShort();
        byte[] bytes = new byte[length];
        dis.readFully(bytes);
        String data = new String(bytes);
        String[] fields = data.split(":");
        logger.debug("handlefields: " + Arrays.toString(fields));
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
        this.ivBytes = Base64.decodeBase64(ivString);
        logger.debug("iv " + Base64.encodeBase64String(ivBytes));
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        if (mode.equals("DECRYPT")) {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            this.dataBytes = cipher.doFinal(Base64.decodeBase64(dataString));
            write(ivBytes, dataBytes);
        } else if (mode.equals("ENCRYPT")) {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);    
            this.dataBytes = cipher.doFinal(dataString.getBytes());
            write(ivBytes, dataBytes);
        }
    }

    private void write(byte[] ivBytes, byte[] bytes) throws Exception {
        dos.writeShort(ivBytes.length);
        dos.write(ivBytes);
        dos.writeShort(bytes.length);
        dos.write(bytes);
    }    
}

