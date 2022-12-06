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

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanTable;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormMessages;
import com.flowcentraltech.flowcentral.common.data.PageLoadDetails;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;

/**
 * Entry table page object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntryTablePage {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;

    private final SectorIcon sectorIcon;

    private final BreadCrumbs breadCrumbs;

    private final List<EventHandler> entrySwitchOnChangeHandlers;

    private final String entryTable;

    private final String entryEditPolicy;

    private final FilterGroupDef filterGroupDef;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private BeanTable entryBeanTable;

    private String entryCaption;

    private List<FormMessage> validationErrors;

    public EntryTablePage(AppletContext ctx, List<EventHandler> entrySwitchOnChangeHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, EntityClassDef entityClassDef, String baseField, Object baseId,
            SectorIcon sectorIcon, BreadCrumbs breadCrumbs, String entryTable, String entryEditPolicy,
            FilterGroupDef filterGroupDef) {
        this.ctx = ctx;
        this.entrySwitchOnChangeHandlers = entrySwitchOnChangeHandlers;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.entityClassDef = entityClassDef;
        this.baseField = baseField;
        this.baseId = baseId;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        this.entryTable = entryTable;
        this.entryEditPolicy = entryEditPolicy;
        this.filterGroupDef = filterGroupDef;
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

    public String getEntryCaption() {
        return entryCaption;
    }

    public void setEntryCaption(String entryCaption) {
        this.entryCaption = entryCaption;
    }

    public List<FormMessage> getValidationErrors() {
        return validationErrors;
    }

    public void addValidationError(FormMessage message) {
        if (validationErrors == null) {
            validationErrors = new ArrayList<FormMessage>();
        }

        validationErrors.add(message);
    }

    public boolean isWithValidationErrors() {
        return !DataUtils.isBlank(validationErrors);
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

    public boolean isWithSectorIcon() {
        return sectorIcon != null;
    }

    public void switchOnChange(RowChangeInfo rowChangeInfo) throws UnifyException {
        getEntryBeanTable().fireOnRowChange(rowChangeInfo);
    }

    public BeanTable getEntryBeanTable() throws UnifyException {
        if (entryBeanTable == null) {
            entryBeanTable = ctx.au().constructEntryBeanTable(entryTable, filterGroupDef, entryEditPolicy);
        }

        return entryBeanTable;
    }

    @SuppressWarnings("unchecked")
    public void loadEntryList() throws UnifyException {
        final BeanTable _beanTable = getEntryBeanTable();
        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .addEquals(baseField, baseId);
        Restriction br = filterGroupDef != null ? filterGroupDef.getRestriction(FilterType.TAB, null, ctx.au().getNow())
                : null;
        if (br != null) {
            query.addRestriction(br);
        }

        List<Entity> resultList = (List<Entity>) ctx.environment().listAll(query);

        _beanTable.setSwitchOnChangeHandlers(entrySwitchOnChangeHandlers);
        _beanTable.setSourceObject(resultList);
        _beanTable.setFixedAssignment(true);

        if (entryEditPolicy != null) {
            PageLoadDetails pageLoadDetails = ctx.au().getComponent(ChildListEditPolicy.class, entryEditPolicy)
                    .getOnLoadDetails((Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId);
            entryCaption = pageLoadDetails != null && pageLoadDetails.getCaption() != null
                    ? pageLoadDetails.getCaption()
                    : entryCaption;
        }
    }

    @SuppressWarnings("unchecked")
    public void commitEntryList(boolean reload) throws UnifyException {
        List<? extends Entity> entryList = (List<? extends Entity>) getEntryBeanTable().getSourceObject();
        ctx.environment().updateEntryList(sweepingCommitPolicy, entryEditPolicy,
                (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId, entryList);
        if (reload) {
            loadEntryList();
        }

        validationErrors = null;
        if (entryEditPolicy != null) {
            FormMessages messages = ctx.au().getComponent(ChildListEditPolicy.class, entryEditPolicy).validateEntries(
                    (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId, entryList);
            validationErrors = messages != null ? messages.getMessages() : null;
        }
    }
}
