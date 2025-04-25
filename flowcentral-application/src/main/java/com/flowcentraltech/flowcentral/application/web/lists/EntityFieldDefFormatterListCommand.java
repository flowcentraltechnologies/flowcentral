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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.util.DataTypeUtils;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.LocaleFactoryMaps;

/**
 * Entity field definition formatter list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("entityfielddefformatterlist")
public class EntityFieldDefFormatterListCommand extends AbstractFlowCentralListCommand<EntityFieldDefListParams> {

    private final LocaleFactoryMaps<EntityFieldDataType, List<? extends Listable>> listMap;

    public EntityFieldDefFormatterListCommand() {
        super(EntityFieldDefListParams.class);
        listMap = new LocaleFactoryMaps<EntityFieldDataType, List<? extends Listable>>()
            { 
                @Override
                protected List<? extends Listable> createObject(Locale locale, EntityFieldDataType fieldType,
                        Object... params) throws Exception {
                    return DataTypeUtils.getFormatterList(fieldType);
                }

            };

    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityFieldDefListParams params) throws UnifyException {
        if (params.isPresent()) {
            EntityFieldDataType fieldType = params.getEntityDef().getFieldDef(params.getFieldName()).getDataType();
            return listMap.get(locale, fieldType);
        }

        return Collections.emptyList();
    }

}
