/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VSockets {
    static Logr logger = LogrFactory.getThreadLogger(VSockets.class);
     
    public static void write(Socket socket, Object message) throws IOException {
        String json = new Gson().toJson(message);
        byte[] bytes = json.getBytes(VProviderContext.CHARSET);
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
