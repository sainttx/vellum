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
package vellum.util;

import java.net.Socket;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan.summers
 */
public class Sockets {
    public static String localHost = "127.0.0.1";
    public static Logr logger = LogrFactory.getLogger(Sockets.class);
    
    public static boolean portAvailable(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            socket.close();
            return true;
        } catch (Exception e) {
            logger.warn("portAvailable", e.getMessage());
            return false;
        }
    }

    public static boolean waitPort(int port, long timeoutMillis, long sleepMillis) {
        return waitPort(localHost, port, timeoutMillis, sleepMillis);
    }
    
    public static boolean waitPort(String host, int port, long timeoutMillis, long sleepMillis) {
        long time = System.currentTimeMillis() + timeoutMillis;
        while (!portAvailable(host, port) && System.currentTimeMillis() < time) {
            Threads.sleep(sleepMillis);
            logger.warn("waitPort");
        }
        return true;
    }
    
}
