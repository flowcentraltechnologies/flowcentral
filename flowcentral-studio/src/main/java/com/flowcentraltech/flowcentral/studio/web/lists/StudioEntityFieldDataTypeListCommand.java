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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.list.ListManager;

/**
 * Studio entity field data type list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studioentityfielddatatypelist")
public class StudioEntityFieldDataTypeListCommand extends AbstractApplicationListCommand<EntityFieldTypeParams> {

    @Configurable
    private ListManager listManager;

    private List<EntityFieldDataType> supportedTypeList = Collections
            .unmodifiableList(Arrays.asList(EntityFieldDataType.REF, EntityFieldDataType.REF_UNLINKABLE,
                    EntityFieldDataType.REF_FILEUPLOAD, EntityFieldDataType.ENUM_REF, EntityFieldDataType.ENUM,
                    EntityFieldDataType.ENUM_DYN, EntityFieldDataType.LIST_ONLY, EntityFieldDataType.CHILD,
                    EntityFieldDataType.CHILD_LIST, EntityFieldDataType.STRING, EntityFieldDataType.STRING_ARRAY,
                    EntityFieldDataType.CHAR, EntityFieldDataType.BOOLEAN, EntityFieldDataType.BOOLEAN_ARRAY,
                    EntityFieldDataType.LONG, EntityFieldDataType.LONG_ARRAY, EntityFieldDataType.INTEGER,
                    EntityFieldDataType.INTEGER_ARRAY, EntityFieldDataType.SHORT, EntityFieldDataType.SHORT_ARRAY,
                    EntityFieldDataType.DECIMAL, EntityFieldDataType.DECIMAL_ARRAY, EntityFieldDataType.DOUBLE,
                    EntityFieldDataType.DOUBLE_ARRAY, EntityFieldDataType.FLOAT, EntityFieldDataType.FLOAT_ARRAY,
                    EntityFieldDataType.DATE, EntityFieldDataType.DATE_ARRAY, EntityFieldDataType.TIMESTAMP,
                    EntityFieldDataType.TIMESTAMP_UTC, EntityFieldDataType.CLOB, EntityFieldDataType.BLOB));

    private LocaleFactoryMap<List<Listable>> listFactory;

    public StudioEntityFieldDataTypeListCommand() {
        super(EntityFieldTypeParams.class);
        listFactory = new LocaleFactoryMap<List<Listable>>()
            {

                @Override
                protected List<Listable> create(Locale locale, Object... arg1) throws Exception {
                    List<Listable> list = new ArrayList<Listable>();
                    Map<String, Listable> map = listManager.getListMap(locale, "entityfielddatatypelist");
                    for (EntityFieldDataType type : supportedTypeList) {
                        list.add(map.get(type.code()));
                    }

                    return Collections.unmodifiableList(list);
                }

            };
    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityFieldTypeParams params) throws UnifyException {
        if (params.isPresent()) {
            if (params.getType().isCustom()) {
                return listFactory.get(locale);
            }
        }

        return listManager.getList(locale, "entityfielddatatypelist");
    }

}
