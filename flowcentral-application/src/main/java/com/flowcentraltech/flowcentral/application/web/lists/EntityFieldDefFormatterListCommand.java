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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.LocaleFactoryMaps;
import com.tcdng.unify.core.list.AbstractListCommand;

/**
 * Entity field definition formatter list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("entityfielddefformatterlist")
public class EntityFieldDefFormatterListCommand extends AbstractListCommand<EntityFieldDefListParams> {

    private final LocaleFactoryMaps<EntityFieldDataType, List<? extends Listable>> listMap;

    public EntityFieldDefFormatterListCommand() {
        super(EntityFieldDefListParams.class);
        listMap = new LocaleFactoryMaps<EntityFieldDataType, List<? extends Listable>>() {

            @Override
            protected List<? extends Listable> createObject(Locale locale, EntityFieldDataType fieldType, Object... params)
                    throws Exception {
                List<ListData> list = Collections.emptyList();
                switch(fieldType) {
                    case BLOB:
                    case BOOLEAN:
                    case CATEGORY_COLUMN:
                    case CHAR:
                    case CHILD:
                    case CHILD_LIST:
                    case REF_FILEUPLOAD:
                    case CLOB:
                    case DECIMAL:
                    case DOUBLE:
                    case ENUM:
                    case ENUM_REF:
                    case FLOAT:
                    case FOSTER_PARENT_ID:
                    case FOSTER_PARENT_TYPE:
                    case INTEGER:
                    case LIST_ONLY:
                    case LONG:
                    case REF:
                    case REF_UNLINKABLE:
                    case SCRATCH:
                    case SHORT:
                    case STRING:
                        break;
                    case DATE:
                        list = Arrays.asList(new ListData("!fixeddatetimeformat pattern:$s{dd/MM/yyyy}", "dd/MM/yyyy"),
                        new ListData("!fixeddatetimeformat pattern:$s{MM/dd/yyyy}", "MM/dd/yyyy"),
                        new ListData("!fixeddatetimeformat pattern:$s{yyyy/MM/dd}", "yyyy/MM/dd"),
                        new ListData("!fixeddatetimeformat pattern:$s{dd-MM-yyyy}", "dd-MM-yyyy"),
                        new ListData("!fixeddatetimeformat pattern:$s{MM-dd-yyyy}", "MM-dd-yyyy"),
                        new ListData("!fixeddatetimeformat pattern:$s{yyyy-MM-dd}", "yyyy-MM-dd"));
                        break;
                    case TIMESTAMP:
                    case TIMESTAMP_UTC:
                        list = new ArrayList<ListData>();
                        list.add(new ListData("!fixeddatetimeformat pattern:$s{dd/MM/yyyy HH:mm:ss}", "dd/MM/yyyy HH:mm:ss"));
                        list.add(new ListData("!fixeddatetimeformat pattern:$s{MM/dd/yyyy HH:mm:ss}", "MM/dd/yyyy HH:mm:ss"));
                        list.add(new ListData("!fixeddatetimeformat pattern:$s{yyyy/MM/dd HH:mm:ss}", "yyyy/MM/dd HH:mm:ss"));
                        list.add(new ListData("!fixeddatetimeformat pattern:$s{dd-MM-yyyy HH:mm:ss}", "dd-MM-yyyy HH:mm:ss"));
                        list.add(new ListData("!fixeddatetimeformat pattern:$s{MM-dd-yyyy HH:mm:ss}", "MM-dd-yyyy HH:mm:ss"));
                        list.add(new ListData("!fixeddatetimeformat pattern:$s{yyyy-MM-dd HH:mm:ss}", "yyyy-MM-dd HH:mm:ss"));
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

}
