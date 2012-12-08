/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers
 * 
 */
package saltserver.storage.secret;

/**
 *
 * @author evan
 */
public enum SecretValueQuery {
    insert,
    update,
    exists,
    delete,
    find_id,
    find,
    list,
    validate
}
