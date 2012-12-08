package vellumdemo.cryptonomicaldemo;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import vellum.crypto.Base64;

public class PBECipher {
    private static final String pbeAlgorithm = "PBEWithMD5AndDES";
    private static final String defaultPassword = "Ssh ssh!";
    private static byte[] salt = {
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03,
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32
    };
    private static final int iterationCount = 5;
    
    SecretKey secretKey;
    PBEParameterSpec parameterSpec;
    Cipher encryptCipher;
    Cipher decryptCipher;

    public PBECipher() {
        this(defaultPassword);
    }
    
    public PBECipher(String password) {
        try {
            parameterSpec = new PBEParameterSpec(salt, iterationCount);
            secretKey = createSecretKey(password);
            encryptCipher = createEncryptCipher();
            decryptCipher = createDecryptCipher();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private SecretKey createSecretKey(String secretKey) throws Exception {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(pbeAlgorithm);
        return keyFactory.generateSecret(keySpec);
    }
    
    private Cipher createEncryptCipher() throws Exception {
        Cipher encryptCipher = Cipher.getInstance(pbeAlgorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        return encryptCipher;
    }
    
    private Cipher createDecryptCipher() throws Exception {
        Cipher decryptCipher = Cipher.getInstance(pbeAlgorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        return decryptCipher;
    }
    
    public String encrypt(String string) {
        try {
            return Base64.encode(encryptCipher.doFinal(string.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public String decrypt(String string) {
        try {
            return new String(decryptCipher.doFinal(Base64.decode(string)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
}
