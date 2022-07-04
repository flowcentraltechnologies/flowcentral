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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.EntitySelectHandler;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.ILike;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity select object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntitySelect {

    private final String fieldName;

    private String title;

    private String label;

    private String filter;

    private EntityTable entityTable;

    private ValueStore formValueStore;

    private String selectHandlerName;

    private Restriction baseRestriction;

    private boolean enableFilter;
    
    public EntitySelect(AppletUtilities au, TableDef tableDef, String searchFieldName, ValueStore formValueStore,
            String selectHandlerName, int limit) {
        this.entityTable = new EntityTable(au, tableDef);
        this.entityTable.setOrder(new Order().add(searchFieldName));
        this.entityTable.setLimit(limit);
        this.fieldName = searchFieldName;
        this.formValueStore = formValueStore;
        this.selectHandlerName = selectHandlerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public EntityTable getEntityTable() {
        return entityTable;
    }

    public Restriction getBaseRestriction() {
        return baseRestriction;
    }

    public void setBaseRestriction(Restriction baseRestriction) {
        this.baseRestriction = baseRestriction;
    }

    public boolean isEnableFilter() {
        return enableFilter;
    }

    public void setEnableFilter(boolean enableFilter) {
        this.enableFilter = enableFilter;
    }

    public void select(int index) throws UnifyException {
        if (formValueStore != null && selectHandlerName != null) {
            EntitySelectHandler handler = getEntityTable().au().getComponent(EntitySelectHandler.class,
                    selectHandlerName);
            Object sel = entityTable.getDisplayItem(index);
            handler.applySelection(formValueStore, new BeanValueStore(sel));
        }
    }

    public void applySelect() throws UnifyException {
        if (formValueStore != null && selectHandlerName != null) {
            EntitySelectHandler handler = entityTable.au().getComponent(EntitySelectHandler.class,
                    selectHandlerName);
            List<?> sel = entityTable.getSelectedItems();
            handler.applySelection(formValueStore, new BeanValueListStore(sel));
        }
    }

    public void ensureTableStruct() throws UnifyException {
        TableDef _eTableDef = entityTable.getTableDef();
        TableDef _nTableDef = entityTable.au().getTableDef(_eTableDef.getLongName());
        if (_eTableDef.getVersion() != _nTableDef.getVersion()) {
            entityTable = new EntityTable(entityTable.au(), _nTableDef);
            applyFilterToSearch();
        }
    }

    public void applyFilterToSearch() throws UnifyException {
        Restriction restriction = !StringUtils.isBlank(filter) ? new ILike(fieldName, filter) : null;
        if (baseRestriction != null) {
            if (restriction == null) {
                restriction = baseRestriction;
            } else {
                restriction = new And().add(baseRestriction).add(restriction);
            }
        }
        
        entityTable.setSourceObject(restriction);
    }

}
