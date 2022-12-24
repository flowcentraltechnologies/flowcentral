package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Mapped entity provider component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface MappedEntityProvider<T extends Entity> extends UnifyComponent {

    String srcEntity();
    
    T find(Long id) throws UnifyException;

    T find(Long id, long versionNo) throws UnifyException;

    T find(Query<T> query) throws UnifyException;

    T findLean(Long id) throws UnifyException;

    T findLean(Long id, long versionNo) throws UnifyException;

    T findLean(Query<T> query) throws UnifyException;

    List<T> findAll(Query<T> query) throws UnifyException;

    T list(Long id) throws UnifyException;

    T list(Long id, long versionNo) throws UnifyException;

    T list(Query<T> query) throws UnifyException;

    T listLean(Long id) throws UnifyException;

    T listLean(Long id, long versionNo) throws UnifyException;

    T listLean(Query<T> query) throws UnifyException;

    List<T> listAll(Query<T> query) throws UnifyException;

    int countAll(Query<T> query) throws UnifyException;
}
