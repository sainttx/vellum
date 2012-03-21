/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;

/**
 *
 * @author evan
 */
public class VCipherSpi extends CipherSpi {
    VProvider provider = VProvider.instance; 
    VCipherConnection connection = provider.newConnection(); 
    
    public VCipherSpi() {
        super();
    }

    @Override
    protected void engineSetMode(String string) throws NoSuchAlgorithmException {
    }

    @Override
    protected void engineSetPadding(String string) throws NoSuchPaddingException {
    }

    @Override
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override
    protected int engineGetOutputSize(int i) {
        return 0;
    }

    @Override
    protected byte[] engineGetIV() {
        return null;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    @Override
    protected void engineInit(int i, Key key, SecureRandom sr) throws InvalidKeyException {
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameterSpec aps, SecureRandom sr) throws InvalidKeyException, InvalidAlgorithmParameterException {
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameters ap, SecureRandom sr) throws InvalidKeyException, InvalidAlgorithmParameterException {
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
        return null;
    }

    @Override
    protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
        return 0;
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int i, int i1) throws IllegalBlockSizeException, BadPaddingException {
        try {
            VCipherRequest request = new VCipherRequest(bytes);
            VCipherResponse response = connection.sendRequest(request);
            return response.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return 0;
    }
}
