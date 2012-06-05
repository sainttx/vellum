package vellumdemo.cryptonomicaldemo;

public class CryptonomicalDemo {
    public static CryptonomicalResources resources = new CryptonomicalResources();
    
    CryptonomicalServer server = new CryptonomicalServer();
    CryptonomicalClient client = new CryptonomicalClient();
    
    protected void test() throws Exception {
        server.bind(resources.serverPort);
        server.start();
        client.connect("localhost", resources.serverPort);
        client.start();
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new CryptonomicalDemo().test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
