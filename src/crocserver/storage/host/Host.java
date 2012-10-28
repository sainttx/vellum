/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.host;

import bizstat.entity.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Args;
import crocserver.storage.org.Org;
import crocserver.storage.service.Service;
import vellum.type.UniqueList;
import java.util.List;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class Host extends AbstractIdEntity {

    static Logr logger = LogrFactory.getLogger(Host.class);
    
    String name;
    String fullName;
    String ipNumber;
    boolean enabled;
    long orgId;
    Org org;
    
    transient Network network;    
    transient List<Service> serviceList = new UniqueList();
    transient List<ContactGroup> contactGroupList = new UniqueList();

    public Host() {
    }

    public Host(String name) {
        this.name = name;
    }

    @Override
    public Comparable getId() {
        return name;
    }
    
    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
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
    public String toString() {
        return Args.format(name);
    }
}
