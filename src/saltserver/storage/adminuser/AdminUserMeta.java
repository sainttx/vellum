/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package saltserver.storage.adminuser;

/**
 *
 * @author evan.summers
 */
public enum AdminUserMeta {
    user_name,
    display_name,
    email,
    cert_subject,
    otp_secret,
    password_hash,
    enabled,    
    role_
}
