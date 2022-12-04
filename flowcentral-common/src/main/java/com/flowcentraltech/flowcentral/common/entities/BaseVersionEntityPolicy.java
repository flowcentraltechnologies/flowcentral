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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * Base version entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("baseversion-entitypolicy")
public class BaseVersionEntityPolicy extends BaseEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        BaseVersionEntity baseVersionEntity = ((BaseVersionEntity) record);
        baseVersionEntity.setVersionNo(1L);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        super.preUpdate(record, now);
        final BaseVersionEntity baseVersionEntity = ((BaseVersionEntity) record);
        baseVersionEntity.setVersionNo(baseVersionEntity.getVersionNo() + 1L);
    }

    @Override
    public void onUpdateError(final Entity record) {
        final BaseVersionEntity baseVersionEntity = (BaseVersionEntity) record;
        baseVersionEntity.setVersionNo(baseVersionEntity.getVersionNo() - 1L);
    }

}
