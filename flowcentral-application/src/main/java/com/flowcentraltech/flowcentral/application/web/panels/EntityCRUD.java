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

package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Entity CRUD
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityCRUD extends AbstractCRUD<EntityTable> {

    private final EntityClassDef entityClassDef;

    private final AppletDef formAppletDef;

    private final String childFieldName;

    public EntityCRUD(AppletUtilities au, SweepingCommitPolicy scp, AppletDef formAppletDef,
            EntityClassDef entityClassDef, String baseField, Object baseId, EntityTable table, MiniForm createForm,
            MiniForm maintainForm, String childFieldName, boolean allowAddition) throws UnifyException {
        super(au, scp, baseField, baseId, table, createForm, maintainForm, "$m{button.save}",
                allowAddition && isAllowCreate(formAppletDef));
        this.entityClassDef = entityClassDef;
        this.formAppletDef = formAppletDef;
        this.childFieldName = childFieldName;
    }

    @Override
    protected void evaluateFormContext(FormContext formContext, EvaluationMode evaluationMode) throws UnifyException {
        au().formContextEvaluator().evaluateFormContext(formContext, evaluationMode);
    }

    @Override
    protected Object createObject() throws UnifyException {
        Object entity = ReflectUtils.newInstance(entityClassDef.getEntityClass());
        DataUtils.setBeanProperty(entity, getBaseField(), getBaseId());
        return entity;
    }

    @Override
    protected void prepareCreate(FormContext formContext) throws UnifyException {
        if (formContext.getParentInst() != null) {
            au().populateNewChildReferenceFields(formContext.getParentEntityDef(), childFieldName,
                    formContext.getParentInst(), (Entity) formContext.getInst());
        } else {
            DataUtils.setBeanProperty((Entity) formContext.getInst(), getBaseField(), (Long) getBaseId());
        }

        au().onFormConstruct(formAppletDef, formContext, getBaseField(), true);
    }

    @Override
    protected void create(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        au().createEntityInstByFormContext(formAppletDef, formContext, scp);
    }

    @Override
    protected Object reload(Object inst) throws UnifyException {
        return au().environment().listLean(((Entity) inst).getClass(), ((Entity) inst).getId());
    }

    @Override
    protected void prepareMaintain(FormContext formContext) throws UnifyException {
        FormDef formDef = getMaintainFormDef();
        if (formDef.isWithConsolidatedFormState()) {
            Entity inst = (Entity) formContext.getInst();
            ConsolidatedFormStatePolicy policy = au().getComponent(ConsolidatedFormStatePolicy.class,
                    formDef.getConsolidatedFormState());
            boolean autoUpdated = policy.performAutoUpdates(new BeanValueStore(inst));
            if (autoUpdated) {
                inst = au().environment().listLean(inst.getClass(), inst.getId());
                formContext.setInst(inst);
            }
        }

        au().onFormConstruct(formAppletDef, formContext, getBaseField(), false);
    }

    @Override
    protected void update(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        au().updateEntityInstByFormContext(formAppletDef, formContext, scp);
    }

    @Override
    protected void delete(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        au().deleteEntityInstByFormContext(formAppletDef, formContext, scp);
    }

    private static boolean isAllowCreate(AppletDef formAppletDef) throws UnifyException {
        return formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.CREATE_FORM_SAVE)
                || formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.CREATE_FORM_SAVE_NEXT)
                || formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.CREATE_FORM_SAVE_CLOSE);
    }
}
