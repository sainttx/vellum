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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import localca.Certificates;
import localca.TrustManagerDelegate;

/**
 *
 * @author evans
 */
public class StorageTrustManagerDelegate implements TrustManagerDelegate {
    final private boolean requireCertificate;
    final private CertificateStorage certificateStorage;
    
    public StorageTrustManagerDelegate(boolean requireCertificate, 
            CertificateStorage certificateStorage) {
        this.requireCertificate = requireCertificate;
        this.certificateStorage = certificateStorage;
    }

    @Override
    public boolean accept() throws CertificateException {
        return !requireCertificate;
    }
    
    @Override
    public boolean accept(X509Certificate peerCertificate) throws CertificateException {
        String commonName = Certificates.getCommonName(peerCertificate.getSubjectDN());
        try {
            if (certificateStorage.exists(commonName)) {
                if (certificateStorage.isNull(commonName)) {
                    certificateStorage.set(commonName, peerCertificate);
                    return true;
                } else {
                    X509Certificate trustedCertificate = certificateStorage.load(commonName);
                    if (new Date().after(trustedCertificate.getNotAfter())) {
                        certificateStorage.update(commonName, peerCertificate);
                        return true;
                    } else {
                        return Arrays.equals(peerCertificate.getPublicKey().getEncoded(),
                                trustedCertificate.getPublicKey().getEncoded());
                    }
                }
            } else {
                return false;
            }
        } catch (CertificateStorageException e) {
            throw new CertificateException(e);
        }
    }
}
