/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.IdEntity;

/**
 *
 * @author evan
 */
public class IdStorage<T extends IdEntity> {
    Logr logger = LogrFactory.getLogger(IdStorage.class);
    List<T> list = new ArrayList();
    Map<Comparable, T> map = new HashMap();
    
    public IdStorage() {
    }
    
    public void init(List<T> entityList) {        
        this.list.addAll(entityList);
        for (T entity : entityList) {
            map.put(entity.getId(), entity);
        }
    }

    public boolean exists(Comparable id) {
        return map.containsKey(id);
    }

    public List<T> getList() {
        return list;
    }

    public Map<Comparable, T> getMap() {
        return map;
    }
    
    public T get(Comparable id) {
        return map.get(id);
    }

    public void update(T entity) throws Exception {
        logger.info("update", entity);
    }

    public void add(T entity) {
        logger.info("add", entity);
    }

}
