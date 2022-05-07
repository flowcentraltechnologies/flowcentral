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
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;

/**
 * Entry page object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntryPage {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;

    private final BreadCrumbs breadCrumbs;

    private final List<EventHandler> entrySwitchOnChangeHandlers;

    private final String entryTable;

    private final String entryTablePolicy;

    private final String entryUpdatePolicy;

    private final FilterDef entryFilter;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private BeanTable entryBeanTable;

    public EntryPage(AppletContext ctx, List<EventHandler> entrySwitchOnChangeHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, EntityClassDef entityClassDef, String baseField, Object baseId,
            BreadCrumbs breadCrumbs, String entryTable, String entryTablePolicy, String entryUpdatePolicy,
            FilterDef entryFilter) {
        this.ctx = ctx;
        this.entrySwitchOnChangeHandlers = entrySwitchOnChangeHandlers;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.entityClassDef = entityClassDef;
        this.baseField = baseField;
        this.baseId = baseId;
        this.breadCrumbs = breadCrumbs;
        this.entryTable = entryTable;
        this.entryTablePolicy = entryTablePolicy;
        this.entryUpdatePolicy = entryUpdatePolicy;
        this.entryFilter = entryFilter;
    }

    public String getMainTitle() {
        return breadCrumbs.getLastBreadCrumb().getTitle();
    }

    public String getSubTitle() {
        return breadCrumbs.getLastBreadCrumb().getSubTitle();
    }

    public BreadCrumbs getBreadCrumbs() {
        return breadCrumbs;
    }

    public AppletUtilities getAu() {
        return ctx.getAu();
    }

    public AppletContext getCtx() {
        return ctx;
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

    public void switchOnChange() throws UnifyException {
        getEntryBeanTable().fireOnChange();
    }

    public BeanTable getEntryBeanTable() throws UnifyException {
        if (entryBeanTable == null) {
            entryBeanTable = new BeanTable(ctx.getAu(), ctx.getAu().getTableDef(entryTable), true);
            if (!StringUtils.isBlank(entryTablePolicy)) {
                BeanTablePolicy policy = ctx.getAu().getComponent(BeanTablePolicy.class, entryTablePolicy);
                entryBeanTable.setPolicy(policy);
            }
        }

        return entryBeanTable;
    }

    @SuppressWarnings("unchecked")
    public void loadEntryList() throws UnifyException {
        final Date now = ctx.getAu().getNow();
        // Entry list
        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .addEquals(baseField, baseId);
        if (entryFilter != null) {
            Restriction br = entryFilter.getRestriction(entityClassDef.getEntityDef(), null, now);
            query.addRestriction(br);
        }

        List<Entity> resultList = (List<Entity>) ctx.getEnvironment().listAll(query);

        final BeanTable _beanTable = getEntryBeanTable();
        _beanTable.setSwitchOnChangeHandlers(entrySwitchOnChangeHandlers);
        _beanTable.setSourceObject(resultList);
        _beanTable.setFixedAssignment(true);
    }

    @SuppressWarnings("unchecked")
    public void commitEntryList(boolean reload) throws UnifyException {
        List<? extends Entity> assignedList = (List<? extends Entity>) getEntryBeanTable().getSourceObject();
        ctx.getEnvironment().updateEntryList(sweepingCommitPolicy, entryUpdatePolicy,
                (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId,
                assignedList);
        if (reload) {
            loadEntryList();
        }
    }
}
