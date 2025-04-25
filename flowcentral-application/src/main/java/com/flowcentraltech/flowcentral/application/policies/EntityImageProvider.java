/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.policies;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.util.EntityImage;
import com.flowcentraltech.flowcentral.application.util.EntityImageUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.resource.AbstractImageProvider;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Entity image provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(ApplicationModuleNameConstants.ENTITY_IMAGE_PROVIDER)
public class EntityImageProvider extends AbstractImageProvider {

    @Configurable
    private AppletUtilities appletUtilities;

    @SuppressWarnings("unchecked")
    @Override
    protected byte[] doProvideAsByteArray(String encoded) throws UnifyException {
        EntityImage image = EntityImageUtils.decode(encoded);
        if (image.isPresent()) {
            EntityClassDef entityClassDef = appletUtilities.getEntityClassDef(image.getEntity());
            return appletUtilities.environment().value(byte[].class, image.getFieldName(), Query
                    .of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id", image.getInstId()));
        } else if (image.isNullInstance()) {
            return DataUtils.ZEROLEN_BYTE_ARRAY;
        }
        
        return null;
    }

}
