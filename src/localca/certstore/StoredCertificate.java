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
package localca.certstore;

import java.security.cert.X509Certificate;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author evans
 */
public class StoredCertificate {
    String commonName;
    String encoded;
    boolean revoked = false;

    public StoredCertificate(String commonName, String encoded) {
        this.commonName = commonName;
        this.encoded = encoded;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
        
    public String getCommonName() {
        return commonName;
    }

    public String getEncoded() {
        return encoded;
    }

    public boolean isRevoked() {
        return revoked;
    }        
}
