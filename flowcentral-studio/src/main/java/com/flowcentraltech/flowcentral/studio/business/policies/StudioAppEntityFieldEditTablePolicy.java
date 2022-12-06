/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.FixedRowActionType;
import com.flowcentraltech.flowcentral.common.business.policies.TableStateOverride;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormMessages;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.PageLoadDetails;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;

/**
 * Studio application entity field table policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences("application.appEntityField")
@Component(name = "studioappentityfield-tablepolicy", description = "Studio Application Entity Field Table Policy")
public class StudioAppEntityFieldEditTablePolicy extends AbstractChildListEditPolicy {

    @Override
    public void onEntryTableLoad(ValueStoreReader parentReader, ValueStore tableValueStore, Set<Integer> selected)
            throws UnifyException {

    }

    @Override
    public EntryActionType onEntryTableChange(ValueStoreReader parentReader, ValueStore tableValueStore,
            Set<Integer> selected, TableChangeType changeType) throws UnifyException {
        
        return EntryActionType.NONE;
    }

    @Override
    public EntryActionType onEntryRowChange(ValueStoreReader parentReader, ValueStore tableValueStore,
            RowChangeInfo rowChangeInfo) throws UnifyException {

        return EntryActionType.NONE;
    }

    @Override
    public void validateEntries(EvaluationMode evaluationMode, ValueStoreReader parentReader,
            ValueStore tableValueStore, FormValidationErrors errors) throws UnifyException {

    }

    @Override
    public void applyTableStateOverride(ValueStoreReader parentReader, ValueStore valueStore,
            TableStateOverride tableStateOverride) throws UnifyException {

    }

    @Override
    public FixedRowActionType resolveFixedIndex(ValueStoreReader parentReader, ValueStore valueStore, int index,
            int size) throws UnifyException {
        return FixedRowActionType.FIXED;
    }

    @Override
    public int resolveActionIndex(ValueStoreReader parentReader, ValueStore valueStore, int index, int size)
            throws UnifyException {
        return 0;
    }

    @Override
    public void postAssignmentUpdate(Class<? extends Entity> entityClass, String baseFieldName, Object baseId)
            throws UnifyException {

    }

    @Override
    public void postEntryUpdate(Class<? extends Entity> entityClass, String baseFieldName, Object baseId,
            List<?> instList) throws UnifyException {

    }

    @Override
    public PageLoadDetails getOnLoadDetails(Class<? extends Entity> entityClass, String baseFieldName, Object baseId)
            throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void doApplyFixedAction(ValueStoreReader parentReader, ValueStore valueStore, int index,
            FixedRowActionType fixedActionType) throws UnifyException {
        
    }

    @Override
    protected void validateEntries(FormMessages messages, Class<? extends Entity> entityClass, String baseFieldName,
            Object baseId, List<?> instList) throws UnifyException {

    }

}