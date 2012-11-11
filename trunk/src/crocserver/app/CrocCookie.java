/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import java.util.Map;
import vellum.parameter.StringMap;

/**
 *
 * @author evan
 */
public class CrocCookie {
    public static final long MAX_AGE = 60*60*24;
    
    String email;
    String displayName;
    long loginMillis;

    public CrocCookie() {
    }

    public CrocCookie(Map map) {
        this(new StringMap(map));
    }
    
    public CrocCookie(StringMap map) {
        email = map.get(CrocCookieMeta.email.name());
        displayName = map.get(CrocCookieMeta.displayName.name());
        loginMillis = map.getLong(CrocCookieMeta.loginMillis.name(), 0);
    }
    
    public CrocCookie(String email, String displayName, long loginMillis) {
        this.email = email;
        this.displayName = displayName;
        this.loginMillis = loginMillis;
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
        return map;
    }

    public long getLoginMillis() {
        return loginMillis;
    }

    @Override
    public String toString() {
        return toMap().toString();
    }

}
