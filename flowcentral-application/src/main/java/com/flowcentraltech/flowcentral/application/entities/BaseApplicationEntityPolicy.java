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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.DevelopmentVersionType;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntityPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * Base application entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("baseapplication-entitypolicy")
public class BaseApplicationEntityPolicy extends BaseConfigEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        if (isEnterprise()) {
            ((BaseApplicationEntity) record).setDevVersionType(DevelopmentVersionType.NEW);
        } else {
            ((BaseApplicationEntity) record).setDevVersionType(DevelopmentVersionType.CURRENT);
        }
        
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        if (!isEnterprise()) {
            ((BaseApplicationEntity) record).setDevMergeVersionNo(null);
        }

        super.preUpdate(record, now);
    }
}
