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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.policies.LoadingTableProvider;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.data.ColorLegendInfo;

/**
 * Entity table object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LoadingTable extends AbstractTable<Restriction, Entity> {

    private static final Order DEFAULT_TABLE_ORDER = new Order().add("id");

    public LoadingTable(AppletUtilities au, TableDef tableDef, FilterGroupDef filterGroupDef) {
        super(au, tableDef, filterGroupDef, DEFAULT_TABLE_ORDER, 0);
        this.setFixedRows(tableDef.isFixedRows());
    }
    
    public void commitChange() throws UnifyException {
        TableDef _tableDef = getTableDef();
        List<Section> sections = getSections();
        final int slen = sections.size();
        Section currentSection = null;
        TableLoadingDef currentTableLoadingDef = null;
        int sectionIndex = -1;
        ValueStore valueStore = new BeanValueListStore(getDispItemList());
        final int len = valueStore.size();
        for (int i = 0; i < len; i++) {
            valueStore.setDataIndex(i);
            while ((currentSection == null || !currentSection.isIndexWithin(i)) && ((++sectionIndex) < slen)) {
                currentSection = sections.get(sectionIndex);
                currentTableLoadingDef = _tableDef.getTableLoadingDef(sectionIndex);
            }

            LoadingTableProvider loadingTableProvider = au.getComponent(LoadingTableProvider.class,
                    currentTableLoadingDef.getProvider());
            loadingTableProvider.commitChange(valueStore);
        }
    }
    
    public LoadingTableProvider getLoadingTableProvider(int itemIndex) throws UnifyException {
        TableDef _tableDef = getTableDef();
        List<Section> sections = getSections();
        final int len = sections.size();
        for (int i = 0; i < len; i++) {
            Section section =  sections.get(i);
            if (section.isIndexWithin(itemIndex)) {
                TableLoadingDef currentTableLoadingDef = _tableDef.getTableLoadingDef(i);
                return au.getComponent(LoadingTableProvider.class,
                        currentTableLoadingDef.getProvider());
            }
        }
        
        return null;
    }
    
    public ColorLegendInfo getColorLegendInfo() {
        return getTableDef().getColorLegendInfo();
    }
    
    public boolean isWithColorLegendInfo() {
        return getTableDef().getColorLegendInfo() != null && !getTableDef().getColorLegendInfo().isEmpty();
    }

    @Override
    protected void validate(EvaluationMode evaluationMode, Restriction sourceObject, FormValidationErrors errors)
            throws UnifyException {

    }

    @Override
    protected void onLoadSourceObject(Restriction sourceObject, Set<Integer> selected) throws UnifyException {

    }

    @Override
    protected EntryActionType onFireOnTableChange(Restriction sourceObject, Set<Integer> selected,
            TableChangeType changeType) throws UnifyException {
        return EntryActionType.NONE;
    }

    @Override
    protected EntryActionType onFireOnRowChange(Restriction sourceObject, RowChangeInfo rowChangeInfo)
            throws UnifyException {
        return EntryActionType.NONE;
    }

    @Override
    protected void orderOnReset() throws UnifyException {

    }

    @Override
    protected int getSourceObjectSize(Restriction restriction) throws UnifyException {
        final TableDef tableDef = getTableDef();
        final int len = tableDef.getLoadingDefCount();
        int count = 0;
        for (int i = 0; i < len; i++) {
            TableLoadingDef tableLoadingDef = tableDef.getTableLoadingDef(i);
            LoadingTableProvider loadingTableProvider = au.getComponent(LoadingTableProvider.class,
                    tableLoadingDef.getProvider());
            count += loadingTableProvider.countLoadingItems(restriction);
        }

        return count;
    }

    @Override
    protected List<Entity> getDisplayItems(Restriction restriction, int dispStartIndex, int dispEndIndex)
            throws UnifyException {
        final TableDef tableDef = getTableDef();
        final int len = tableDef.getLoadingDefCount();
        List<Entity> items = new ArrayList<Entity>();
        List<Section> sectionList = new ArrayList<Section>(len);
        int startItemIndex = 0;
        for (int i = 0; i < len; i++) {
            TableLoadingDef tableLoadingDef = tableDef.getTableLoadingDef(i);
            LoadingTableProvider loadingTableProvider = au.getComponent(LoadingTableProvider.class,
                    tableLoadingDef.getProvider());
            String label = loadingTableProvider.getLoadingLabel();
            List<? extends Entity> _items = loadingTableProvider.getLoadingItems(restriction);
            Order order = getOrder();
            if (order == null) {
                DataUtils.sortAscending(_items, Entity.class, "id");
            } else {
                DataUtils.sort(_items, au().getEntityClassDef(tableDef.getEntityDef().getLongName()).getEntityClass(),
                        order);
            }

            if (!DataUtils.isBlank(_items)) {
                int endItemIndex = startItemIndex + _items.size() - 1;
                sectionList.add(new Section(startItemIndex, endItemIndex, label));
                items.addAll(_items);
                startItemIndex = endItemIndex + 1;
            } else {
                sectionList.add(new Section(label));
            }
        }

        setSections(sectionList);
        return items;
    }
}
