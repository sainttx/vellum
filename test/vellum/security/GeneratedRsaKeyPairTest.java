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
package vellum.security;

import java.util.Date;
import org.junit.Test;
import vellum.logr.Logr;
import vellum.logr.LogrFactory; 

/**
 *
 * @author evan.summers
 */
public class GeneratedRsaKeyPairTest {
    Logr logger = LogrFactory.getLogger(getClass());    
    GeneratedRsaKeyPair rootKeyPair = new GeneratedRsaKeyPair();
    GeneratedRsaKeyPair clientKeyPair = new GeneratedRsaKeyPair();
    String rootDname = Certificates.formatDname(
            "localhost", "serverUnit", "serverOrg", "WP", "CT", "za");
    String clientDname = Certificates.formatDname(
            "123456", "clientUnit", "clientOrg", "WP", "CT", "za");
    
    @Test
    public void test() throws Exception {
        rootKeyPair.generate(rootDname, new Date(), 999);
        clientKeyPair.generate(clientDname, new Date(), 999);
        logger.info(Certificates.buildCertReqPem(clientKeyPair.getCertReq()));
        clientKeyPair.sign(rootKeyPair.getPrivateKey(), rootKeyPair.getCert());
        logger.info(Certificates.buildKeyPem(clientKeyPair.getPrivateKey()));
        logger.info(Certificates.buildCertPem(clientKeyPair.getCert()));
    }
}
