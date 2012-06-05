package vellumdemo.cryptonomicaldemo;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

public class CryptonomicalResources {
    String algorithm = "RSA";
    String algorithmModePadding = "RSA/ECB/PKCS1Padding";
    int keySize = 1024;
    int blockCapacity = 117;
    int serverPort = 80;
    
}

