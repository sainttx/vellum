/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.IOException;
import java.security.AlgorithmParameters;
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
import venigma.server.storage.CipherStorage;
import venigma.common.KeyInfo;

/**
 *
 * @author evan
 */
public class CipherHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CipherContext context;
    CipherStorage storage;
    SSLSocket socket;
    CipherRequest request;
    CipherResponseType responseType;
    boolean running = true;
    String subject; 
    
    public CipherHandler(CipherContext context) {
        this.context = context;
        this.storage = context.getStorage();
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
            logger.warn(e);
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
        } else if (request.requestType == CipherRequestType.GENERATE_KEY) {
            reply(generateKey());
        } else if (request.requestType == CipherRequestType.GRANT) {
            reply(grant());
        } else if (request.requestType == CipherRequestType.REVOKE) {
            reply(revoke());
        } else if (request.requestType == CipherRequestType.ADD_USER) {
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
        if (!storage.getKeyInfoStorage().exists(request.getKeyAlias())) {
            return new CipherResponse(CipherResponseType.ERROR_KEY_NOT_FOUND);
        }
        KeyInfo keyInfo = storage.getKeyInfoStorage().get(request.getKeyAlias());
        Cipher cipher = context.getCipher(keyInfo, Cipher.DECRYPT_MODE, request.iv);
        byte[] decryptedBytes = cipher.doFinal(request.getBytes());
        return new CipherResponse(CipherResponseType.OK, decryptedBytes);
    }

    protected CipherResponse encrypt() throws Exception {
        if (!storage.getKeyInfoStorage().exists(request.getKeyAlias())) {
            return new CipherResponse(CipherResponseType.ERROR_KEY_NOT_FOUND);
        }
        KeyInfo keyInfo = storage.getKeyInfoStorage().get(request.getKeyAlias());
        Cipher cipher = context.getCipher(keyInfo, Cipher.ENCRYPT_MODE);
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
    
    protected CipherResponse generateKey() throws Exception {
        if (request.getKeySize() == 0) {
            return new CipherResponse(CipherResponseType.ERROR_NO_KEY_SIZE);            
        }
        if (request.getKeySize() != 128 && request.getKeySize() != 192 && request.getKeySize() != 256) {
            return new CipherResponse(CipherResponseType.ERROR_INVALID_KEY_SIZE);            
        }
        if (storage.getKeyInfoStorage().exists(request.getKeyAlias())) {
            return new CipherResponse(CipherResponseType.ERROR_KEY_ALREADY_EXISTS);
        }
        KeyInfo keyInfo = new KeyInfo(request.getKeyAlias(), request.getKeyRevision(), request.getKeySize());
        context.saveNewKey(keyInfo);
        return new CipherResponse(CipherResponseType.OK);
    }
    
    protected CipherResponse reviseKey() throws Exception {
        if (!storage.getKeyInfoStorage().exists(request.getKeyAlias())) {
            return new CipherResponse(CipherResponseType.ERROR_KEY_NOT_FOUND);
        }
        KeyInfo keyInfo = storage.getKeyInfoStorage().get(request.getKeyAlias());
        context.saveRevisedKey(keyInfo);
        return new CipherResponse(CipherResponseType.OK);
    }

}
