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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Entity CRUD page object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityCRUDPage {

    private final AppletContext ctx;

    private final AppletDef formAppletDef;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;

    private final String tableName;

    private final String createFormName;

    private final String maintainFormName;

    private final SectorIcon sectorIcon;

    private final BreadCrumbs breadCrumbs;

    private final EntityFormEventHandlers formEventHandlers;

    private final Restriction baseRestriction;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private EntityCRUD crud;

    public EntityCRUDPage(AppletContext ctx, AppletDef formAppletDef, EntityFormEventHandlers formEventHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, EntityClassDef entityClassDef, String baseField, Object baseId,
            SectorIcon sectorIcon, BreadCrumbs breadCrumbs, String tableName, String createFormName,
            String maintainFormName, Restriction baseRestriction) {
        this.ctx = ctx;
        this.formAppletDef = formAppletDef;
        this.formEventHandlers = formEventHandlers;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.entityClassDef = entityClassDef;
        this.baseField = baseField;
        this.baseId = baseId;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        this.tableName = tableName;
        this.createFormName = createFormName;
        this.maintainFormName = maintainFormName;
        this.baseRestriction = baseRestriction;
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

    public void crudSelectItem(int index) throws UnifyException {
        getCrud().enterMaintain(index);
    }

    public EntityCRUD getCrud() throws UnifyException {
        if (crud == null) {
            EntityTable entityTable = new EntityTable(ctx.au(), ctx.au().getTableDef(tableName));
            FormContext createFrmCtx = new FormContext(ctx, ctx.au().getFormDef(createFormName), formEventHandlers);
            FormContext maintainFrmCtx = new FormContext(ctx, ctx.au().getFormDef(maintainFormName),
                    formEventHandlers);
            MiniForm createForm = new MiniForm(MiniFormScope.MAIN_FORM, createFrmCtx,
                    createFrmCtx.getFormDef().getFormTabDef(0));
            MiniForm maintainForm = new MiniForm(MiniFormScope.MAIN_FORM, maintainFrmCtx,
                    maintainFrmCtx.getFormDef().getFormTabDef(0));
            crud = new EntityCRUD(ctx.au(), sweepingCommitPolicy, formAppletDef, entityClassDef, baseField, baseId,
                    entityTable, createForm, maintainForm);
        }

        return crud;
    }

    public void loadCrudList() throws UnifyException {
        EntityTable entityTable = getCrud().getTable();
        Restriction restriction = new Equals(baseField, baseId);
        if (baseRestriction != null) {
            restriction = new And().add(restriction).add(baseRestriction);
        }

        entityTable.setSourceObject(restriction);
        entityTable.setCrudActionHandlers(formEventHandlers.getCrudActionHandlers());

        if (ctx.isContextEditable()) {
            getCrud().enterCreate();
        } else {
            getCrud().enterMaintain(0);
        }
    }

}
