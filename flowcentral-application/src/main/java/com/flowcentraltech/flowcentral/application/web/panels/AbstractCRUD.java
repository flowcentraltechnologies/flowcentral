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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;

/**
 * CRUD object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCRUD<T extends AbstractTable<?, ?>> {

    private final AppletUtilities au;

    private final SweepingCommitPolicy scp;

    private final T table;

    private final MiniForm createForm;

    private final MiniForm maintainForm;

    private final String baseField;

    private final Object baseId;

    private int maintainIndex;

    private boolean create;

    public AbstractCRUD(AppletUtilities au, SweepingCommitPolicy scp, String baseField, Object baseId, T table,
            MiniForm createForm, MiniForm maintainForm) {
        this.au = au;
        this.scp = scp;
        this.baseField = baseField;
        this.baseId = baseId;
        this.table = table;
        this.createForm = createForm;
        this.maintainForm = maintainForm;
    }

    public AppletUtilities getAu() {
        return au;
    }

    public T getTable() {
        return table;
    }

    public MiniForm getForm() {
        return create ? createForm : maintainForm;
    }

    public boolean isWithFormErrors() {
        return getForm().getCtx().isWithFormErrors();
    }

    public boolean isWithDisplayItems() {
        return table.isWithDisplayItems();
    }

    public List<FormMessage> getValidationErrors() {
        return getForm().getCtx().getValidationErrors();
    }

    public boolean isCreate() {
        return create;
    }

    public boolean isMaintain() {
        return !create;
    }

    public void enterCreate() throws UnifyException {
        create = true;
        Object _inst = createObject();
        FormContext formContext = getForm().getCtx();
        formContext.clearValidationErrors();
        formContext.setInst(_inst);
        prepareCreate(formContext);
    }

    public void enterMaintain(int index) throws UnifyException {
        create = false;
        if (table.isWithDisplayItems()) {
            Object inst = table.getDisplayItem(index);
            Object _inst = reload(inst);
            final FormContext formContext = getForm().getCtx();
            formContext.setInst(_inst);
            prepareMaintain(formContext);
            maintainIndex = index;
        }        
    }

    public void save() throws UnifyException {
        final EvaluationMode evaluationMode = create ? EvaluationMode.CREATE : EvaluationMode.UPDATE;
        final FormContext formContext = getForm().getCtx();
        evaluateFormContext(formContext, evaluationMode);
        if (!isWithFormErrors()) {
            if (create) {
                create(formContext, scp);
                enterCreate();
            } else {
                update(formContext, scp);
                enterMaintain(maintainIndex);
            }
        }
    }

    protected String getBaseField() {
        return baseField;
    }

    protected Object getBaseId() {
        return baseId;
    }

    protected abstract void evaluateFormContext(FormContext formContext, EvaluationMode evaluationMode)
            throws UnifyException;

    protected abstract Object createObject() throws UnifyException;

    protected abstract void prepareCreate(FormContext formContext) throws UnifyException;

    protected abstract void create(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException;

    protected abstract Object reload(Object inst) throws UnifyException;

    protected abstract void prepareMaintain(FormContext formContext) throws UnifyException;

    protected abstract void update(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException;
}
