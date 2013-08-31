/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
