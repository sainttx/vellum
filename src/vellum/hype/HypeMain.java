/*
 * 
 */
package vellum.hype;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author evans
 */
public class HypeMain {

    HypeContext context = new HypeContext();
    HypeReader reader = new HypeReader();

    public void start(InputStream inputStream, OutputStream outputStream) throws Exception {
        context.init();
        reader.read(inputStream, outputStream);
    }

    public void start(String[] args) throws Exception {
        InputStream in = System.in;
        OutputStream out = System.out;
        if (args.length > 0) {
            in = new FileInputStream(args[0]);
        }
        start(in, out);
    }

    public static void main(String[] args) throws Exception {
    }
}
