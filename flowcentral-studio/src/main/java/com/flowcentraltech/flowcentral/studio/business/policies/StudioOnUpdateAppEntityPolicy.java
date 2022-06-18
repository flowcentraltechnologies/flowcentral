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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Query;

/**
 * Studio on update application entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioonupdateappentity-policy")
public class StudioOnUpdateAppEntityPolicy extends StudioOnCreateComponentPolicy {

    @Override
    protected void doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        AppEntity appEntity = (AppEntity) ctx.getInst();
        // Update base fields if necessary
        EntityBaseType currentBaseType = environment().value(EntityBaseType.class, "baseType",
                Query.of(AppEntity.class).addEquals("id", appEntity.getId()));
        if (!currentBaseType.equals(appEntity.getBaseType())) {
            // Delete old base fields
            final Long appEntityId = appEntity.getId();
            environment().deleteAll(Query.of(AppEntityField.class).addEquals("type", EntityFieldType.BASE)
                    .addEquals("appEntityId", appEntityId));

            // Add new base fields
            List<AppEntityField> newBaseFieldList = application().getEntityBaseTypeFieldList(appEntity.getBaseType(),
                    ConfigType.CUSTOM);
            for (AppEntityField appEntityField : newBaseFieldList) {
                appEntityField.setAppEntityId(appEntityId);
                environment().create(appEntityField);
            }
        }
    }

}
