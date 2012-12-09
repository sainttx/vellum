/*
 * Copyright Evan Summers
 * 
 */
package saltserver.storage.adminuser;

/**
 *
 * @author evan
 */
public enum AdminUserQuery {
    validate,
    insert,
    update_subject,
    exists_username,
    exists_email,
    exists_subject,
    delete_username,
    find_username,
    find_email,
    find_subject,
    list,
}
