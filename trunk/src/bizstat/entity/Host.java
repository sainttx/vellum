/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Args;
import bizstat.server.BizstatServer;
import vellum.type.UniqueList;
import java.util.List;
import vellum.config.PropertiesMap;
import vellum.entity.AbstractIdEntity;
import vellum.entity.ConfigurableEntity;

/**
 *
 * @author evan
 */
public class Host extends AbstractIdEntity implements ConfigurableEntity {

    static Logr logger = LogrFactory.getLogger(Network.class);
    
    String name;
    String fullName;
    String ipNumber;
    boolean enabled;
    
    transient Network network;    
    transient List<Service> serviceList = new UniqueList();
    transient List<ContactGroup> contactGroupList = new UniqueList();

    public Host() {
    }

    public Host(String name) {
        this.name = name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public String getIpNumber() {
        return ipNumber;
    }

    public void setIpNumber(String ipNumber) {
        this.ipNumber = ipNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public Network getHostGroup() {
        return network;
    }

    @Override
    public Comparable getId() {
        return name;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public Network getNetwork() {
        return network;
    }
    
    public List<ContactGroup> getContactGroupList() {
        return contactGroupList;
    }
    
    @Override
    public void set(BizstatServer server, PropertiesMap properties) {
        ipNumber = properties.getString("ipNumber", null);
        enabled = properties.getBoolean("enabled", true);
        network = server.getConfigStorage().find(Network.class, properties.getString("network"));
        network.getHostList().add(this);
        for (String serviceName : properties.splitCsv("services")) {
            Service service = server.getConfigStorage().find(Service.class, serviceName);
            logger.info("services add", name, serviceName);
            serviceList.add(service);
        }
       for (String contactGroupName : properties.splitCsv("contactGroups")) {
            contactGroupList.add(server.getConfigStorage().find(ContactGroup.class, contactGroupName));
        }
     }
    
    @Override
    public String toString() {
        return Args.format(name);
    }
}
