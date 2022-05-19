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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.validation.FormContextEvaluator;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
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

    public EntityCRUD(AppletUtilities au, SweepingCommitPolicy scp, AppletDef formAppletDef,
            EntityClassDef entityClassDef, String baseField, Object baseId, EntityTable table, MiniForm createForm,
            MiniForm maintainForm) {
        super(au, scp, baseField, baseId, table, createForm, maintainForm);
        this.entityClassDef = entityClassDef;
        this.formAppletDef = formAppletDef;
    }

    @Override
    protected void evaluateFormContext(FormContext formContext, EvaluationMode evaluationMode) throws UnifyException {
        FormContextEvaluator formContextEvaluator = getAu().getComponent(FormContextEvaluator.class,
                ApplicationModuleNameConstants.FORMCONTEXT_EVALUATOR);
        formContextEvaluator.evaluateFormContext(formContext, evaluationMode);
    }

    @Override
    protected Object createObject() throws UnifyException {
        return ReflectUtils.newInstance(entityClassDef.getEntityClass());
    }

    @Override
    protected void prepareCreate(FormContext formContext) throws UnifyException {
        getAu().onFormConstruct(formAppletDef, formContext, getBaseField(), true);
    }

    @Override
    protected void create(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        getAu().createEntityInstByFormContext(formAppletDef, formContext, scp);
    }

    @Override
    protected Object reload(Object inst) throws UnifyException {
        return getAu().getEnvironment().listLean(((Entity) inst).getClass(), ((Entity) inst).getId());
    }

    @Override
    protected void prepareMaintain(FormContext formContext) throws UnifyException {
        getAu().onFormConstruct(formAppletDef, formContext, getBaseField(), false);
    }

    @Override
    protected void update(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        getAu().updateEntityInstByFormContext(formAppletDef, formContext, scp);
    }

}
