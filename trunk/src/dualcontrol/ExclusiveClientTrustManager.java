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

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author evan.summers
 */
public class ExclusiveClientTrustManager implements X509TrustManager {
    static Logger logger = LoggerFactory.getLogger(ExclusiveClientTrustManager.class);

    X509Certificate serverCertificate;
    X509TrustManager delegate;
    Map<String, X509Certificate> clientCertificateMap = new HashMap();
    
    public ExclusiveClientTrustManager(KeyStore trustStore) 
        throws GeneralSecurityException {
        this.delegate = KeyStores.getX509TrustManager(trustStore);
        for (String alias: Collections.list(trustStore.aliases())) {
            clientCertificateMap.put(alias, (X509Certificate) 
                    trustStore.getCertificate(alias));
        }
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) 
            throws CertificateException {
        logger.debug("checkClientTrusted {}", certs[0].getSubjectDN().getName());
        if (certs.length != 1) {
            throw new CertificateException("Invalid cert chain length");
        }
        X509Certificate trustedCertificate = clientCertificateMap.get(
                getCN(certs[0].getSubjectDN()));
        if (trustedCertificate == null) {
            throw new CertificateException("Untrusted client certificate");            
        }
        if (!Arrays.equals(certs[0].getPublicKey().getEncoded(),
                trustedCertificate.getPublicKey().getEncoded())) {
            throw new CertificateException("Invalid client certificate");
        }
        delegate.checkClientTrusted(certs, authType);
    }
    
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) 
            throws CertificateException {
        logger.debug("checkServerTrusted {}", certs[0].getSubjectDN().getName());
        delegate.checkServerTrusted(certs, authType);
    }        

    public static Map<String, X509Certificate> mapTrustStore(KeyStore trustStore) 
            throws KeyStoreException {
        Map<String, X509Certificate> clientCertificateMap = new HashMap();
        for (String alias : Collections.list(trustStore.aliases())) {
            clientCertificateMap.put(alias, (X509Certificate) 
                    trustStore.getCertificate(alias));
        }
        return clientCertificateMap;
    }
    
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