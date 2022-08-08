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
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;

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

    private final EntityDef parentEntityDef;

    private final Entity parentInst;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;

    private final String tableName;

    private final String entryEditPolicy;

    private final String createFormName;

    private final String maintainFormName;

    private final String childListName;

    private final SectorIcon sectorIcon;

    private final BreadCrumbs breadCrumbs;

    private final EntityFormEventHandlers formEventHandlers;

    private final Restriction baseRestriction;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private EntityCRUD crud;

    public EntityCRUDPage(AppletContext ctx, AppletDef formAppletDef, EntityFormEventHandlers formEventHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, EntityDef parentEntityDef, Entity parentInst,
            EntityClassDef entityClassDef, String baseField, Object baseId, String childListName, SectorIcon sectorIcon,
            BreadCrumbs breadCrumbs, String tableName, String entryEditPolicy, String createFormName,
            String maintainFormName, Restriction baseRestriction) {
        this.ctx = ctx;
        this.formAppletDef = formAppletDef;
        this.formEventHandlers = formEventHandlers;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.parentEntityDef = parentEntityDef;
        this.parentInst = parentInst;
        this.entityClassDef = entityClassDef;
        this.baseField = baseField;
        this.baseId = baseId;
        this.childListName = childListName;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        this.tableName = tableName;
        this.entryEditPolicy = entryEditPolicy;
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

    public void switchOnChange() throws UnifyException {
        MiniForm form = getCrud().getForm();
        form.getCtx().resetTabIndex();

        Entity inst = (Entity) form.getFormBean();
        FormTabDef formTabDef = form.getFormTabDef();
        // Clear unsatisfactory foreign key fields
        if (formTabDef.isWithCondRefDefFormFields()) {
            ctx.au().clearUnsatisfactoryRefs(formTabDef, entityClassDef.getEntityDef(),
                    form.getCtx().getFormValueStore().getReader(), inst);
        }

        // Populate entity list-only fields
        ctx.au().populateListOnlyFields(entityClassDef.getEntityDef(), inst);
    }

    public EntityCRUD getCrud() throws UnifyException {
        if (crud == null) {
            TableDef tableDef = ctx.au().getTableDef(tableName);
            EntityTable entityTable = new EntityTable(ctx.au(), tableDef);
            if (!StringUtils.isBlank(entryEditPolicy)) {
                ChildListEditPolicy policy = ctx.au().getComponent(ChildListEditPolicy.class, entryEditPolicy);
                entityTable.setPolicy(policy);
            }

            FormContext createFrmCtx = new FormContext(ctx, ctx.au().getFormDef(createFormName), formEventHandlers);
            createFrmCtx.setCrudMode();
            createFrmCtx.setParentEntityDef(parentEntityDef);
            createFrmCtx.setParentInst(parentInst);

            FormContext maintainFrmCtx = new FormContext(ctx, ctx.au().getFormDef(maintainFormName), formEventHandlers);
            maintainFrmCtx.setCrudMode();
            maintainFrmCtx.setParentEntityDef(parentEntityDef);
            maintainFrmCtx.setParentInst(parentInst);

            MiniForm createForm = new MiniForm(MiniFormScope.MAIN_FORM, createFrmCtx,
                    createFrmCtx.getFormDef().getFormTabDef(0));
            MiniForm maintainForm = new MiniForm(MiniFormScope.MAIN_FORM, maintainFrmCtx,
                    maintainFrmCtx.getFormDef().getFormTabDef(0));
            crud = new EntityCRUD(ctx.au(), sweepingCommitPolicy, formAppletDef, entityClassDef, baseField, baseId,
                    entityTable, createForm, maintainForm, childListName);
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
