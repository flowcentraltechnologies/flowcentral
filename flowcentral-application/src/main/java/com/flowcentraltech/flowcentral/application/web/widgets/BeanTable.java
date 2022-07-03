/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Order.Part;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Bean table object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class BeanTable extends AbstractTable<List<?>, Object> {

    private ValueStore valueStore;

    public BeanTable(AppletUtilities au, TableDef tableDef) {
        this(au, tableDef, 0);
    }

    public BeanTable(AppletUtilities au, TableDef tableDef, int entryMode) {
        super(au, tableDef, null, entryMode);
    }

    public int getItemCount() throws UnifyException {
        List<?> sourceObject = getSourceObject();
        return sourceObject != null ? sourceObject.size() : 0;
    }

    @Override
    protected void validate(EvaluationMode evaluationMode, List<?> sourceObject, FormValidationErrors errors)
            throws UnifyException {
        if (isWithEntryPolicy()) {
            getEntryPolicy().validateEntries(evaluationMode, getParentReader(), getValueStore(sourceObject), errors);
        }
    }

    @Override
    protected void onLoadSourceObject(List<?> sourceObject, Set<Integer> selected) throws UnifyException {
        if (isWithEntryPolicy()) {
            getEntryPolicy().onEntryTableLoad(getParentReader(), getValueStore(sourceObject), selected);
        }
    }

    @Override
    protected void onFireOnTableChange(List<?> sourceObject, Set<Integer> selected) throws UnifyException {
        if (isWithEntryPolicy()) {
            getEntryPolicy().onEntryTableChange(getParentReader(), getValueStore(sourceObject), selected);
        }
    }

    @Override
    protected void onFireOnRowChange(List<?> sourceObject, RowChangeInfo rowChangeInfo) throws UnifyException {
        if (isWithEntryPolicy()) {
            ValueStore tableValueStore = getValueStore(sourceObject);
            tableValueStore.setDataIndex(rowChangeInfo.getRowIndex());
            getEntryPolicy().onEntryRowChange(getParentReader(), tableValueStore, rowChangeInfo);
        }
    }

    @Override
    protected int getSourceObjectSize(List<?> sourceObject) throws UnifyException {
        if (sourceObject != null) {
            return sourceObject.size();
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getDisplayItems(List<?> sourceObject, int dispStartIndex, int dispEndIndex)
            throws UnifyException {
        if (sourceObject != null) {
            if (dispStartIndex == 0 && dispEndIndex == sourceObject.size()) {
                return (List<Object>) sourceObject;
            }

            return (List<Object>) sourceObject.subList(dispStartIndex, dispEndIndex);
        }

        return Collections.emptyList();
    }

    @Override
    protected void orderOnReset() throws UnifyException {
        List<?> sourceObject = getSourceObject();
        if (sourceObject != null && !sourceObject.isEmpty()) {
            Order order = getOrder();
            if (order != null) {
                Class<?> beanClass = sourceObject.get(0).getClass();
                List<Part> parts = order.getParts();
                for (int i = parts.size() - 1; i >= 0; i--) {
                    Part part = parts.get(i);
                    if (part.isAscending()) {
                        DataUtils.sortAscending(sourceObject, beanClass, part.getField());
                    } else {
                        DataUtils.sortDescending(sourceObject, beanClass, part.getField());
                    }
                }
            }
        }
    }

    private ValueStore getValueStore(List<?> sourceObject) throws UnifyException {
        List<?> oldSourceObject = valueStore != null ? (List<?>) valueStore.getValueObject() : null;
        if (sourceObject != oldSourceObject) {
            synchronized (this) {
                oldSourceObject = valueStore != null ? (List<?>) valueStore.getValueObject() : null;
                if (sourceObject != oldSourceObject) {
                    valueStore = new BeanValueListStore(sourceObject);
                }
            }
        }

        return valueStore;
    }
}
