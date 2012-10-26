/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigma.server.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import venigma.data.EntityPair;
import venigma.data.IdPair;

/**
 *
 * @author evan
 */
public class PairMap<T extends EntityPair> {
    Logr logger = LogrFactory.getLogger(PairMap.class);
    List<T> list = new ArrayList();
    Map<IdPair, T> map = new HashMap();
    
    public PairMap() {
    }
    
    public boolean exists(Comparable id, Comparable otherId) {
        return map.containsKey(new IdPair(id, otherId));
    }

    public List<T> getList() {
        return list;
    }

    public Map<IdPair, T> getMap() {
        return map;
    }
    
    public T get(IdPair idPair) {
        if (idPair == null) {
            throw StorageExceptionType.ID_NULL.newRuntimeException();    
        }
        return map.get(idPair);
    }

    public T find(IdPair idPair) throws StorageException {
        if (idPair == null) {
            throw StorageExceptionType.ID_NULL.newRuntimeException();    
        }
        if (!map.containsKey(idPair)) {
            throw StorageExceptionType.PAIR_NOT_FOUND.newException();
        }
        return map.get(idPair);
    }

    public void add(T entityPair) throws StorageException {
        if (entityPair == null) {
            throw StorageExceptionType.ENTITY_NULL.newException();    
        }
        logger.info("add", entityPair);
        if (map.containsKey(entityPair.getIdPair())) {
            throw StorageExceptionType.PAIR_ALREADY_EXISTS.newException();
        }
        map.put(entityPair.getIdPair(), entityPair);
    }
    
    public void update(T entityPair) throws StorageException {
        if (entityPair == null) {
            throw StorageExceptionType.ENTITY_NULL.newException();    
        }
        logger.info("update", entityPair);
        if (!map.containsKey(entityPair.getIdPair())) {
            throw StorageExceptionType.PAIR_NOT_FOUND.newException();
        }
    }

    public void remove(T entityPair) throws StorageException {
        if (entityPair == null) {
            throw StorageExceptionType.ENTITY_NULL.newException();    
        }
        logger.info("remove", entityPair);
        if (!map.containsKey(entityPair.getIdPair())) {
            throw StorageExceptionType.PAIR_NOT_FOUND.newException();
        }
    }    
}
