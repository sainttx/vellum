/*
 * Copyright Evan Summers
 * 
 */
package crocserver.storage.adminuser;

/**
 *
 * @author evan
 */
public enum UserQuery {
    validate,
    insert,
    update_display_name_subject,
    update_login,
    update_logout,
    update_secret,
    update_cert,
    exists_username,
    exists_email,
    delete_username,
    find_username,
    find_email,
    list,
}
