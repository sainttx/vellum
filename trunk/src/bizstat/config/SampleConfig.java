/*
 * Copyright Evan Summers
 * 
 */
package bizstat.config;

import bizstat.entity.Contact;
import bizstat.entity.ContactGroup;
import bizstat.entity.Host;
import bizstat.entity.Service;

/**
 *
 * @author evan
 */
public class SampleConfig {
    ContactGroup ipay = new ContactGroup();
    ContactGroup itronFin = new ContactGroup();
    Contact evans = new Contact();
    Contact hentyw = new Contact();
    Contact brandonh = new Contact();
    Host biz1 = new Host();
    Host biz3 = new Host();
    Service qamps = new Service();
    Service fin = new Service();
    
    public SampleConfig() {
    }
}
