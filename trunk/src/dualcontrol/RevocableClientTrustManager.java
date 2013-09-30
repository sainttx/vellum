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

import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.validator.Validator;

/**
 *
 * @author evan.summers
 */
public class RevocableClientTrustManager implements X509TrustManager {
    static Logger logger = LoggerFactory.getLogger(RevocableClientTrustManager.class);

    Validator validator;
    X509Certificate serverCertificate;
    X509TrustManager delegate;
    Collection<BigInteger> revocationList;
    
    public RevocableClientTrustManager(Validator validator, 
            X509Certificate serverCertificate, 
            X509TrustManager delegate,
            Collection<BigInteger> revocationList) {
        this.validator = validator;
        this.delegate = delegate;
        this.serverCertificate = serverCertificate;
        this.revocationList = revocationList;
    }
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[] {serverCertificate};
    }
    
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) 
            throws CertificateException {
        logger.debug("checkClientTrusted {}", certs[0].getSubjectDN().getName());
        if (certs.length != 2) {
            throw new CertificateException("Invalid cert chain length");
        }
        if (!certs[0].getIssuerX500Principal().equals(
                serverCertificate.getSubjectX500Principal())) {
            throw new CertificateException("Untrusted isser");
        }
        if (!Arrays.equals(certs[1].getPublicKey().getEncoded(),
                serverCertificate.getPublicKey().getEncoded())) {
            throw new CertificateException("Invalid server certificate");
        }
        if (revocationList.contains(certs[0].getSerialNumber())) {
            throw new CertificateException("Certificate in revocation list");
        }
        validator.validate(certs);
        delegate.checkClientTrusted(certs, authType);
    }
    
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) 
            throws CertificateException {
        delegate.checkServerTrusted(certs, authType);
    }        
        
}