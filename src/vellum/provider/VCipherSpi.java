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
    VProviderConnection connection = new VProviderConnection();
    
    public VCipherSpi() {
        super();
    }

    @Override
    protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
    }

    @Override
    protected void engineSetPadding(String padding) throws NoSuchPaddingException {
    }

    @Override
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override
    protected int engineGetOutputSize(int inputLen) {
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
    protected void engineInit(int opmode, Key key, SecureRandom sr) throws InvalidKeyException {
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameterSpec aps, SecureRandom sr) throws InvalidKeyException, InvalidAlgorithmParameterException {
    }

    @Override
    protected void engineInit(int opmode, Key key, AlgorithmParameters ap, SecureRandom sr) throws InvalidKeyException, InvalidAlgorithmParameterException {
    }

    @Override
    protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
        return null;
    }

    @Override
    protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        return 0;
    }

    @Override
    protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        try {
            VCipherRequest request = new VCipherRequest(VCipherRequestType.ENCIPHER, input);
            VCipherResponse response = connection.sendCipherRequest(request);
            if (response.responseType != VCipherResponseType.OK) {
                throw new VCipherResponseRuntimeException(response);
            }
            return response.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return 0;
    }
}
