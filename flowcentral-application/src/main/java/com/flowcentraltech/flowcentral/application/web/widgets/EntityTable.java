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

import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFeatureConstants;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Entity table object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityTable extends AbstractTable<Restriction, Entity> {

    private static final Order DEFAULT_TABLE_ORDER = new Order().add("id");

    private final String saveGlobalTableQuickFilterPrivilege;

    private int limit;

    private boolean limitSelectToColumns;

    public EntityTable(AppletUtilities au, TableDef tableDef) {
        super(au, tableDef, DEFAULT_TABLE_ORDER, 0);
        this.setFixedRows(tableDef.isFixedRows());
        this.limitSelectToColumns = true;
        this.saveGlobalTableQuickFilterPrivilege = PrivilegeNameUtils
                .getFeaturePrivilegeName(ApplicationFeatureConstants.SAVE_GLOBAL_TABLE_QUICK_FILTER);
    }

    public String getSaveGlobalTableQuickFilterPrivilege() {
        return saveGlobalTableQuickFilterPrivilege;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isLimitSelectToColumns() {
        return limitSelectToColumns;
    }

    public void setLimitSelectToColumns(boolean limitSelectToColumns) {
        this.limitSelectToColumns = limitSelectToColumns;
    }

    @Override
    protected void validate(EvaluationMode evaluationMode, Restriction sourceObject, FormValidationErrors errors)
            throws UnifyException {

    }

    @Override
    protected void onLoadSourceObject(Restriction sourceObject, Set<Integer> selected) throws UnifyException {

    }

    @Override
    protected void onFireOnTableChange(Restriction sourceObject, Set<Integer> selected) throws UnifyException {

    }

    @Override
    protected EntryActionType onFireOnRowChange(Restriction sourceObject, RowChangeInfo rowChangeInfo)
            throws UnifyException {
        return EntryActionType.NONE;
    }

    @Override
    protected void orderOnReset() throws UnifyException {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected int getSourceObjectSize(Restriction restriction) throws UnifyException {
        final EntityClassDef entityClassDef = au.getEntityClassDef(getTableDef().getEntityDef().getLongName());
        if (restriction != null) {
            return au.environment().countAll(
                    Query.ofDefaultingToAnd((Class<? extends Entity>) entityClassDef.getEntityClass(), restriction));
        }

        return au.environment().countAll(
                Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).ignoreEmptyCriteria(true));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Entity> getDisplayItems(Restriction restriction, int dispStartIndex, int dispEndIndex)
            throws UnifyException {
        final TableDef tableDef = getTableDef();
        final EntityClassDef entityClassDef = au.getEntityClassDef(tableDef.getEntityDef().getLongName());
        if (restriction != null) {
            Query<? extends Entity> query = Query
                    .ofDefaultingToAnd((Class<? extends Entity>) entityClassDef.getEntityClass(), restriction)
                    .setOrder(getOrder()).setOffset(dispStartIndex).setLimit(dispEndIndex - dispStartIndex);
            if (limitSelectToColumns && tableDef.isLimitSelectToColumns()) {
                query.setSelect(tableDef.getSelect());
            }

            return (List<Entity>) au.environment().listAll(query);
        }

        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .ignoreEmptyCriteria(true).setOrder(getOrder()).setOffset(dispStartIndex)
                .setLimit(dispEndIndex - dispStartIndex);
        if (limitSelectToColumns && tableDef.isLimitSelectToColumns()) {
            query.setSelect(tableDef.getSelect());
        }

        return (List<Entity>) au.environment().listAll(query);
    }
}
