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
    String loginUrl;
    String redirectUri; 
    
    public GoogleApi() {
    }
    
    public void init(String redirectUri) throws UnsupportedEncodingException {        
        this.redirectUri = redirectUri;
        StringBuilder builder = new StringBuilder();
        builder.append("https://accounts.google.com/o/oauth2/auth");
        builder.append("?state=none");
        builder.append("&response_type=token");
        builder.append("&approval_prompt=force");
        builder.append("&client_id=").append(clientId);
        builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
        builder.append("&scope=").append(URLEncoder.encode("https://www.googleapis.com/auth/userinfo.email", "UTF-8"));
        builder.append("+").append(URLEncoder.encode("https://www.googleapis.com/auth/userinfo.profile", "UTF-8"));
        loginUrl = builder.toString();
    }

    public GoogleUserInfo sendTokenRequest(String code) throws Exception {
        URL url = new URL("https://accounts.google.com/o/oauth2/token");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(DefaultKeyStores.createSSLSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        StringBuilder builder = new StringBuilder();
        builder.append("grant_type=authorization_code");
        builder.append("&client_id=").append(clientId);
        builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
        builder.append("&client_secret=").append(clientSecret);
        builder.append("&code=").append(URLEncoder.encode(code, "UTF-8"));
        logger.info("request", url, builder.toString());
            connection.getOutputStream().write(builder.toString().getBytes());
        String responseText = Streams.readString(connection.getInputStream());
        String accessToken = JsonStrings.get(responseText, "access_token");
        logger.info("response", responseText);
        logger.info("accessToken", accessToken);
        return sendUserRequest(accessToken);
    }
    
    public GoogleUserInfo sendUserRequest(String accessToken) throws Exception {
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken);
        logger.info("request", url.toString());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        String responseText = Streams.readString(connection.getInputStream());
        GoogleUserInfo userInfo = new GoogleUserInfo();
        userInfo.setEmail(JsonStrings.get(responseText, "email"));
        userInfo.setDisplayName(JsonStrings.get(responseText, "name"));
        userInfo.setGivenName(JsonStrings.get(responseText, "given_name"));
        userInfo.setFamilyName(JsonStrings.get(responseText, "family_name"));
        logger.info("response", userInfo, responseText);        
        return userInfo;
    }
    
    public void sendPlusRequest(String userId) throws Exception {
        URL url = new URL("https://www.googleapis.com/plus/v1/people/" + userId);
        logger.info("request", url.toString());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        String responseText = Streams.readString(connection.getInputStream());
        logger.info("response", responseText);        
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
        
    public String getLoginUrl() {
        return loginUrl;
    }
   
    @Override
    public String toString() {
        return Args.format(clientId);
    }   
}
