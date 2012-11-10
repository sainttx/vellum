/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package crocserver.storage.service;

import bizstat.entity.*;
import bizstat.enumtype.NotifyType;
import bizstat.enumtype.StatusChangeType;
import vellum.util.Args;
import crocserver.storage.org.Org;
import java.util.*;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class ServiceType extends AbstractIdEntity {
    
    long orgId;
    Org org;
    String name;
    String label;
    String description;
    boolean enabled = true;
    Date scheduleTime;
    long intervalMillis;
    long notifyMillis;
    long escalateMillis;
    long notifyRecoveryMillis;
    NotifyType notifyType;
    String hostName;
    Host host;
    
    transient Map<StatusChangeType, Integer> repeatCountMap = new HashMap();
    transient Map<StatusChangeType, Long> notifyIntervalMap = new HashMap();
    
    public ServiceType() {
    }

    public ServiceType(String name) {
        this.name = name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
        
    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
   
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }
        
    public NotifyType getNotifyType() {
        return notifyType;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public long getNotifyMillis() {
        return notifyMillis;
    }

    public void setNotifyMillis(long notifyMillis) {
        this.notifyMillis = notifyMillis;
    }

    @Override
    public String toString() {
        return Args.format(name);
    }    
}
