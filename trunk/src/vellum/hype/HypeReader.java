/*
 * 
 */
package vellum.hype;

import vellum.util.Lists;
import vellum.hype.java.JavaMeta;
import vellum.util.StreamsX;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import vellum.hype.java.JavaTokenizer;

/**
 *
 * @author evans
 */
public class HypeReader {

    HypeContext context = new HypeContext();
    String line;
    BlockType blockType;
    StringBuilder builder = new StringBuilder();
    String previousToken;
    boolean publicLine; 
    
    public void read(InputStream inputStream, OutputStream outputStream) throws Exception {
        BufferedReader reader = StreamsX.newBufferedReader(inputStream);
        PrintWriter printer = StreamsX.newPrintWriter(outputStream);
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            processLine();
            printer.println(line);
        }
        printer.close();
    }

    private void processLine() throws Exception {
        if (blockType == BlockType.JAVA) {
            if (line.startsWith(JavaMeta.endPattern)) {
                blockType = null;
            } else {
                line = new HypeJavaLineProcessor(line).process();
            }
        } else if (line.startsWith(JavaMeta.beginPattern)) {
            blockType = BlockType.JAVA;
        }
    }
}

