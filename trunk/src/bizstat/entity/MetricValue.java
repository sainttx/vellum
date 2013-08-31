/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package bizstat.entity;

import vellum.datatype.Timestamped;

/**
 *
 * @author evan.summers
 */
public class MetricValue implements Timestamped {
    float value;
    long timestamp;
    transient MetricInfo metricInfo;

    public MetricValue(MetricInfo metricInfo, float value, long timestamp) {
        this.metricInfo = metricInfo;
        this.timestamp = timestamp;
        this.value = value;
    }
    
    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
