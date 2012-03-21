/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author evan
 */
public class VCipherSocket {
    SSLSocket socket;

    public VCipherSocket(SSLSocket socket) {
        this.socket = socket;
    }
     
    public VCipherResponse sendRequest(VCipherRequest request) throws IOException {
        socket.getOutputStream().write(new Gson().toJson(request).getBytes(VProvider.CHARSET));
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        VCipherResponse response = new Gson().fromJson(reader, VCipherResponse.class);
        return response;
    }
    
}
