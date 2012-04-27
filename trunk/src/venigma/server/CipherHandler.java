/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.AdminUser;
import venigma.common.JsonSockets;

/**
 *
 * @author evan
 */
public class CipherHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CipherContext context;
    Key key;
    SSLSocket socket;
    CipherRequest request;
    CipherResponseType responseType;
    boolean running = true;
    String subject; 
    
    public CipherHandler(CipherContext context) {
        this.context = context;
    }
    
    public void init() throws Exception {
    }
    
    public void handle(SSLSocket socket) {
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

    private String getSubject() throws SSLPeerUnverifiedException {
        for (Certificate cert : socket.getSession().getPeerCertificates()) {
            if (cert instanceof X509Certificate) {
                X509Certificate x509Cert = (X509Certificate) cert;
                return x509Cert.getSubjectDN().getName();
            }
        }
        throw new RuntimeException();
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
        subject = getSubject();
        responseType = context.requestAuth.auth(request, subject);
        if (responseType != CipherResponseType.OK) {
            reply(new CipherResponse(responseType));            
            return;
        }
        if (request.requestType == CipherRequestType.PING) {
            reply(new CipherResponse(CipherResponseType.PING));
        } else if (request.requestType == CipherRequestType.START) {
            reply(start());
        } else if (request.requestType == CipherRequestType.STOP) {
            reply(stop());
        } else if (request.requestType == CipherRequestType.CHECK) {
            reply(check());
        } else if (request.requestType == CipherRequestType.GENKEY) {
            reply(genKey());
        } else if (request.requestType == CipherRequestType.GRANT) {
            reply(grant());
        } else if (request.requestType == CipherRequestType.REVOKE) {
            reply(revoke());
        } else if (request.requestType == CipherRequestType.ADDUSER) {
            reply(addUser());
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
        AdminUser user = context.storage.getAdminUserStorage().get(request.getUsername());
        if (user == null) {
            return new CipherResponse(CipherResponseType.ERROR_USER_NOT_FOUND);
        }
        if (user.isEnabled()) { 
            return new CipherResponse(CipherResponseType.ERROR_USER_ALREADY_GRANTED);
        }
        user.setEnabled(true);
        context.storage.getAdminUserStorage().update(user);
        return new CipherResponse(CipherResponseType.OK);
    }

    protected CipherResponse revoke() throws Exception {
        AdminUser user = context.storage.getAdminUserStorage().get(request.getUsername());
        if (user == null) {
            return new CipherResponse(CipherResponseType.ERROR_USER_NOT_FOUND);
        }
        if (!user.isEnabled()) { 
            return new CipherResponse(CipherResponseType.ERROR_USER_ALREADY_REVOKED);
        }
        user.setEnabled(false);
        context.storage.getAdminUserStorage().update(user);
        return new CipherResponse(CipherResponseType.OK);
    }

    protected CipherResponse addUser() throws Exception {
        AdminUser user = request.getUser();
        if (context.storage.getAdminUserStorage().exists(user.getUsername())) {
            return new CipherResponse(CipherResponseType.ERROR_USER_ALREADY_EXISTS);
        }        
        context.storage.getAdminUserStorage().add(user);        
        return new CipherResponse(CipherResponseType.OK);
    }
    
    protected CipherResponse genKey() throws Exception {
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        if (request.getKeySize() == 0) {
            return new CipherResponse(CipherResponseType.ERROR_NO_KEY_SIZE);            
        }
        if (request.getKeySize() < 128 && request.getKeySize() != 192 && request.getKeySize() != 256) {
            return new CipherResponse(CipherResponseType.ERROR_INVALID_KEY_SIZE);            
        }
        aes.init(request.getKeySize(), context.sr);
        SecretKey key = aes.generateKey();
        context.save(key, request.getKeyAlias(), request.getKeyRevision());
        return new CipherResponse(CipherResponseType.OK);
    }
}
