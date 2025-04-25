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
package com.flowcentraltech.flowcentral.audit.web.widgets;

import com.flowcentraltech.flowcentral.application.web.widgets.AbstractSearchEntriesRestrictionResolver;
import com.flowcentraltech.flowcentral.audit.business.AuditModuleService;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for audit search entries restriction
 * resolvers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractAuditSearchEntriesRestrictionResolver extends AbstractSearchEntriesRestrictionResolver {

    @Configurable
    private AuditModuleService auditModuleService;

    protected AuditModuleService audit() {
        return auditModuleService;
    }
}
