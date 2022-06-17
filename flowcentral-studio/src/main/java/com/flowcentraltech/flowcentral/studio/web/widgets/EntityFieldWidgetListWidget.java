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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityListWidget;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.studio.util.StudioQueryUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Entity field widget list widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entityfieldwidgetlist")
@UplAttributes({ @UplAttribute(name = "typeField", type = String.class, mandatory = true),
        @UplAttribute(name = "listKey", type = String.class, defaultVal = "longName"),
        @UplAttribute(name = "ref", type = String[].class, defaultVal = "application.appWidgetTypeRef") })
public class EntityFieldWidgetListWidget extends EntityListWidget {

    @Override
    protected void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException {
        super.addMoreResultRestriction(query);
        EntityFieldDataType type = getValue(EntityFieldDataType.class, getUplAttribute(String.class, "typeField"));
        if (EntityFieldDataType.LIST_ONLY.equals(type)) {
            AppEntityField appEntityField = (AppEntityField) getValueStore().getValueObject();
            type = application().resolveListOnlyEntityDataType(appEntityField);
        }

        StudioQueryUtils.addWidgetTypeCriteria(query, type);
    }

}
