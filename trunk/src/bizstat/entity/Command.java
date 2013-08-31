/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
