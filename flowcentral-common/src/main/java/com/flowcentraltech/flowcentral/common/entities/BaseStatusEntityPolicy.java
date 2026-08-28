/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Base status entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("basestatus-entitypolicy")
public class BaseStatusEntityPolicy<T extends BaseStatusEntity> extends BaseAuditEntityPolicy<T> {

    @Override
    public Object preCreate(T record, Date now) throws UnifyException {
        if (record.getStatus() == null) {
            record.setStatus(RecordStatus.ACTIVE);
        }

        return super.preCreate(record, now);
    }

}
