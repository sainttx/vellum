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
public class VCipherSocket {
    Socket socket;

    public VCipherSocket(Socket socket) {
        this.socket = socket;
    }
     
    public VCipherResponse sendRequest(VCipherRequest request) throws IOException {
        socket.getOutputStream().write(new Gson().toJson(request).getBytes(VProvider.CHARSET));
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        VCipherResponse response = new Gson().fromJson(reader, VCipherResponse.class);
        return response;
    }
    
}
