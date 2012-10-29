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
    exists_org_host_client,
    find_org_host_client,    
    list_org,
    list,
    ;
    
}
