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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.business.EntitySchemaManager;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio on create application entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiooncreateappentity-policy")
public class StudioOnCreateAppEntityPolicy extends StudioOnCreateComponentPolicy {

    @Configurable
    private EntitySchemaManager entitySchemaManager;

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        AppEntity appEntity = (AppEntity) ctx.getInst();
        if (DataUtils.isBlank(appEntity.getFieldList())) {
            appEntity.setFieldList(application().getEntityBaseTypeFieldList(appEntity.getBaseType(), ConfigType.CUSTOM));
        } else {
            for (AppEntityField appEntityField : appEntity.getFieldList()) {
                if (appEntityField.getType().isStatic()) {
                    appEntityField.setType(EntityFieldType.CUSTOM);
                }
            }
        }
        
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = super.doExecutePostAction(ctx);
        final AppEntity appEntity = (AppEntity) ctx.getInst();
        final String applicationName = application().getApplicationName(appEntity.getApplicationId());
        final String entity = ApplicationNameUtils.ensureLongNameReference(applicationName, appEntity.getName());
        entitySchemaManager.createDefaultComponents(entity, appEntity);
        return result;
    }

}
