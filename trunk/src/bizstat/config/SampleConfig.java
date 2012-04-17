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
    ContactGroup itron = new ContactGroup();
    Contact evans = new Contact("evan.summers@gmail.com", "0827745205");
    Host bizswitch = new Host("BizSwitch.net");
    Service qamps = new Service();
    Service fin = new Service();
    
    public SampleConfig() {
    }
}
