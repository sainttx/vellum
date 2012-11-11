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
    long authMillis;

    public CrocCookie() {
    }

    public CrocCookie(Map map) {
        this(new StringMap(map));
    }
    
    public CrocCookie(StringMap map) {
        email = map.get(CrocCookieMeta.email.name());
        displayName = map.get(CrocCookieMeta.displayName.name());
        authMillis = map.getLong(CrocCookieMeta.authMillis.name(), 0);
    }
    
    public CrocCookie(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
        this.authMillis = System.currentTimeMillis();        
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
        map.put(CrocCookieMeta.authMillis.name(), Long.toString(authMillis));
        return map;
    }

    public long getAuthMillis() {
        return authMillis;
    }

    public boolean isAuth() {
        return false;
    }
    
    @Override
    public String toString() {
        return toMap().toString();
    }

}
