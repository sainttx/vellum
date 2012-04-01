/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import venigma.common.JsonSockets;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class CipherHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CipherContext context;
    Key key;
    Cipher aesEncryptCipher;
    Cipher aesDecryptCipher;
    Socket socket;
    CipherRequest request;
    boolean running = true;
    
    public CipherHandler(CipherContext context) {
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
            reply(new CipherResponse(throwable));
        } catch (IOException e) {
            logger.warn(e);
        }
    }
    
    private void process() throws Exception {
        this.request = JsonSockets.read(socket, CipherRequest.class);
        logger.info("received", request);
        if (request.requestType == CipherRequestType.PING) {
            reply(new CipherResponse(CipherResponseType.PING));
        } else if (request.requestType == CipherRequestType.ENCIPHER) {
            reply(encrypt());
        } else if (request.requestType == CipherRequestType.DECIPHER) {
            reply(decrypt());
        }
    }
    
    protected void reply(CipherResponse response) throws IOException {
        logger.info("reply", response);
        JsonSockets.write(socket, response);                
    }

    protected CipherResponse decrypt() throws Exception {
        IvParameterSpec ips = new IvParameterSpec(request.iv);
        aesDecryptCipher.init(Cipher.DECRYPT_MODE, key, ips);
        byte[] decryptedBytes = aesDecryptCipher.doFinal(request.getBytes());
        return new CipherResponse(CipherResponseType.OK, decryptedBytes);
    }

    protected CipherResponse encrypt() throws Exception {
        AlgorithmParameters params = aesEncryptCipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedBytes = aesEncryptCipher.doFinal(request.getBytes());
        return new CipherResponse(CipherResponseType.OK, encryptedBytes, iv);
    }
   
}
