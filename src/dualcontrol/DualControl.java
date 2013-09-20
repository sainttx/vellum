/*
 * Source https://code.google.com/p/vellum by @evanxsummers

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.  
 */
package dualcontrol;

import java.util.Map;
import javax.net.ssl.SSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class DualControl {

    private final static Logger logger = LoggerFactory.getLogger(DualControl.class);
    private char[] dualPass;
    private String dualAlias;
    
    public void call(String purpose) throws Exception {
        DualControlManager manager = new DualControlManager(System.getProperties(), 2, purpose);
        SSLContext sslContext = DualControlSSLContextFactory.createSSLContext(
                System.getProperties(), new ConsoleAdapter(System.console()));
        manager.init(sslContext);
        manager.call();
        Map.Entry<String, char[]> entry = manager.getDualMap().entrySet().iterator().next();
        dualAlias = entry.getKey();
        dualPass = entry.getValue();
        logger.info("dualAlias: " + dualAlias);
    }

    public String getDualAlias() {
        return dualAlias;
    }

    public char[] getDualPass() {
        return dualPass;
    }
}
