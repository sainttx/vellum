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
    String redirectUrl; 
    
    public GoogleApi() {
    }
    
    public void init(String redirectUrl) throws UnsupportedEncodingException {        
        this.redirectUrl = redirectUrl;
        StringBuilder builder = new StringBuilder();
        builder.append("https://accounts.google.com/o/oauth2/auth");
        builder.append("?state=none");
        builder.append("&response_type=token");
        builder.append("&approval_prompt=force");
        builder.append("&client_id=").append(clientId);
        builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUrl, "UTF-8"));
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
        builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUrl, "UTF-8"));
        builder.append("&client_secret=").append(clientSecret);
        builder.append("&code=").append(URLEncoder.encode(code, "UTF-8"));
        logger.info("request", url, builder.toString());
            connection.getOutputStream().write(builder.toString().getBytes());
        String responseText = Streams.readString(connection.getInputStream());
        String accessToken = JsonStrings.get(responseText, "access_token");
        logger.info("response", responseText);
        logger.info("accessToken", accessToken);
        return getUserInfo(accessToken);
    }

    public GoogleUserInfo getUserInfo(String accessToken) throws Exception {
        String json = getUserInfoJson(accessToken);
        GoogleUserInfo userInfo = new GoogleUserInfo();
        userInfo.parseJson(json);
        return userInfo;
    }

    public String getUserInfoJson(String accessToken) throws Exception {
        URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken);
        logger.info("request", url.toString());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        String res = Streams.readString(connection.getInputStream());
        logger.info("response", connection.getResponseCode(), res);
        return res;
    }

    public GooglePlusUserInfo getPlusPerson(String accessToken, String userId) throws Exception {
        String json = getPlusPersonJson(accessToken, userId);
        GooglePlusUserInfo userInfo = new GooglePlusUserInfo();
        userInfo.parseJson(json);
        return userInfo;
    }
    
    public String getPlusPersonJson(String accessToken, String userId) throws Exception {
        URL url = new URL("https://www.googleapis.com/plus/v1/people/" + userId + "?access_token=" + accessToken);
        logger.info("request", url.toString());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        return Streams.readString(connection.getInputStream());
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
        
    public String getLoginUrl() {
        return loginUrl;
    }
   
    @Override
    public String toString() {
        return Args.format(clientId);
    }   
}
