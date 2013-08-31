/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
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
