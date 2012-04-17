/*
 * Copyright Evan Summers
 * 
 */
package bizstat.app;

import bizstat.config.SampleConfig;
import common.entity.EntityManager;

/**
 *
 * @author evan
 */
public class BizstatApp {
    public static BizstatApp app = new BizstatApp();
    
    EntityManager em = new EntityManager();    

    public BizstatApp() {
    }
    
    public void init() {
        SampleConfig sampleConfig = new SampleConfig();
    }
    
    
}
