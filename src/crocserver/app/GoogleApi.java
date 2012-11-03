/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.security.DefaultKeyStores;
import vellum.util.Args;
import vellum.util.Streams;

/**
 *
 * @author evan
 */
public class GoogleApi {
    Logr logger = LogrFactory.getLogger(getClass());
    String clientId = System.getProperty("google.clientId");
    String clientSecret = System.getProperty("google.clientSecret");
    String loginUrl = "https://accounts.google.com/o/oauth2/auth";    
    String oauthUrl; 
    String code; 
    String accessToken;
    
    public GoogleApi() {
    }
    
    public void initLoginUrl(String oauthUrl) throws UnsupportedEncodingException {        
        this.oauthUrl = oauthUrl;
        StringBuilder builder = new StringBuilder();
        builder.append(loginUrl);
        builder.append("?state=none");
        builder.append("&response_type=code");
        builder.append("&client_id=").append(clientId);
        builder.append("&redirect_uri=").append(URLEncoder.encode(oauthUrl, "UTF-8"));
        builder.append("&scope=").append(URLEncoder.encode("https://www.googleapis.com/auth/userinfo.email", "UTF-8"));
        builder.append("+").append(URLEncoder.encode("https://www.googleapis.com/auth/userinfo.profile", "UTF-8"));
        loginUrl = builder.toString();
    }

    public void sendTokenRequest() throws Exception {
        URL url = new URL("https://accounts.google.com/o/oauth2/token");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(DefaultKeyStores.createSSLSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        StringBuilder builder = new StringBuilder();
        builder.append("grant_type=authorization_code");
        builder.append("&client_id=").append(clientId);
        builder.append("&redirect_uri=").append(URLEncoder.encode(oauthUrl, "UTF-8"));
        builder.append("&client_secret=").append(clientSecret);
        builder.append("&code=").append(URLEncoder.encode(code, "UTF-8"));
        logger.info("request", url, builder.toString());
            connection.getOutputStream().write(builder.toString().getBytes());
        String responseText = Streams.readString(connection.getInputStream());
        accessToken = JsonStrings.get(responseText, "access_token");
        logger.info("response", responseText);
        logger.info("accessToken", accessToken);
        sendUserRequest();
    }
    
    public void sendUserRequest() throws Exception {
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + URLEncoder.encode(accessToken, "UTF-8"));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        String responseText = Streams.readString(connection.getInputStream());
        logger.info("response", responseText);        
    }
    
    public String getLoginUrl() {
        return loginUrl;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
   
    @Override
    public String toString() {
        return Args.format(clientId);
    }   
}
