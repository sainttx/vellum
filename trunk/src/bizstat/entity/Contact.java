/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import vellum.util.Args;
import bizstat.server.BizstatServer;
import vellum.entity.AbstractIdEntity;
import java.util.Date;
import vellum.config.PropertiesMap;
import vellum.entity.ConfigurableEntity;

/**
 *
 * @author evan
 */
public class Contact extends AbstractIdEntity implements ConfigurableEntity {
    String name; 
    String fullName;
    String email;
    String im;
    String sms;
    Date notifyTime;
    boolean enabled = true;
    boolean admin = false;
    transient ContactGroup contactGroup;
    
    public Contact() {
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }
    
    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Override
    public Comparable getId() {
        return name;
    }

    public ContactGroup getContactGroup() {
        return contactGroup;
    }

    @Override
    public void set(BizstatServer server, PropertiesMap properties) {
        fullName = properties.getString("fullName", null);
        sms = properties.getString("sms", null);
        email = properties.getString("email", null);
        im = properties.getString("gtalk", null);
        enabled = properties.getBoolean("enabled", false);
        admin = properties.getBoolean("admin", false);
        if (admin) {
            server.getAdminContacts().add(this);
        }
    }

    public PropertiesMap getPropertiesMap() {
        PropertiesMap map = new PropertiesMap();
        map.put("name", name);        
        map.put("fullName", fullName);
        map.put("sms", sms);
        map.put("email", email);
        return map;
    }
    
    @Override
    public String toString() {
        return Args.format(name, im, contactGroup);
    }

    
}
