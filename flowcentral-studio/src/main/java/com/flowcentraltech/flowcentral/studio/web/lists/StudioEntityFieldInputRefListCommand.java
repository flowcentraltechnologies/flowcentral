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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppRefQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio entity field input reference list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioentityfieldinputreflist")
public class StudioEntityFieldInputRefListCommand extends AbstractApplicationListCommand<StudioEntityFieldFormParams> {

    public StudioEntityFieldInputRefListCommand() {
        super(StudioEntityFieldFormParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StudioEntityFieldFormParams params) throws UnifyException {
        if (params.isPresent()) {
            EntityFieldDataType type = DataUtils.convert(EntityFieldDataType.class, params.getDataType());
            if (type.isForeignKey() && !type.isEnumDataType()) {
                ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(params.getEntity());
                AppEntityField appEntityField = application().findAppEntityField(
                        (AppEntityFieldQuery) new AppEntityFieldQuery().applicationName(np.getApplicationName())
                                .appEntityName(np.getEntityName()).name(params.getName()));
                RefDef _refDef = application().getRefDef(appEntityField.getReferences());
                AppRefQuery query = new AppRefQuery();
                query.entity(_refDef.getEntity());
                return au().getApplicationEntitiesListables(query);
            }
        }

        return Collections.emptyList();
    }

}
