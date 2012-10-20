/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.server.BizstatServer;
import vellum.entity.EntityMap;
import vellum.entity.HasParent;
import vellum.entity.IdEntity;
import java.io.PrintStream;

/**
 *
 * @author evan
 */
public class BizstatStorageHtmlPrinter {

    BizstatServer context;
    BizstatConfigStorage storage;
    PrintStream stream;

    public BizstatStorageHtmlPrinter(BizstatServer context) {
        this.context = context;
        this.storage = context.getConfigStorage();
    }

    public void print(PrintStream stream) {
        this.stream = stream;
        for (EntityMap entityMap : storage.entityTypeMap.values()) {
            print(entityMap);
        }
    }

    private <E extends IdEntity> void print(EntityMap<E> entityMap) {
        stream.printf("\n<h2>%s</h2>\n", entityMap.getEntityType().getSimpleName());
        for (E entity : entityMap.getExtentList()) {
            stream.println(format(entity));
        }
    }
    
    private String format(IdEntity entity) {
        if (entity instanceof HasParent) {
        }
        return entity.toString();
    }
}
