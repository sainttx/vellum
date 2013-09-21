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

import java.security.PrivateKey;
import java.util.Date;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import sun.security.pkcs.PKCS10;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

/**
 *
 * @author evan.summers
 */
public class GeneratedRsaKeyPair {    
    private String providerName = null;
    private String keyAlgName = "RSA";
    private String sigAlgName = "SHA1WithRSA";
    private int keySize = 2048;
    private CertAndKeyGen keyPair;
    private PrivateKey privateKey;
    private X509Certificate cert;
    
    public GeneratedRsaKeyPair() {
    }

    public void generate(String dname, Date startDate, int validityDays) throws Exception {
        keyPair = new CertAndKeyGen(keyAlgName, sigAlgName, providerName);
        keyPair.generate(keySize);
        privateKey = keyPair.getPrivateKey();
        X500Name x500Name = new X500Name(dname);
        cert = keyPair.getSelfCertificate(x500Name, startDate, 
                TimeUnit.DAYS.toSeconds(validityDays));
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public X509Certificate getCert() {
        return cert;
    }    
}
