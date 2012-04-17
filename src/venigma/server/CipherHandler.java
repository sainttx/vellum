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
import javax.net.ssl.SSLSocket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.JsonSockets;

/**
 *
 * @author evan
 */
public class CipherHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CipherContext context;
    Key key;
    Socket socket;
    CipherRequest request;
    boolean running = true;
    
    public CipherHandler(CipherContext context) {
        this.context = context;
    }
    
    public void init() throws Exception {
    }
    
    public void handle(SSLSocket socket) {
        this.socket = socket;
        try {
            //logger.info("handle", socket.getSession().getPeerCertificateChain().toString());
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
        } else if (request.requestType == CipherRequestType.START) {
            reply(start());
        } else if (request.requestType == CipherRequestType.STOP) {
            reply(stop());
        } else if (request.requestType == CipherRequestType.CHECK) {
            reply(check());
        } else if (request.requestType == CipherRequestType.GRANT) {
            reply(grant());
        } else if (request.requestType == CipherRequestType.REVOKE) {
            reply(revoke());
        } else if (request.requestType == CipherRequestType.ENCIPHER) {
            if (!context.isStarted()) {
                reply(new CipherResponse(CipherResponseType.ERROR_NOT_STARTED));
            } else {
                reply(encrypt());
            }
        } else if (request.requestType == CipherRequestType.DECIPHER) {
            if (!context.isStarted()) {
                reply(new CipherResponse(CipherResponseType.ERROR_NOT_STARTED));
            } else {
                reply(decrypt());
            }
        }
    }
    
    protected void reply(CipherResponse response) throws IOException {
        logger.info("reply", response);
        JsonSockets.write(socket, response);                
    }

    protected CipherResponse decrypt() throws Exception {
        Cipher cipher = context.getCipher(Cipher.DECRYPT_MODE, request.iv);
        byte[] decryptedBytes = cipher.doFinal(request.getBytes());
        return new CipherResponse(CipherResponseType.OK, decryptedBytes);
    }

    protected CipherResponse encrypt() throws Exception {
        Cipher cipher = context.getCipher(Cipher.ENCRYPT_MODE);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedBytes = cipher.doFinal(request.getBytes());
        return new CipherResponse(CipherResponseType.OK, encryptedBytes, iv);
    }
    
    protected CipherResponse start() throws Exception {
        context.setStarted(true);
        return new CipherResponse(CipherResponseType.OK);
    }

    protected CipherResponse check() throws Exception {
        return new CipherResponse(CipherResponseType.OK);
    }
    
    protected CipherResponse stop() throws Exception {
        return new CipherResponse(CipherResponseType.OK);
    }
    
    protected CipherResponse grant() throws Exception {
        return new CipherResponse(CipherResponseType.OK);
    }

    protected CipherResponse revoke() throws Exception {
        return new CipherResponse(CipherResponseType.OK);
    }
    
}
