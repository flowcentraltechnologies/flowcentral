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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Studio on delete application applet policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "application.appApplet" })
@Component("studioondeleteappapplet-policy")
public class StudioOnDeleteAppAppletPolicy extends AbstractStudioAppletActionPolicy {

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        final AppApplet appApplet = (AppApplet) ctx.getInst();
        final String appletName = ApplicationNameUtils.getApplicationEntityLongName(appApplet.getApplicationName(),
                appApplet.getName());
        unregisterPrivilege(appApplet.getApplicationId(),
                ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                PrivilegeNameUtils.getAppletPrivilegeName(appletName));
        // Delete facade applets
        List<AppApplet> facadeAppletList = environment()
                .listAll(new AppAppletQuery().routeToApplet(appletName).addSelect("name", "applicationName"));
        for (AppApplet facadeApplet : facadeAppletList) {
            unregisterPrivilege(appApplet.getApplicationId(),
                    ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                    PrivilegeNameUtils.getAppletPrivilegeName(ApplicationNameUtils
                            .getApplicationEntityLongName(facadeApplet.getApplicationName(), facadeApplet.getName())));
        }
        environment().deleteAll(new AppAppletQuery().routeToApplet(appletName));
        return new EntityActionResult(ctx);
    }

    @Override
    public boolean checkAppliesTo(ValueStoreReader reader) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

}
