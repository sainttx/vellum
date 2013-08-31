/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package bizstat.entity;

import vellum.entity.StringIdEntity;

/**
 *
 * @author evan.summers
 */
public class Command extends StringIdEntity {
    String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    
    
}
