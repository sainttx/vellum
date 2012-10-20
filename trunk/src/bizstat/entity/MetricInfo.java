/*
 * Copyright Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.enumtype.StatusChangeType;
import bizstat.enumtype.MetricType;
import bizstat.server.BizstatServer;
import java.util.HashMap;
import java.util.Map;
import vellum.config.PropertiesMap;
import vellum.entity.AbstractIdEntity;
import vellum.entity.ConfigurableEntity;

/**
 *
 * @author evan
 */
public class MetricInfo extends AbstractIdEntity implements ConfigurableEntity {
    String name;
    String label;
    String description;
    boolean enabled = true;
    MetricType metricType;
    transient Service service;
    transient Map<StatusChangeType, Float> valueMap = new HashMap();

    public MetricInfo() {
    }
        
    public MetricInfo(Service service, String name) {
        this.service = service;
        this.name = name;
    }
    
    @Override
    public Comparable getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }
        
    @Override
    public void setName(String name) {
        this.name = name;
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
    
    public void setValueMap(Map<StatusChangeType, Float> valueMap) {
        this.valueMap = valueMap;
    }

    public Map<StatusChangeType, Float> getValueMap() {
        return valueMap;
    }
            
    @Override
    public void set(BizstatServer server, PropertiesMap properties) {
        String serviceName = properties.getString("service", name);
        service = server.getConfigStorage().find(Service.class, serviceName);
        valueMap = StatusChangeType.newValueMap(properties.splitCsv("values"));
    }
}
