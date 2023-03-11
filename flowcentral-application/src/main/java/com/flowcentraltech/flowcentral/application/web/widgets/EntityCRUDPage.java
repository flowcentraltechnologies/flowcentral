/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity CRUD page object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityCRUDPage {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final EntityDef parentEntityDef;

    private final Entity parentInst;

    private final String baseField;

    private final Object baseId;

    private final String appletName;

    private final String childFieldName;

    private final SectorIcon sectorIcon;

    private final BreadCrumbs breadCrumbs;

    private final EntityFormEventHandlers formEventHandlers;

    private final FilterGroupDef filterGroupDef;

    private final boolean viewOnly;

    private final boolean fixedRows;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private EntityCRUD crud;

    public EntityCRUDPage(AppletContext ctx, String appletName, EntityFormEventHandlers formEventHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, EntityDef parentEntityDef, Entity parentInst, String baseField,
            Object baseId, String childFieldName, SectorIcon sectorIcon, BreadCrumbs breadCrumbs,
            FilterGroupDef filterGroupDef, boolean viewOnly, boolean fixedRows) {
        this.ctx = ctx;
        this.appletName = appletName;
        this.formEventHandlers = formEventHandlers;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.parentEntityDef = parentEntityDef;
        this.parentInst = parentInst;
        this.baseField = baseField;
        this.baseId = baseId;
        this.childFieldName = childFieldName;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        this.filterGroupDef = filterGroupDef;
        this.viewOnly = viewOnly;
        this.fixedRows = fixedRows;
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

    public EntityDef getEntityDef() throws UnifyException {
        return ctx.au().getAppletEntityDef(appletName);
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

    public boolean isWithValidationErrors() {
        return crud.getForm().getCtx().isWithValidationErrors();
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

    public boolean isCreate() {
        return !viewOnly;
    }

    public void crudSelectItem(int index) throws UnifyException {
        getCrud().enterMaintain(index);
    }

    public void switchOnChange() throws UnifyException {
        ctx.au().onMiniformSwitchOnChange(getCrud().getForm());
    }

    public EntityCRUD getCrud() throws UnifyException {
        if (crud == null) {
            crud = ctx.au().constructEntityCRUD(ctx, appletName, formEventHandlers, sweepingCommitPolicy,
                    parentEntityDef, parentInst, baseField, baseId, childFieldName, filterGroupDef, viewOnly,
                    fixedRows);
        }

        return crud;
    }

    public void loadCrudList() throws UnifyException {
        EntityTable entityTable = getCrud().getTable();
        Restriction restriction = new Equals(baseField, baseId);
        Restriction baseRestriction = entityTable.getRestriction(FilterType.BASE, null, ctx.au().getNow());
        if (baseRestriction != null) {
            restriction = new And().add(restriction).add(baseRestriction);
        }

        entityTable.setSourceObjectKeepSelected(restriction);
        entityTable.setCrudActionHandlers(formEventHandlers.getCrudActionHandlers());

        if (ctx.isContextEditable() && isCreate()) {
            getCrud().enterCreate();
        } else {
            getCrud().enterMaintain(0);
        }
    }

}
