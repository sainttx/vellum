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
public class VSocket {
    static Logr logger = LogrFactory.getLogger(VSocket.class);
    Socket socket;

    public VSocket(Socket socket) {
        this.socket = socket;
    }
     
    public void write(Object message) throws IOException {
        String json = new Gson().toJson(message);
        byte[] bytes = json.getBytes(VProviderContext.CHARSET);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(json);
        writer.flush();
        logger.trace("write", message.getClass(), bytes.length, new String(bytes));
    }

    public <T> T read(Class messageClass) throws IOException {        
        logger.trace("read", messageClass, socket.getInputStream().available());
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String json = br.readLine();
        Object response = new Gson().fromJson(json, messageClass);
        logger.trace("read", response.getClass());
        return (T) response;
    }        
}
