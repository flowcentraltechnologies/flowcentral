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

package com.flowcentraltech.flowcentral.common.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.AuditSnapshot;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;

/**
 * Audit logger.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface AuditLogger extends FlowCentralComponent {

    /**
     * Checks if audit logger supports entity logging
     * 
     * @param sourceType
     *                   the source type
     * @param entity
     *                   the entity name
     * @return true if entity supported otherwise false
     */
    boolean supportsAuditLog(AuditSourceType sourceType, String entity);

    /**
     * Logs an audit snapshot.
     * 
     * @param auditSnapshot
     *                      the audit snapshot to log
     */
    void log(AuditSnapshot auditSnapshot);

}
