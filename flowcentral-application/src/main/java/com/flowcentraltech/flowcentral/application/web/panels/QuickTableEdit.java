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

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanTable;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormMessages;
import com.flowcentraltech.flowcentral.common.data.PageLoadDetails;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Quick table edit object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class QuickTableEdit {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;
    
    private final String entryTable;
    
    private final String entryEditPolicy;

    private BeanTable entryBeanTable;

    private String entryCaption;

    private List<FormMessage> validationErrors;

    private int width;
    
    private int height;
    
    public QuickTableEdit(AppletContext ctx, SweepingCommitPolicy sweepingCommitPolicy, EntityClassDef entityClassDef,
            String baseField, Object baseId, String entryTable, String entryEditPolicy) {
        this.ctx = ctx;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.entityClassDef = entityClassDef;
        this.baseField = baseField;
        this.baseId = baseId;
        this.entryTable = entryTable;
        this.entryEditPolicy = entryEditPolicy;
    }

    public BeanTable getEntryBeanTable() throws UnifyException {
        if (entryBeanTable == null) {
            entryBeanTable = ctx.au().constructEntryBeanTable(entryTable, entryEditPolicy);
        }

        return entryBeanTable;
    }

    public void setEntryCaption(String entryCaption) {
        this.entryCaption = entryCaption;
    }

    public String getEntryCaption() {
        return entryCaption;
    }

    public List<FormMessage> getValidationErrors() {
        return validationErrors;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @SuppressWarnings("unchecked")
    public void loadEntryList() throws UnifyException {
        final BeanTable _beanTable = getEntryBeanTable();
        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .addEquals(baseField, baseId);
        List<Entity> resultList = (List<Entity>) ctx.environment().listAll(query);

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
    public boolean commitEntryList() throws UnifyException {
        List<? extends Entity> entryList = (List<? extends Entity>) getEntryBeanTable().getSourceObject();
        validationErrors = null;
        if (entryEditPolicy != null) {
            FormMessages messages = ctx.au().getComponent(ChildListEditPolicy.class, entryEditPolicy).validateEntries(
                    (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId, entryList);
            validationErrors = messages != null ? messages.getMessages() : null;
        }
        
        if (validationErrors == null) {
            ctx.environment().updateEntryList(sweepingCommitPolicy, entryEditPolicy,
                    (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId, entryList);
            return true;
        }

        return false;
    }

}
