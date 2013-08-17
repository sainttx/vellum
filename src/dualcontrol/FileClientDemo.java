
package dualcontrol;

import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class FileClientDemo {
    private static Logger logger = Logger.getLogger(FileClientDemo.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: hostAddress port");
        } else {
            new FileClientDemo().run(args[0], Integer.parseInt(args[1]));
        }
    }
    
    private void run(String hostAddress, int port) throws Exception {
        logger.debug(String.format("FileClient %s:%d", hostAddress, port));
        byte[] bytes = FileClient.read(hostAddress, port);
        logger.debug(String.format("FileClient read %d bytes", bytes.length));
    }
    
}
