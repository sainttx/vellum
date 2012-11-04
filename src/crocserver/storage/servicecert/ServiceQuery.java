/*
 * Copyright Evan Summers
 * 
 */
package crocserver.storage.servicecert;

/**
 *
 * @author evan
 */
public enum ServiceQuery {
    insert,
    update_cert,
    find_subject,
    find_id,
    delete_id,
    exists_org_host_account,
    find_org_host_account,    
    list_org,
    list,
    ;
    
}
