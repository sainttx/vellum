/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author evan
 */
public class VSocket {
    Socket socket;

    public VSocket(Socket socket) {
        this.socket = socket;
    }
     
    public void write(Object message) throws IOException {
        socket.getOutputStream().write(new Gson().toJson(message).getBytes(VProviderContext.CHARSET));        
    }

    public <T> T read(Class messageClass) throws IOException {
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        Object response = new Gson().fromJson(reader, messageClass);
        return (T) response;
    }        
}
