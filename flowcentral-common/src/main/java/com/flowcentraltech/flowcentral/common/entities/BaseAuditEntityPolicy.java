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

import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;

/**
 * Base audit entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("baseaudit-entitypolicy")
public class BaseAuditEntityPolicy extends BaseVersionEntityPolicy {

    @Override
    public Object preCreate(final Entity record, Date now) throws UnifyException {
        final BaseAuditEntity baseAuditEntity = ((BaseAuditEntity) record);
        baseAuditEntity.setCreateDt(now);
        baseAuditEntity.setUpdateDt(now);

        final String loginId = getUserLoginId();
        if (baseAuditEntity.getCreatedBy() == null) {
            baseAuditEntity.setCreatedBy(loginId);
        }
        
        if (baseAuditEntity.getUpdatedBy() == null) {
            baseAuditEntity.setUpdatedBy(loginId);
        }

        return super.preCreate(baseAuditEntity, now);
    }

    @Override
    public void preUpdate(final Entity record, Date now) throws UnifyException {
        super.preUpdate(record, now);
        final BaseAuditEntity baseAuditEntity = ((BaseAuditEntity) record);
        baseAuditEntity.setUpdateDt(now);

        final String loginId = getUserLoginId();
        baseAuditEntity.setUpdatedBy(loginId);
    }

    private String getUserLoginId() throws UnifyException {
        UserToken userToken = getUserToken();
        return userToken != null && userToken.isWithUserLoginId() ? userToken.getUserLoginId()
                : DefaultApplicationConstants.SYSTEM_LOGINID;
    }
}
