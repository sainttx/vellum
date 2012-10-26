/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.enumtype.NotifyType;
import bizstat.enumtype.StatusChangeType;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Args;
import bizstat.server.BizstatServer;
import java.util.*;
import vellum.config.PropertiesMap;
import vellum.entity.AbstractIdEntity;
import vellum.entity.ConfigurableEntity;
import vellum.type.UniqueList;

/**
 *
 * @author evan
 */
public class Service extends AbstractIdEntity implements ConfigurableEntity<BizstatServer> {
    Logr logger = LogrFactory.getLogger(Service.class);
    
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
    transient Map<StatusChangeType, Integer> repeatCountMap = new HashMap();
    transient Map<StatusChangeType, Long> notifyIntervalMap = new HashMap();
    transient Map<String, MetricInfo> metrics = new HashMap();
    transient Host host;
    transient List<ContactGroup> contactGroupList = new UniqueList();
    
    public Service() {
    }

    public Service(String name) {
        this.name = name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
   
    public String getName() {
        return name;
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

    public Map<StatusChangeType, Integer> getRepeatCountMap() {
        return repeatCountMap;
    }
    
    public long getIntervalMillis() {
        return intervalMillis;
    }
    
    @Override
    public Comparable getId() {
        return name;
    }
        
    public List<ContactGroup> getContactGroupList() {
        return contactGroupList;
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

    public Map<StatusChangeType, Long> getNotifyIntervalMap() {
        return notifyIntervalMap;
    }
        
    @Override
    public void config(BizstatServer server, PropertiesMap properties) {
        new ServiceConfigurator(server, properties, this).configure();
    }
    
    @Override
    public String toString() {
        return Args.format(name);
    }
    
}
