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

import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public abstract class DummyDualControlConsole {
    final static Logger logger = Logger.getLogger(DummyDualControlConsole.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            logger.error("usage: alias password");
        } else {
            MockConsole console = new MockConsole(args[0], args[1].toCharArray());
            DualControlConsole instance = new DualControlConsole(
                    System.getProperties(), console);
            instance.init();
            instance.call();
            logger.info(console.getLines().get(0));
        }
    }    
}
