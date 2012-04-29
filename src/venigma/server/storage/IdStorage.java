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
        if (id == null) {
            throw new StorageRuntimeException(StorageExceptionType.ID_NULL);
        }
        return map.containsKey(id);
    }

    public List<T> getList() {
        return list;
    }

    public Map<Comparable, T> getMap() {
        return map;
    }
    
    public T get(Comparable id) {
        if (id == null) {
            throw StorageExceptionType.ID_NULL.newRuntimeException();
        }
        return map.get(id);
    }

    public T find(Comparable id) throws StorageException {
        if (id == null) {
            throw StorageExceptionType.ID_NULL.newException();
        }
        if (!map.containsKey(id)) {
            throw StorageExceptionType.ID_NOT_FOUND.newException();
        }
        return map.get(id);
    }

    public void add(T entity) throws StorageException {
        logger.info("add", entity);
        if (entity == null) {
            throw StorageExceptionType.ENTITY_NULL.newException();
        }
        if (map.containsKey(entity.getId())) {
            throw StorageExceptionType.ID_ALREADY_EXISTS.newException();
        }
        map.put(entity.getId(), entity);
    }
    
    public void update(T entity) throws StorageException {
        logger.info("update", entity);
        if (entity == null) {
            throw StorageExceptionType.ENTITY_NULL.newException();
        }
        if (!map.containsKey(entity.getId())) {
            throw StorageExceptionType.ID_NOT_FOUND.newException();
        }
    }

    public void remove(T entity) throws StorageException {
        logger.info("remove", entity);
        if (entity == null) {
            throw StorageExceptionType.ENTITY_NULL.newException();
        }
        if (!map.containsKey(entity.getId())) {
            throw StorageExceptionType.ID_NOT_FOUND.newException();
        }
    }
}
