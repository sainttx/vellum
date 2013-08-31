/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package saltserver.storage.adminuser;

/**
 *
 * @author evan.summers
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
