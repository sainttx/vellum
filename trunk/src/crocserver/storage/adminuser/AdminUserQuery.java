/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package crocserver.storage.adminuser;

/**
 *
 * @author evan.summers
 */
public enum AdminUserQuery {
    validate,
    insert,
    update_login,
    update_logout,
    update_org,
    update_secret,
    update_cert,
    update_display_name,
    update_display_name_subject_cert,
    exists_username,
    exists_email,
    delete_username,
    find_username,
    find_email,
    list,
}
