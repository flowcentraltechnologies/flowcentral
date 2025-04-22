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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AdditionalDataTypeEditorProvider;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.StringParam;

/**
 * Data type editor list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("datatypeeditorlist")
public class DataTypeEditorListCommand extends AbstractApplicationListCommand<StringParam> {

    private final Map<DataType, List<Listable>> editors;

    @Configurable
    private AdditionalDataTypeEditorProvider additionalProvider;
    
    public DataTypeEditorListCommand() {
        super(StringParam.class);
        this.editors = new HashMap<DataType, List<Listable>>();
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam stringParam) throws UnifyException {
        if (stringParam.isPresent()) {
            DataType dataType = DataType.fromCode(stringParam.getValue());
            if (dataType != null) {
                List<Listable> list = editors.get(dataType);
                if (list == null) {
                    synchronized (this) {
                        if (list == null) {
                            list = new ArrayList<>();
                            switch (dataType) {
                                case BLOB:
                                    break;
                                case BOOLEAN:
                                    list.add(new ListData("!ui-checkbox", "Check Box"));
                                    list.add(new ListData("!ui-select list:booleanlist blankOption:$m{blank.none}",
                                            "Boolean List"));
                                    break;
                                case CHAR:
                                    list.add(new ListData("!ui-text maxLen:1", "Character Field"));
                                    break;
                                case CLOB:
                                    break;
                                case DATE:
                                    list.add(new ListData(
                                            "!ui-date formatter:$d{!fixeddatetimeformat pattern:$s{yyyy-MM-dd}} clearable:true",
                                            "Calendar Field"));
                                    break;
                                case DECIMAL:
                                case DOUBLE:
                                case FLOAT:
                                    list.add(new ListData("!ui-decimal", "Decimal Field"));
                                    break;
                                case INTEGER:
                                case LONG:
                                case SHORT:
                                    list.add(new ListData("!ui-integer", "Integer Field"));
                                    break;
                                case STRING:
                                    list.add(new ListData("!ui-text", "Text Field"));
                                    list.add(new ListData("!ui-integertext", "Integer Text Field"));
                                    list.add(new ListData("!ui-alphanumeric", "Alphanumeric Text Field"));
                                    list.add(new ListData("!ui-name", "Name Field"));
                                    break;
                                case TIMESTAMP:
                                case TIMESTAMP_UTC:
                                    list.add(new ListData("!ui-datetime clearable:true", "Datetime Field"));
                                    break;
                                default:
                                    break;
                            }

                            if (additionalProvider != null) {
                                list.addAll(additionalProvider.provide(locale, dataType));
                             }
                            
                            editors.put(dataType, Collections.unmodifiableList(list));
                        }
                    }
                }
                
                return list;
            }
        }

        return Collections.emptyList();
    }

}
