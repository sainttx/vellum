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
public class GooglePlusUserInfo {
    static Logr logger = LogrFactory.getLogger(GooglePlusUserInfo.class);
    String json;
    String email;
    String displayName;
    String givenName;
    String familyName;
    
    public GooglePlusUserInfo() {
    }

    public GooglePlusUserInfo(String json) {
        this.json = json;
    }
    
    public GooglePlusUserInfo(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    public void parseJson(String json) {
        this.json = json;
        logger.info(json);
        setEmail(JsonStrings.get(json, "email"));
        setDisplayName(JsonStrings.get(json, "name"));
        setGivenName(JsonStrings.get(json, "given_name"));
        setFamilyName(JsonStrings.get(json, "family_name"));
        
    }
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGivenName() {
        return givenName;
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

    public void setJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    
    @Override
    public String toString() {
        return Args.format(email, displayName);
    }

    
}
