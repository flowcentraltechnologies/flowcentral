/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Bean CRUD
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class BeanCRUD extends AbstractCRUD<BeanListTable> {

    private final Class<?> beanClass;

    public BeanCRUD(AppletUtilities au, SweepingCommitPolicy scp, Class<?> beanClass, String baseField, Object baseId,
            BeanListTable table, MiniForm createForm, MiniForm maintainForm) {
        super(au, scp, baseField, baseId, table, createForm, maintainForm, "$m{button.add}", true);
        this.beanClass = beanClass;
    }

    @Override
    protected void evaluateFormContext(FormContext formContext, FormValidationContext vCtx) throws UnifyException {

    }

    @Override
    protected Object createObject() throws UnifyException {
        return ReflectUtils.newInstance(beanClass);
    }

    @Override
    protected void prepareCreate(FormContext formContext) throws UnifyException {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void create(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        List<Object> list = (List<Object>) getTable().getSourceObject();
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }

        list.add(formContext.getInst());
        getTable().setSourceObjectKeepSelected(list);
    }

    @Override
    protected void prepareMaintain(FormContext formContext) throws UnifyException {

    }

    @Override
    protected void update(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void delete(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        List<Object> list = (List<Object>) getTable().getSourceObject();
        list.remove(formContext.getInst());
    }

    @Override
    protected Object reload(Object inst) throws UnifyException {
        return inst;
    }

}
