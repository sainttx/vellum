/*
 * Copyright Evan Summers
 * 
 */
package crocserver.httphandler.persona;

import crocserver.app.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.StringMap;
import vellum.util.Args;

/**
 *
 * @author evan
 */
public class PersonaUserInfo {
    static Logr logger = LogrFactory.getLogger(PersonaUserInfo.class);
    String json;
    String email;
    String issuer;
    long expires;
    boolean ok;
    
    public PersonaUserInfo() {
    }

    public boolean parseJson(String json) {
        this.json = json;
        StringMap map = JsonStrings.getStringMap(json);
        String status = map.get("status");
        if (status != null && status.equals("okay")) {
            email = map.get("email");
            expires = map.getLong("expires");
            issuer = map.get("issuer");
            ok = true;
        } else {
            ok = false;
        }
        return ok;
    }

    public boolean isOk() {
        return ok;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }
    
    @Override
    public String toString() {
        return Args.format(email, issuer, expires);
    }

    
}
