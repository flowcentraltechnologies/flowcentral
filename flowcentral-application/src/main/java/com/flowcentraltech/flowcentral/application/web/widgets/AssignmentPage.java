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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;

/**
 * Assignment page object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AssignmentPage {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final AssignmentPageDef assignmentPageDef;

    private final EntityClassDef entityClassDef;

    private final Object baseId;

    private final SectorIcon sectorIcon;

    private final BreadCrumbs breadCrumbs;

    private final List<EventHandler> assnSwitchOnChangeHandlers;

    private final String entryTable;

    private final String assnEditPolicy;

    private final String pseudoDeleteField;

    private final FilterGroupDef filterGroupDef;

    private final boolean fixedAssignment;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private List<Long> assignedIdList;

    private BeanTable entryBeanTable;

    public AssignmentPage(AppletContext ctx, List<EventHandler> assnSwitchOnChangeHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, AssignmentPageDef assignmentPageDef,
            EntityClassDef entityClassDef, Object baseId, SectorIcon sectorIcon, BreadCrumbs breadCrumbs,
            String entryTable, String assnEditPolicy, String pseudoDeleteField, FilterGroupDef filterGroupDef,
            boolean fixedAssignment) {
        this.ctx = ctx;
        this.assnSwitchOnChangeHandlers = assnSwitchOnChangeHandlers;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.assignmentPageDef = assignmentPageDef;
        this.entityClassDef = entityClassDef;
        this.baseId = baseId;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        this.entryTable = entryTable;
        this.assnEditPolicy = assnEditPolicy;
        this.pseudoDeleteField = pseudoDeleteField;
        this.filterGroupDef = filterGroupDef;
        this.fixedAssignment = fixedAssignment;
    }

    public String getMainTitle() {
        return breadCrumbs.getLastBreadCrumb().getTitle();
    }

    public String getSubTitle() {
        return breadCrumbs.getLastBreadCrumb().getSubTitle();
    }

    public SectorIcon getSectorIcon() {
        return sectorIcon;
    }

    public BreadCrumbs getBreadCrumbs() {
        return breadCrumbs;
    }

    public AppletUtilities getAu() {
        return ctx.au();
    }

    public AppletContext getCtx() {
        return ctx;
    }

    public AssignmentPageDef getAssignmentPageDef() {
        return assignmentPageDef;
    }

    public EntityDef getEntityDef() {
        return entityClassDef.getEntityDef();
    }

    public Object getBaseId() {
        return baseId;
    }

    public String getDisplayItemCounter() {
        return displayItemCounter;
    }

    public void setDisplayItemCounter(String displayItemCounter) {
        this.displayItemCounter = displayItemCounter;
    }

    public String getDisplayItemCounterClass() {
        return displayItemCounterClass;
    }

    public void setDisplayItemCounterClass(String displayItemCounterClass) {
        this.displayItemCounterClass = displayItemCounterClass;
    }

    public void clearDisplayItem() {
        displayItemCounter = null;
        displayItemCounterClass = null;
    }

    public List<Long> getAssignedIdList() {
        return assignedIdList;
    }

    public void setAssignedIdList(List<Long> assignedIdList) {
        this.assignedIdList = assignedIdList;
    }

    public boolean isWithSectorIcon() {
        return sectorIcon != null;
    }

    public void switchOnChange(RowChangeInfo rowChangeInfo) throws UnifyException {
        if (isEntryTableMode()) {
            getEntryBeanTable().fireOnRowChange(rowChangeInfo);
        }
    }

    public BeanTable getEntryBeanTable() throws UnifyException {
        if (isEntryTableMode() && entryBeanTable == null) {
            entryBeanTable = new BeanTable(ctx.au(), ctx.au().getTableDef(entryTable), filterGroupDef,
                    BeanTable.ENTRY_ENABLED | BeanTable.SORT_DISABLED);
            if (!StringUtils.isBlank(assnEditPolicy)) {
                ChildListEditPolicy policy = ctx.au().getComponent(ChildListEditPolicy.class, assnEditPolicy);
                entryBeanTable.setPolicy(policy);
            }
        }

        return entryBeanTable;
    }

    public boolean isEntryTableMode() {
        return !StringUtils.isBlank(entryTable);
    }

    public boolean isPseudoDelete() {
        return !StringUtils.isBlank(pseudoDeleteField);
    }

    @SuppressWarnings("unchecked")
    public void loadAssignedList() throws UnifyException {
        if (isEntryTableMode()) {
            final Date now = ctx.au().getNow();
            // Assigned list
            Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                    .addEquals(assignmentPageDef.getBaseField(), baseId);
            if (filterGroupDef != null) {
                Restriction br = filterGroupDef.getRestriction(FilterType.TAB, null, now);
                if (br != null) {
                    query.addRestriction(br);
                }
            }

            List<Entity> resultList = (List<Entity>) ctx.environment().listAll(query);
            Set<Integer> selected = new HashSet<Integer>();
            final int len = resultList.size();
            if (len > 0) {
                if (isPseudoDelete()) {
                    DataUtils.sortAscending(resultList, entityClassDef.getEntityClass(), pseudoDeleteField);
                    ValueStore resultValueStore = new BeanValueListStore(resultList);
                    for (int i = 0; i < len; i++) {
                        resultValueStore.setDataIndex(i);
                        if (!resultValueStore.retrieve(boolean.class, pseudoDeleteField)) {
                            selected.add(i);
                        } else break;
                    }
                } else {
                    for (int i = 0; i < len; i++) {
                        selected.add(i);
                    }
                }
            }

            if (!fixedAssignment) {
                List<Long> assignedIdList = ctx.environment().getAssignedList(
                        (Class<? extends Entity>) entityClassDef.getEntityClass(), Long.class,
                        assignmentPageDef.getBaseField(), baseId, assignmentPageDef.getAssignField());
                // Add unassigned list
                final EntityFieldDef _assignFieldDef = entityClassDef.getEntityDef()
                        .getFieldDef(assignmentPageDef.getAssignField());
                final RefDef _assignRefDef = _assignFieldDef.getRefDef();
                final EntityClassDef _assignEntityClassDef = ctx.au().getEntityClassDef(_assignRefDef.getEntity());
                query = Query.of((Class<? extends Entity>) _assignEntityClassDef.getEntityClass());
                if (_assignRefDef.isWithFilterGenerator()) {
                    RefDef _baseRefDef = entityClassDef.getEntityDef().getFieldDef(assignmentPageDef.getBaseField())
                            .getRefDef();
                    EntityClassDef _baseEntityClassDef = ctx.au().getEntityClassDef(_baseRefDef.getEntity());
                    Entity baseInst = ctx.au().environment()
                            .listLean((Class<? extends Entity>) _baseEntityClassDef.getEntityClass(), baseId);
                    Restriction br = _assignRefDef.getFilter().getRestriction(_assignEntityClassDef.getEntityDef(),
                            new BeanValueStore(baseInst).getReader(), now);
                    query.addRestriction(br);
                } else if (_assignRefDef.isWithFilter()) {
                    Restriction br = _assignRefDef.getFilter().getRestriction(_assignEntityClassDef.getEntityDef(),
                            null, now);
                    query.addRestriction(br);
                }

                if (!assignedIdList.isEmpty()) {
                    query.addNotAmongst("id", assignedIdList);
                } else {
                    query.ignoreEmptyCriteria(true);
                }

                final EntityDef _entityDef = entityClassDef.getEntityDef();
                for (Long unAssignId : ctx.environment().valueList(Long.class, "id", query)) {
                    Entity entity = entityClassDef.newInst();
                    DataUtils.setBeanProperty(entity, assignmentPageDef.getBaseField(), baseId);
                    DataUtils.setBeanProperty(entity, assignmentPageDef.getAssignField(), unAssignId);
                    ctx.au().populateListOnlyFields(_entityDef, entity);
                    resultList.add(entity); // Addition is done here
                }
            }

            final BeanTable _beanTable = getEntryBeanTable();
            _beanTable.setSwitchOnChangeHandlers(assnSwitchOnChangeHandlers);
            _beanTable.setSelectedRows(selected);
            _beanTable.setSourceObject(resultList);
            _beanTable.setFixedAssignment(fixedAssignment);
        } else {
            assignedIdList = ctx.environment().getAssignedList(
                    (Class<? extends Entity>) entityClassDef.getEntityClass(), Long.class,
                    assignmentPageDef.getBaseField(), baseId, assignmentPageDef.getAssignField());
        }
    }

    @SuppressWarnings("unchecked")
    public void commitAssignedList(boolean reload) throws UnifyException {
        if (isEntryTableMode()) {
            List<? extends Entity> assignedList = (List<? extends Entity>) getEntryBeanTable().getSourceObject();
            if (!fixedAssignment) {
                if (isPseudoDelete()) {
                    final ValueStore resultValueStore = new BeanValueListStore(assignedList);
                    final Set<Integer> selected = getEntryBeanTable().getSelectedRows();
                    final int len = assignedList.size();
                    for (int i = 0; i < len; i++) {
                        resultValueStore.setDataIndex(i);
                        resultValueStore.store(pseudoDeleteField, !selected.contains(i));
                    }
                } else {
                    assignedList = (List<? extends Entity>) getEntryBeanTable().getSelectedItems();
                }
            }

            String assnUpdatePolicy = !StringUtils.isBlank(assnEditPolicy) ? assnEditPolicy
                    : assignmentPageDef.getUpdatePolicy();
            ctx.environment().updateAssignedList(sweepingCommitPolicy, assnUpdatePolicy,
                    (Class<? extends Entity>) entityClassDef.getEntityClass(), assignmentPageDef.getBaseField(), baseId,
                    assignedList, fixedAssignment);
            if (reload) {
                loadAssignedList();
            }
        } else {
            String assnUpdatePolicy = !StringUtils.isBlank(assnEditPolicy) ? assnEditPolicy
                    : assignmentPageDef.getUpdatePolicy();
            ctx.environment().updateAssignedList(sweepingCommitPolicy, assnUpdatePolicy,
                    (Class<? extends Entity>) entityClassDef.getEntityClass(), assignmentPageDef.getBaseField(), baseId,
                    assignmentPageDef.getAssignField(), assignedIdList);
        }
    }
}
