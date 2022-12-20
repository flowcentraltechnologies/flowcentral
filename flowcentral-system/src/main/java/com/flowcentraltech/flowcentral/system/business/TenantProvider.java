package com.flowcentraltech.flowcentral.system.business;

import java.util.List;

import com.flowcentraltech.flowcentral.system.entities.Tenant;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Query;

/**
 * Tenant provider component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface TenantProvider extends UnifyComponent {

    Tenant find(Long id) throws UnifyException;

    Tenant find(Long id, Object versionNo) throws UnifyException;

    Tenant find(Query<Tenant> query) throws UnifyException;

    Tenant findLean(Long id) throws UnifyException;

    Tenant findLean(Long id, Object versionNo) throws UnifyException;

    Tenant findLean(Query<Tenant> query) throws UnifyException;

    List<Tenant> findAll(Query<Tenant> query) throws UnifyException;

    Tenant list(Long id) throws UnifyException;

    Tenant list(Long id, Object versionNo) throws UnifyException;

    Tenant list(Query<Tenant> query) throws UnifyException;

    Tenant listLean(Long id) throws UnifyException;

    Tenant listLean(Long id, Object versionNo) throws UnifyException;

    Tenant listLean(Query<Tenant> query) throws UnifyException;

    List<Tenant> listAll(Query<Tenant> query) throws UnifyException;

    int countAll(Query<Tenant> query) throws UnifyException;
}
