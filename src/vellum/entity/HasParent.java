/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public interface HasParent<P> {
    public P getParent();
}
