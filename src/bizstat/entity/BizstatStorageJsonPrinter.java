/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.server.BizstatServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import vellum.entity.EntityMap;
import vellum.entity.IdEntity;
import java.io.PrintStream;

/**
 *
 * @author evan
 */
public class BizstatStorageJsonPrinter {

    BizstatServer context;
    BizstatConfigStorage storage;
    PrintStream stream;
    GsonBuilder builder = new GsonBuilder();
    Gson gson; 
    
    public BizstatStorageJsonPrinter(BizstatServer context) {
        this.context = context;
        this.storage = context.getConfigStorage();
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public void print(PrintStream stream) {
        this.stream = stream;
        for (EntityMap entityMap : storage.entityTypeMap.values()) {
            print(entityMap);
        }
    }

    private <E extends IdEntity> void print(EntityMap<E> entityMap) {
        stream.println(entityMap.getEntityType().getSimpleName());
        stream.println(gson.toJson(entityMap.getExtentList()));
        for (E entity : entityMap.getExtentList()) {
            //stream.println(gson.toJson(entity));
        }
    }
    
}
