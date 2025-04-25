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

package com.flowcentraltech.flowcentral.common.entities;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Base class for work entity with status.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Policy("basestatuswork-entitypolicy")
public abstract class BaseStatusWorkEntity extends BaseWorkEntity {

    @ForeignKey(name = "REC_ST")
    private RecordStatus status;

    @ListOnly(key = "status", property = "description")
    private String statusDesc;

    public final RecordStatus getStatus() {
        return status;
    }

    public final void setStatus(RecordStatus status) {
        this.status = status;
    }

    public final String getStatusDesc() {
        return statusDesc;
    }

    public final void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
