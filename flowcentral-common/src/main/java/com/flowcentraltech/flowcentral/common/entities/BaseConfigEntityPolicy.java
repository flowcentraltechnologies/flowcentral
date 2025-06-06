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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Base configuration entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("baseconfig-entitypolicy")
public class BaseConfigEntityPolicy extends BaseAuditEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        ConfigUtils.preCreate((BaseConfigEntity) record);
        return super.preCreate(record, now);
    }

    @Override
    public void preDelete(Entity record, Date now) throws UnifyException {
        ConfigUtils.preDelete((BaseConfigEntity) record);
        super.preDelete(record, now);
    }
}
