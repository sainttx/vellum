/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Args;

/**
 *
 * @author evan
 */
public class GoogleUserInfo {
    static Logr logger = LogrFactory.getLogger(GoogleUserInfo.class);
    
    String email;
    String displayName;

    public GoogleUserInfo() {
    }
    
    public GoogleUserInfo(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }
   
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return Args.format(email, displayName);
    }

    
}
