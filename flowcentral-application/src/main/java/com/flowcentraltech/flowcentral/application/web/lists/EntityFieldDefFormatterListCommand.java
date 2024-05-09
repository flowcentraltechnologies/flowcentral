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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
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
                    List<ListData> list = Collections.emptyList();
                    switch (fieldType) {
                        case BLOB:
                        case BOOLEAN:
                        case CATEGORY_COLUMN:
                        case CHAR:
                        case CHILD:
                        case CHILD_LIST:
                        case REF_FILEUPLOAD:
                        case CLOB:
                            break;
                        case DECIMAL:
                        case DOUBLE:
                        case FLOAT:
                            list = getListables(
                                    StandardFormatType.DECIMAL,
                                    StandardFormatType.DECIMAL_GROUPED);
                            break;
                        case ENUM:
                        case ENUM_REF:
                        case ENUM_DYN:
                        case FOSTER_PARENT_ID:
                        case FOSTER_PARENT_TYPE:
                            break;
                        case INTEGER:
                        case LONG:
                        case SHORT:
                            list = getListables(
                                    StandardFormatType.INTEGER,
                                    StandardFormatType.INTEGER_GROUPED);
                            break;
                        case TENANT_ID:
                        case MAPPED:
                        case LIST_ONLY:
                        case REF:
                        case REF_UNLINKABLE:
                        case SCRATCH:
                        case STRING:
                            break;
                        case DATE:
                            list = getListables(
                                    StandardFormatType.DATE_DDMMYYYY_SLASH,
                                    StandardFormatType.DATE_MMDDYYYY_SLASH,
                                    StandardFormatType.DATE_YYYYMMDD_SLASH,
                                    StandardFormatType.DATE_DDMMYYYY_DASH,
                                    StandardFormatType.DATE_MMDDYYYY_DASH,
                                    StandardFormatType.DATE_YYYYMMDD_DASH);
                            break;
                        case TIMESTAMP:
                        case TIMESTAMP_UTC:
                            list = getListables(
                                    StandardFormatType.DATETIME_DDMMYYYY_SLASH,
                                    StandardFormatType.DATETIME_MMDDYYYY_SLASH,
                                    StandardFormatType.DATETIME_YYYYMMDD_SLASH,
                                    StandardFormatType.DATETIME_DDMMYYYY_DASH,
                                    StandardFormatType.DATETIME_MMDDYYYY_DASH,
                                    StandardFormatType.DATETIME_YYYYMMDD_DASH);
                            break;
                        default:
                            break;
                    }

                    return list;
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

    private List<ListData> getListables(StandardFormatType... formatTypes) {
        List<ListData> list = new ArrayList<ListData>();
        for (StandardFormatType formatType: formatTypes) {
            list.add(new ListData(formatType.code(), formatType.label()));
        }
        
        return list;
    }

}
