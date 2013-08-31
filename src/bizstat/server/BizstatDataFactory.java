/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
