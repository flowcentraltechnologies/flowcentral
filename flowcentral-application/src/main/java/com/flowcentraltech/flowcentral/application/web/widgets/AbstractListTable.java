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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Order.Part;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Abstract list table object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AbstractListTable<T> extends AbstractTable<List<? extends T>, T> {

    protected AbstractListTable(AppletUtilities au, TableDef tableDef, FilterGroupDef filterGroupDef, int entryMode) {
        super(au, tableDef, filterGroupDef, null, entryMode);
    }

    public int getItemCount() throws UnifyException {
        List<? extends T> sourceObject = getSourceObject();
        return sourceObject != null ? sourceObject.size() : 0;
    }

    @Override
    protected void validate(EvaluationMode evaluationMode, List<? extends T> sourceObject, FormValidationErrors errors)
            throws UnifyException {
        if (isWithEntryPolicy()) {
            getEntryPolicy().validateEntries(evaluationMode, getParentReader(), getValueStore(sourceObject), errors);
        }
    }

    @Override
    protected void onLoadSourceObject(List<? extends T> sourceObject, Set<Integer> selected) throws UnifyException {
        if (isWithEntryPolicy()) {
            getEntryPolicy().onEntryTableLoad(getParentReader(), getValueStore(sourceObject), selected);
        }
    }

    @Override
    protected EntryActionType onFireOnTableChange(List<? extends T> sourceObject, Set<Integer> selected,
            TableChangeType changeType) throws UnifyException {
        if (isWithEntryPolicy()) {
            return getEntryPolicy().onEntryTableChange(getParentReader(), getValueStore(sourceObject), selected,
                    changeType);
        }

        return EntryActionType.NONE;
    }

    @Override
    protected EntryActionType onFireOnRowChange(List<? extends T> sourceObject, RowChangeInfo rowChangeInfo)
            throws UnifyException {
        if (isWithEntryPolicy()) {
            ValueStore tableValueStore = getValueStore(sourceObject);
            tableValueStore.setDataIndex(rowChangeInfo.getRowIndex());
            return getEntryPolicy().onEntryRowChange(getParentReader(), tableValueStore, rowChangeInfo);
        }

        return EntryActionType.NONE;
    }

    @Override
    protected int getSourceObjectSize(List<? extends T> sourceObject) throws UnifyException {
        if (sourceObject != null) {
            return sourceObject.size();
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<T> getDisplayItems(List<? extends T> sourceObject, int dispStartIndex, int dispEndIndex)
            throws UnifyException {
        if (sourceObject != null) {
            List<T> resultList = dispStartIndex == 0 && dispEndIndex == sourceObject.size()
                    ? (List<T>) sourceObject
                    : (List<T>) sourceObject.subList(dispStartIndex, dispEndIndex);
            if (resultList.size() > 1) {
                Order order = getOrder();
                if (order != null) {
                    DataUtils.sort(resultList, au().getEntityClassDef(getEntityDef().getLongName()).getEntityClass(),
                            order);
                    return new ArrayList<>(resultList);
                }
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    protected void orderOnReset() throws UnifyException {
        List<? extends T> sourceObject = getSourceObject();
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

    private ValueStore getValueStore(List<? extends T> sourceObject) throws UnifyException {
        return new BeanValueListStore(sourceObject);
    }
}
