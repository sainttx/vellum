/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.data;

import java.util.Date;
import venigma.common.IdEntity;

/**
 *
 * @author evan
 */
public class LogEvent implements IdEntity {
    Long id;
    Date timestamp;
    String message;
    
    public LogEvent() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
        
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public Comparable getId() {
        return id;
    }
    
}
