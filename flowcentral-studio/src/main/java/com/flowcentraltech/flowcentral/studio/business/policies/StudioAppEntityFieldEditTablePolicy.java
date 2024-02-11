/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.ArrayList;
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
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
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
    public void applyTableStateOverride(ValueStoreReader parentReader, ValueStore rowValueStore,
            TableStateOverride tableStateOverride) throws UnifyException {
        EntityFieldType type = rowValueStore.retrieve(EntityFieldType.class, "type");
        if (type.isBase() || type.isStatic()) {
            tableStateOverride.setColumnDisabled("label", true);
            tableStateOverride.setColumnDisabled("columnName", type.isStatic());
            tableStateOverride.setColumnDisabled("minLen", true);
            tableStateOverride.setColumnDisabled("maxLen", true);
            tableStateOverride.setColumnDisabled("precision", true);
            tableStateOverride.setColumnDisabled("scale", true);
            tableStateOverride.setColumnDisabled("nullable", true);
            tableStateOverride.setColumnDisabled("reportable", true);
            tableStateOverride.setColumnDisabled("auditable", true);
        } else {
            EntityFieldDataType dataType = rowValueStore.retrieve(EntityFieldDataType.class, "dataType");
            tableStateOverride.setColumnDisabled("minLen", !dataType.isString());
            tableStateOverride.setColumnDisabled("maxLen", !dataType.isString());
            tableStateOverride.setColumnDisabled("precision", !dataType.isNumber());
            tableStateOverride.setColumnDisabled("scale", !dataType.isDecimal());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<? extends T> commitList(Class<T> dataType, ValueStore tableValueStore) throws UnifyException {
        List<T> resultList = new ArrayList<>();
        final int len = tableValueStore.size();
        for (int i = 0; i < len; i++) {
            tableValueStore.setDataIndex(i);
            EntityFieldType type = tableValueStore.retrieve(EntityFieldType.class, "type");
            if (!type.isStatic()) {
                resultList.add((T) tableValueStore.getValueObjectAtDataIndex());
            }
        }

        return resultList;
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
    protected FixedRowActionType doApplyFixedAction(ValueStoreReader parentReader, ValueStore valueStore, int index,
            FixedRowActionType fixedActionType) throws UnifyException {
        return fixedActionType;
    }

    @Override
    protected void validateEntries(FormMessages messages, Class<? extends Entity> entityClass, String baseFieldName,
            Object baseId, List<?> instList) throws UnifyException {

    }

}
