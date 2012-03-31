/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VCipherHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    VCipherContext context;
    Key key;
    Cipher aesEncryptCipher;
    Cipher aesDecryptCipher;
    Socket socket;
    VCipherRequest request;
    boolean running = true;
    
    public VCipherHandler(VCipherContext context) {
        this.context = context;
    }
    
    public void init() throws Exception {
        key = loadKey(context.config.secretKeyStore, context.config.secretAlias, 
                context.properties.secretKeyStorePassword, context.properties.secretKeyPassword);
        aesEncryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesDecryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesEncryptCipher.init(Cipher.ENCRYPT_MODE, key);
        logger.info("initialised");        
    }
    
    private Key loadKey(String keyStoreFile, String keyAlias, char[] storePass, char[] keyPass) throws Exception {
        File file = new File(keyStoreFile);
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(file), storePass);
        logger.info("loadKey", keyStore.getType(), keyStore.getProvider().getName());
        Key key = keyStore.getKey(keyAlias, keyPass);
        logger.info(key.getAlgorithm(), key.getFormat());
        return key;
    }
        
    public void handle(Socket socket) {
        this.socket = socket;
        try {
            process();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            reply(e);
        } finally {
        }
    }

    private void reply(Throwable throwable) {
        try {
            reply(new VCipherResponse(throwable));
        } catch (IOException e) {
            logger.warn(e);
        }
    }
    
    private void process() throws Exception {
        this.request = VSockets.read(socket, VCipherRequest.class);
        logger.info("received", request);
        if (request.requestType == VCipherRequestType.PING) {
            reply(new VCipherResponse(VCipherResponseType.PING));
        } else if (request.requestType == VCipherRequestType.ENCIPHER) {
            reply(encrypt());
        } else if (request.requestType == VCipherRequestType.DECIPHER) {
            reply(decrypt());
        }
    }
    
    protected void reply(VCipherResponse response) throws IOException {
        logger.info("reply", response);
        VSockets.write(socket, response);                
    }

    protected VCipherResponse decrypt() throws Exception {
        IvParameterSpec ips = new IvParameterSpec(request.iv);
        aesDecryptCipher.init(Cipher.DECRYPT_MODE, key, ips);
        byte[] decryptedBytes = aesDecryptCipher.doFinal(request.getBytes());
        return new VCipherResponse(VCipherResponseType.OK, decryptedBytes);
    }

    protected VCipherResponse encrypt() throws Exception {
        AlgorithmParameters params = aesEncryptCipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedBytes = aesEncryptCipher.doFinal(request.getBytes());
        return new VCipherResponse(VCipherResponseType.OK, encryptedBytes, iv);
    }
   
}
