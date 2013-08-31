/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
 * 
 */
package crocserver.app;

import java.util.Map;
import vellum.datatype.Millis;
import vellum.exception.EnumException;
import vellum.parameter.StringMap;

/**
 *
 * @author evan.summers
 */
public class CrocCookie {
    public static final long MAX_AGE_MILLIS = Millis.fromHours(16);
    
    String email;
    String displayName;
    String accessToken;
    long loginMillis;
    String authCode; 
            
    public CrocCookie() {
    }

    public CrocCookie(Map map) {
        this(new StringMap(map));
    }
    
    public CrocCookie(StringMap map) {
        email = map.get(CrocCookieMeta.email.name());
        displayName = map.get(CrocCookieMeta.displayName.name());
        accessToken = map.get(CrocCookieMeta.accessToken.name());
        loginMillis = map.getLong(CrocCookieMeta.loginMillis.name(), 0);
        authCode = map.get(CrocCookieMeta.authCode.name());
    }
    
    public CrocCookie(String email, String displayName, long loginMillis, String accessToken) {
        this.email = email;
        this.displayName = displayName;
        this.loginMillis = loginMillis;
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public StringMap toMap() {
        StringMap map = new StringMap();
        map.put(CrocCookieMeta.email.name(), email);
        map.put(CrocCookieMeta.displayName.name(), displayName);
        map.put(CrocCookieMeta.loginMillis.name(), Long.toString(loginMillis));
        map.put(CrocCookieMeta.authCode.name(), authCode);
        map.put(CrocCookieMeta.accessToken.name(), accessToken);
        return map;
    }

    public void createAuthCode(byte[] secret) throws Exception {
        authCode = CrocSecurity.createCode(secret, email, loginMillis);
    }

    public String getAuthCode() {
        return authCode;
    }
    
    public void validateAuthCode(byte[] secret) throws Exception {
        String code = CrocSecurity.createCode(secret, email, loginMillis);
        if (!authCode.equals(code)) {
            throw new EnumException(CrocExceptionType.INVALID_COOKIE);
        }
    }
    
    public long getLoginMillis() {
        return loginMillis;
    }

    public String getAccessToken() {
        return accessToken;
    }
    
    @Override
    public String toString() {
        return toMap().toString();
    }
}
