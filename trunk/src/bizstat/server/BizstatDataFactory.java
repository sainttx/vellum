/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
