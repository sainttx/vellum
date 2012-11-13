/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import vellum.entity.StringIdEntity;

/**
 *
 * @author evan
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
