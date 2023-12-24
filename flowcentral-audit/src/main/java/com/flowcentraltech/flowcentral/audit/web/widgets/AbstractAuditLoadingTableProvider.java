/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.audit.web.widgets;

import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.audit.business.AuditModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Convenient abstract base class for audit loading table providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAuditLoadingTableProvider extends AbstractApplicationLoadingTableProvider {

    @Configurable
    private AuditModuleService auditModuleService;
    
    public final void setAuditModuleService(AuditModuleService auditModuleService) {
        this.auditModuleService = auditModuleService;
    }

    @Override
    public int getSourceItemOptions(Entity loadingEntity) throws UnifyException {
        return 0;
    }

    @Override
    public String getSourceItemFormApplet() throws UnifyException {
        return null;
    }

    @Override
    public void commitChange(ValueStore itemValueStore, boolean listing) throws UnifyException {

    }

    protected AuditModuleService audit() {
        return auditModuleService;
    }
}
