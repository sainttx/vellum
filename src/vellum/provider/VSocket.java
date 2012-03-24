/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
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
        byte[] bytes = new Gson().toJson(message).getBytes(VProviderContext.CHARSET);
        socket.getOutputStream().write(bytes);
        logger.trace("write", message.getClass(), bytes.length);
    }

    public <T> T read(Class messageClass) throws IOException {
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        Object response = new Gson().fromJson(reader, messageClass);
        logger.trace("read", messageClass);
        return (T) response;
    }        
}
