/*
 * Copyright Evan Summers
 * 
 */
package bizstat.entity;

import common.entity.StringIdEntity;

/**
 *
 * @author evan
 */
public class Contact extends StringIdEntity {
    String name;
    String label;
    String email;
    String sms;

    public Contact(String email, String sms) {
        this.email = email;
        this.sms = sms;
    }
    
}
