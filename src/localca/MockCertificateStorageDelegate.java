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
package localca;

import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author evans
 */
public class MockCertificateStorageDelegate implements TrustManagerDelegate {
    Set<Principal> acceptableSet = new TreeSet();
    Map<Principal, X509Certificate> trustedMap = new HashMap();

    public Set<Principal> getAcceptableSet() {
        return acceptableSet;
    }

    public Map<Principal, X509Certificate> getTrustedMap() {
        return trustedMap;
    }
        
    @Override
    public boolean accept(X509Certificate peerCertificate) throws CertificateException {
        Principal principal = peerCertificate.getSubjectDN();
        if (acceptableSet.remove(principal)) {
            trustedMap.put(principal, peerCertificate);
            return true;
        } else {
            X509Certificate trustedCertificate = trustedMap.get(principal);
            return Arrays.equals(trustedCertificate.getPublicKey().getEncoded(),
                    peerCertificate.getPublicKey().getEncoded());
        }
    }
}
