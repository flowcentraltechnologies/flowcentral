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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.core.data.Audit;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity action context object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityActionContext extends AbstractContext {

    private Entity inst;

    private String actionPolicyName;

    private SweepingCommitPolicy sweepingCommitPolicy;

    private RecordActionType actionType;

    private Object entityDef;
    
    private Object result;

    private Audit audit;
    
    public EntityActionContext(Object entityDef, Entity inst, String actionPolicyName) {
        this.entityDef = entityDef;
        this.inst = inst;
        this.actionPolicyName = actionPolicyName;
    }

    public EntityActionContext(Object entityDef, Entity inst, RecordActionType actionType, SweepingCommitPolicy sweepingCommitPolicy,
            String actionPolicyName) {
        this.entityDef = entityDef;
        this.inst = inst;
        this.actionType = actionType;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.actionPolicyName = actionPolicyName;
    }

    public Object getEntityDef() {
        return entityDef;
    }

    public Entity getInst() {
        return inst;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public RecordActionType getActionType() {
        return actionType;
    }

    public String getActionPolicyName() {
        return actionPolicyName;
    }

    public boolean isWithActionPolicy() {
        return actionPolicyName != null;
    }

    public SweepingCommitPolicy getSweepingCommitPolicy() {
        return sweepingCommitPolicy;
    }

    public boolean isWithSweepingCommitPolicy() {
        return sweepingCommitPolicy != null;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
