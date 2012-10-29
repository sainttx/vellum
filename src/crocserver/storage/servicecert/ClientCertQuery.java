/*
 * Copyright Evan Summers
 * 
 */
package crocserver.storage.servicecert;

/**
 *
 * @author evan
 */
public enum ClientCertQuery {
    insert,
    update_cert,
    find_dname,
    find_id,
    delete_id,
    exists_org_host_service,    
    find_org_host_service,    
    list_org,
    list,
    ;
    
}
