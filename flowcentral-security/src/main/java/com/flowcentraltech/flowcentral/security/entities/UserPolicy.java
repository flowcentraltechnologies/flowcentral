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

/**
 * User data entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("userpolicy")
public class UserPolicy extends BaseStatusWorkEntityPolicy<User> {

    @Configurable
    private SystemModuleService systemModuleService;

    @Override
    public Object preCreate(User record, Date now) throws UnifyException {
        if (record.getOriginalCopyId() == null) {
            record.setChangePassword(Boolean.TRUE);

            if (record.getPasswordExpires() == null) {
                record.setPasswordExpires(Boolean.TRUE);
            }

            if (record.getLoginLocked() == null) {
                record.setLoginLocked(Boolean.FALSE);
            }

            if (record.getAllowMultipleLogin() == null) {
                record.setAllowMultipleLogin(Boolean.FALSE);
            }

            if (record.getWorkflowStatus() == null) {
                record.setWorkflowStatus(UserWorkflowStatus.NEW);
            }

            calcPasswordExpiryDate(record, now);

            record.setLoginAttempts(Integer.valueOf(0));
            record.setLastLoginDt(null);
        }

        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(User record, Date now) throws UnifyException {
        if (record.getOriginalCopyId() == null) {
            calcPasswordExpiryDate(record, now);
        }

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
