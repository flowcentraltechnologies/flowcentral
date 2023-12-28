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
package com.flowcentraltech.flowcentral.audit.business;

import com.flowcentraltech.flowcentral.audit.data.EntityAuditConfigDef;
import com.flowcentraltech.flowcentral.common.business.AuditLogger;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.common.data.FormattedAudit;
import com.tcdng.unify.core.UnifyException;

/**
 * Audit business service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface AuditModuleService extends FlowCentralService, AuditLogger {

    /**
     * Gets entity audit configuration.
     * 
     * @param name
     *             the configuration name
     * @return the configuration definition
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityAuditConfigDef getEntityAuditConfigDef(String name) throws UnifyException;

    /**
     * Gets snapshot formatted audit by entity audit keys.
     * 
     * @param entityAuditKeysId
     *                          the entity audit keys ID
     * @return the formatted audit
     * @throws UnifyException
     *                        if an error occurs
     */
    FormattedAudit getSnapshotFormattedAuditByEntityAuditKeys(Long entityAuditKeysId) throws UnifyException;
}
