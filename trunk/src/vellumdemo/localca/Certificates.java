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
package vellumdemo.localca;

import java.security.Principal;
import java.security.cert.CertificateException;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

/**
 *
 * @author evan.summers
 */
public class Certificates {
    
    public static String getCN(Principal principal) throws CertificateException {
        String dname = principal.getName();
        try {
            LdapName ln = new LdapName(dname);
            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    return rdn.getValue().toString();
                }
            }
            throw new InvalidNameException("no CN: " + dname);
        } catch (Exception e) {
            throw new CertificateException(e.getMessage());
        }
    }    
    
}