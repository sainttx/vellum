/*
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
package vellum.util;

import java.net.ServerSocket;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan.summers
 */
public class Sockets {
    public static Logr logger = LogrFactory.getLogger(Sockets.class);
    
    public static boolean portAvailable(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return true;
        } catch (Exception e) {
            logger.warn("portAvailable", e.getMessage());
            return false;
        }
    }

    public static boolean waitPort(int port, long timeoutMillis, long sleepMillis) {
        long time = System.currentTimeMillis() + timeoutMillis;
        while (!portAvailable(port) && System.currentTimeMillis() < time) {
            Threads.sleep(sleepMillis);
            logger.warn("waitPort");
        }
        return true;
    }
    
}
