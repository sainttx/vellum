/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 * 
 */
package vellum.io;

import venigma.provider.ClientContext;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan.summers
 */
public class JsonSockets {
    static Logr logger = LogrFactory.getThreadLogger(JsonSockets.class);
     
    public static void write(Socket socket, Object message) throws IOException {
        String json = new Gson().toJson(message);
        byte[] bytes = json.getBytes(ClientContext.CHARSET);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(json);
        writer.flush();
        logger.trace("write", message.getClass(), bytes.length, new String(bytes));
    }

    public static <T> T read(Socket socket, Class messageClass) throws IOException {        
        logger.trace("read message", messageClass, socket.getInputStream().available());
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String json = br.readLine();
        logger.trace("read json", json); 
        Object response = new Gson().fromJson(json, messageClass);
        logger.trace("read response", response.getClass(), response);
        return (T) response;
    }
}
