/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.server.BizstatServer;
import vellum.config.PropertiesMap;

/**
 *
 * @author evan
 */
public class ContactConfigurator {
    
    public void config(Contact contact, BizstatServer server, PropertiesMap properties) {
        contact.fullName = properties.getString("fullName", null);
        contact.sms = properties.getString("sms", null);
        contact.email = properties.getString("email", null);
        contact.im = properties.getString("gtalk", null);
        contact.enabled = properties.getBoolean("enabled", false);
        contact.admin = properties.getBoolean("admin", false);
        if (contact.admin) {
            server.getAdminContacts().add(contact);
        }
    }
    
}
