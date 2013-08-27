/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */

package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import vellum.datatype.Nanos;

/**
 *
 * @author evans
 */
public class CryptoHandler {
    static final int ivLength = 8;
    static final boolean enableGetKey = Boolean.getBoolean("enableGetKey");
    static final Logger logger = Logger.getLogger(CryptoHandler.class);
    DualControlSession dualControl;
    byte[] ivBytes;
    byte[] dataBytes;
    DataOutputStream dos;
    
    public void handle(DualControlSession dualControl, Socket socket) throws Exception {
        try {
            this.dualControl = dualControl;
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int length = dis.readShort();
            byte[] bytes = new byte[length];
            dis.readFully(bytes);
            String data = new String(bytes);
            String[] fields = data.split(":");
            String mode = fields[0];
            String alias = fields[1];
            this.dos = new DataOutputStream(socket.getOutputStream());
            if (enableGetKey && mode.equals("GETKEY")) {
                SecretKey key = dualControl.loadKey(alias);
                dos.writeUTF(key.getAlgorithm());
                write(key.getEncoded());                
            } else {            
                cipher(mode, alias, fields[2], fields[3], fields[4]);
            }
        } finally {
            dos.close();
        }
    }

    public static String join(Object ... args) {
        return Arrays.toString(args);
    }
    
    private void cipher(String mode, String alias, String transformation, 
            String ivString, String dataString) throws Exception {
        logger.debug(join("cipher", alias, transformation, mode));
        SecretKey key = dualControl.loadKey(alias);
        logger.debug("keyalg " + key.getAlgorithm());
        Cipher cipher = Cipher.getInstance(transformation);
        logger.debug("mode " + mode);
        if (mode.equals("DECRYPT")) {
            this.ivBytes = Base64.decodeBase64(ivString);
            logger.debug("iv " + Base64.encodeBase64String(ivBytes));
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            this.dataBytes = cipher.doFinal(Base64.decodeBase64(dataString));
            write(ivBytes, dataBytes);
        } else if (mode.equals("ENCRYPT")) {
            this.ivBytes = getIvBytes(ivString);
            logger.debug("iv " + Base64.encodeBase64String(ivBytes));
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            long startTime = System.nanoTime();
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);    
            this.dataBytes = cipher.doFinal(dataString.getBytes());
            logger.info("encrypt time nanos " + Nanos.elapsed(startTime));
            write(ivBytes, dataBytes);
        }
    }
    
    private byte[] getIvBytes(String ivString) {
        if (ivString.length() > 2) {
            return Base64.decodeBase64(ivString);
        } 
        int ivLength = Integer.parseInt(ivString);
        this.ivBytes = new byte[ivLength];
        new SecureRandom().nextBytes(ivBytes);
        return ivBytes;
    }

    private void write(byte[] ivBytes, byte[] dataBytes) throws Exception {
        write(ivBytes);
        write(dataBytes);
        logger.debug("ivBytes " + ivBytes.length);
        logger.debug("dataBytes " + dataBytes.length);
    }    
    
    private void write(byte[] bytes) throws Exception {
        dos.writeShort(bytes.length);
        dos.write(bytes);
    }    
    
}

