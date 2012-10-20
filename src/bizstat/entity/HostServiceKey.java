/*
 */
package bizstat.entity;

import vellum.type.ComparableTuple;

/**
 *
 * @author evans
 */
public class HostServiceKey extends ComparableTuple {
    Host host;
    Service service;

    public HostServiceKey(Host host, Service service) {
        super(new Comparable[] {host, service});
        this.host = host;
        this.service = service;
    }

    public Host getHost() {
        return host;
    }

    public Service getService() {
        return service;
    }
}
