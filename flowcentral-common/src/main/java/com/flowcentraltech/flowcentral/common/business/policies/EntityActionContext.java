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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.data.Audit;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Entity action context object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityActionContext extends AbstractContext {

    private Entity inst;
    
    private ValueStore instValueStore;

    private String actionPolicyName;

    private FormListingOptions listingOptions;

    private String listingGenerator;

    private SweepingCommitPolicy sweepingCommitPolicy;

    private RecordActionType actionType;

    private Object entityDef;

    private Object result;

    private Audit audit;

    private boolean workflowCopied;
    
    private List<FormMessage> formMessages;

    public EntityActionContext(Object entityDef, Entity inst, String actionPolicyName) {
        this.entityDef = entityDef;
        this.inst = inst;
        this.actionPolicyName = actionPolicyName;
    }

    public EntityActionContext(Object entityDef, Entity inst) {
        this.entityDef = entityDef;
        this.inst = inst;
    }

    public EntityActionContext(Object entityDef, Entity inst, RecordActionType actionType,
            SweepingCommitPolicy sweepingCommitPolicy, String actionPolicyName) {
        this.entityDef = entityDef;
        this.inst = inst;
        this.actionType = actionType;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.actionPolicyName = actionPolicyName;
    }

    public void addFormMessage(MessageType type, String message) {
        if (formMessages == null) {
            formMessages = new ArrayList<FormMessage>();
        }

        formMessages.add(new FormMessage(type, message));
    }

    public List<FormMessage> getFormMessages() {
        return formMessages != null ? formMessages : Collections.emptyList();
    }

    public boolean isWithFormMessages() {
        return formMessages != null && !formMessages .isEmpty();
    }

    @SuppressWarnings("unchecked")
    public <T> T getEntityDef(Class<T> entityDefType) {
        return (T) entityDef;
    }

    public Entity getInst() {
        return inst;
    }

    public ValueStoreReader getReader() {
        if (instValueStore == null) {
            synchronized(this) {
                if (instValueStore == null) {
                    instValueStore = new BeanValueStore(inst);
                }                
            }
        }
        
        return instValueStore.getReader();
    }
    
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public boolean isWorkflowCopied() {
        return workflowCopied;
    }

    public void setWorkflowCopied(boolean workflowCopied) {
        this.workflowCopied = workflowCopied;
    }

    public RecordActionType getActionType() {
        return actionType;
    }

    public FormListingOptions getListingOptions() {
        return listingOptions;
    }

    public void setListingOptions(FormListingOptions listingOptions) {
        this.listingOptions = listingOptions;
    }

    public String getActionPolicyName() {
        return actionPolicyName;
    }

    public boolean isWithActionPolicy() {
        return !StringUtils.isBlank(actionPolicyName);
    }

    public SweepingCommitPolicy getSweepingCommitPolicy() {
        return sweepingCommitPolicy;
    }

    public boolean isWithSweepingCommitPolicy() {
        return sweepingCommitPolicy != null;
    }

    public String getListingGenerator() {
        return listingGenerator;
    }

    public void setListingGenerator(String listingGenerator) {
        this.listingGenerator = listingGenerator;
    }

    public boolean isWithListingGenerator() {
        return !StringUtils.isBlank(listingGenerator);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
