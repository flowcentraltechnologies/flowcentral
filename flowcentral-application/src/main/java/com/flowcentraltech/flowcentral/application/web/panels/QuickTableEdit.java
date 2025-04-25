/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormMessages;
import com.flowcentraltech.flowcentral.common.data.PageLoadDetails;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Quick table edit object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class QuickTableEdit {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;

    private final String entryTable;

    private final String entryEditPolicy;

    private BeanListTable entryBeanTable;

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

    public BeanListTable getEntryBeanTable() throws UnifyException {
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
        final BeanListTable _beanTable = getEntryBeanTable();
        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .addEquals(baseField, baseId);
        List<Entity> resultList = (List<Entity>) ctx.environment().listAll(query);

        _beanTable.setSourceObjectKeepSelected(resultList);
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
        ChildListEditPolicy policy = ctx.au().getComponent(ChildListEditPolicy.class, entryEditPolicy);
        List<? extends Entity> entryList = (List<? extends Entity>) getEntryBeanTable().getSourceObject();
        entryList = policy.commitList(Entity.class, new BeanValueListStore(entryList));

        validationErrors = null;
        if (!DataUtils.isBlank(entryList)) {
            if (entryEditPolicy != null) {
                FormMessages messages = policy.validateEntries(
                        (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId, entryList);
                validationErrors = messages != null ? messages.getMessages() : null;
            }

            if (DataUtils.isBlank(validationErrors)) {
                ctx.environment().updateEntryList(sweepingCommitPolicy, entryEditPolicy,
                        (Class<? extends Entity>) entityClassDef.getEntityClass(), baseField, baseId, entryList);
            }
        }

        return DataUtils.isBlank(validationErrors);
    }

}
