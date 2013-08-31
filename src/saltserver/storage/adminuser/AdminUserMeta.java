/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
