/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
 * 
 */
package bizstat.server;

import bizstat.entity.BizstatConfigStorage;

/**
 *
 * @author evan.summers
 */
public interface BizstatDataFactory {
    public void init(BizstatConfigStorage storage);
    
}
