/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Calendar;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkEntityPolicy;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleSysParamConstants;
import com.flowcentraltech.flowcentral.security.constants.UserWorkflowStatus;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;

/**
 * User data entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("userpolicy")
public class UserPolicy extends BaseStatusWorkEntityPolicy {

    @Configurable
    private SystemModuleService systemModuleService;

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        User user = (User) record;
        user.setChangePassword(Boolean.TRUE);

        if (user.getPasswordExpires() == null) {
            user.setPasswordExpires(Boolean.TRUE);
        }

        if (user.getLoginLocked() == null) {
            user.setLoginLocked(Boolean.FALSE);
        }

        if (user.getAllowMultipleLogin() == null) {
            user.setAllowMultipleLogin(Boolean.FALSE);
        }

        if (user.getWorkflowStatus() == null) {
            user.setWorkflowStatus(UserWorkflowStatus.NEW);
        }
        
        calcPasswordExpiryDate(user, now);

        user.setLoginAttempts(Integer.valueOf(0));
        user.setLastLoginDt(null);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        calcPasswordExpiryDate((User) record, now);
        super.preUpdate(record, now);
    }

    private void calcPasswordExpiryDate(User user, Date now) throws UnifyException {
        UserToken userToken = getUserToken();
        if (userToken != null) {
            if (user.getPasswordExpires() && user.getPasswordExpiryDt() == null && systemModuleService
                    .getSysParameterValue(boolean.class, SecurityModuleSysParamConstants.ENABLE_PASSWORD_EXPIRY)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DAY_OF_YEAR, systemModuleService.getSysParameterValue(int.class,
                        SecurityModuleSysParamConstants.PASSWORD_EXPIRY_DAYS));
                user.setPasswordExpiryDt(cal.getTime());
                return;
            }
        }

        user.setPasswordExpiryDt(null);
    }
}
