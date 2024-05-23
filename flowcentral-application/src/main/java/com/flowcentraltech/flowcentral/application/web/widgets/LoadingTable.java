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
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.policies.LoadingParams;
import com.flowcentraltech.flowcentral.application.policies.LoadingTableProvider;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.LoadingItems;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.data.ButtonInfo;
import com.tcdng.unify.web.ui.widget.data.ColorLegendInfo;

/**
 * Entity table object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LoadingTable extends AbstractTable<LoadingParams, Entity> {

    private static final Order DEFAULT_TABLE_ORDER = new Order().add("id");

    private List<TableLoadingDef> altTableLoadingDefs;

    private List<ButtonInfo> altButtonInfos;

    private List<LoadingItems> loadingItems;

    public LoadingTable(AppletUtilities au, TableDef tableDef) {
        this(au, tableDef, null);
    }

    public LoadingTable(AppletUtilities au, TableDef tableDef, FilterGroupDef filterGroupDef) {
        super(au, tableDef, filterGroupDef, DEFAULT_TABLE_ORDER, 0);
        this.setFixedRows(tableDef.isFixedRows());
    }

    public void commitChange() throws UnifyException {
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
                currentTableLoadingDef = getTableLoadingDef(sectionIndex);
            }

            LoadingTableProvider loadingTableProvider = getLoadingTableProvider(currentTableLoadingDef);
            loadingTableProvider.commitChange(valueStore, true);
        }
    }

    public LoadingTableProvider getLoadingTableProvider(int itemIndex) throws UnifyException {
        List<Section> sections = getSections();
        final int len = sections.size();
        for (int i = 0; i < len; i++) {
            Section section = sections.get(i);
            if (section.isIndexWithin(itemIndex)) {
                TableLoadingDef currentTableLoadingDef = getTableLoadingDef(i);
                return getLoadingTableProvider(currentTableLoadingDef);
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

    public void setAltTableLoadingDefs(List<TableLoadingDef> altTableLoadingDefs) {
        this.altTableLoadingDefs = altTableLoadingDefs;
    }

    @Override
    public List<ButtonInfo> getActionBtnInfos() {
        if (!DataUtils.isBlank(altTableLoadingDefs)) {
            if (altButtonInfos == null) {
                altButtonInfos = InputWidgetUtils.getButtonInfos(altTableLoadingDefs);
            }

            return altButtonInfos;
        }

        return super.getActionBtnInfos();
    }

    public List<LoadingItems> getLoadingItems() {
        return loadingItems;
    }

    @Override
    protected void validate(EvaluationMode evaluationMode, LoadingParams sourceObject, FormValidationErrors errors)
            throws UnifyException {

    }

    @Override
    protected void onLoadSourceObject(LoadingParams sourceObject, Set<Integer> selected) throws UnifyException {

    }

    @Override
    protected EntryActionType onFireOnTableChange(LoadingParams sourceObject, Set<Integer> selected,
            TableChangeType changeType) throws UnifyException {
        return EntryActionType.NONE;
    }

    @Override
    protected EntryActionType onFireOnRowChange(LoadingParams sourceObject, RowChangeInfo rowChangeInfo)
            throws UnifyException {
        return EntryActionType.NONE;
    }

    @Override
    protected void orderOnReset() throws UnifyException {

    }

    @Override
    protected int getSourceObjectSize(LoadingParams restriction) throws UnifyException {
        final int len = getLoadingDefCount();
        int count = 0;
        for (int i = 0; i < len; i++) {
            TableLoadingDef tableLoadingDef = getTableLoadingDef(i);
            LoadingTableProvider loadingTableProvider = getLoadingTableProvider(tableLoadingDef);
            count += loadingTableProvider.countLoadingItems(restriction);
        }

        return count;
    }

    @Override
    protected synchronized List<Entity> getDisplayItems(LoadingParams loadingParams, int dispStartIndex,
            int dispEndIndex) throws UnifyException {
        if (loadingParams != null) {
            loadingItems = new ArrayList<LoadingItems>();
            final EntityClassDef entityClassDef = au().getEntityClassDef(getTableDef().getEntityDef().getLongName());
            final int len = getLoadingDefCount();
            List<Entity> items = new ArrayList<Entity>();
            List<Section> sectionList = new ArrayList<Section>(len);
            int startItemIndex = 0;
            for (int i = 0; i < len; i++) {
                loadingParams.restore();
                TableLoadingDef tableLoadingDef = getTableLoadingDef(i);
                LoadingTableProvider loadingTableProvider = getLoadingTableProvider(tableLoadingDef);
                final String label = loadingTableProvider.getLoadingLabel();

                LoadingItems _loadingItems = loadingTableProvider.getLoadingItems(loadingParams);
                _loadingItems.setSource(tableLoadingDef.getName());

                List<? extends Entity> _items = _loadingItems.getItems();
                Order order = getOrder();
                if (order == null) {
                    DataUtils.sortAscending(_items, Entity.class, "id");
                } else {
                    DataUtils.sort(_items, entityClassDef.getEntityClass(), order);
                }

                if (!DataUtils.isBlank(_items)) {
                    int endItemIndex = startItemIndex + _items.size() - 1;
                    sectionList.add(new Section(startItemIndex, endItemIndex, label));
                    items.addAll(_items);
                    startItemIndex = endItemIndex + 1;
                } else {
                    sectionList.add(new Section(label));
                }

                loadingItems.add(_loadingItems);
            }

            loadingItems = Collections.unmodifiableList(loadingItems);
            setSections(sectionList);
            loadingParams.restore();
            return items;
        }

        return Collections.emptyList();
    }

    private LoadingTableProvider getLoadingTableProvider(TableLoadingDef tableLoadingDef) throws UnifyException {
        LoadingTableProvider loadingTableProvider = au.getComponent(LoadingTableProvider.class,
                tableLoadingDef.getProvider());
        loadingTableProvider.setWorkingParameter(tableLoadingDef.getParameter());
        return loadingTableProvider;
    }

    private int getLoadingDefCount() {
        return !DataUtils.isBlank(altTableLoadingDefs) ? altTableLoadingDefs.size()
                : getTableDef().getLoadingDefCount();
    }

    private TableLoadingDef getTableLoadingDef(int index) {
        return !DataUtils.isBlank(altTableLoadingDefs) ? altTableLoadingDefs.get(index)
                : getTableDef().getTableLoadingDef(index);
    }
}
