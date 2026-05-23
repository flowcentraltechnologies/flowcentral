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

package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetTypeQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationQueryUtils;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.StringParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Data type editor list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("datatypeeditorlist")
public class DataTypeEditorListCommand extends AbstractApplicationListCommand<StringParam> {
    
    public DataTypeEditorListCommand() {
        super(StringParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam stringParam) throws UnifyException {
        if (stringParam.isPresent()) {
            DataType dataType = DataType.fromCode(stringParam.getValue());
            if (dataType != null) {
                AppWidgetTypeQuery query = new AppWidgetTypeQuery();
                ApplicationQueryUtils.addWidgetTypeCriteria(query, dataType);
                query.addSelect("editor", "description").addOrder("description");
                List<AppWidgetType> widgetTypeList = application().findAppWidgetTypes(query);
                if (!DataUtils.isBlank(widgetTypeList)) {
                    final List<Listable> list = new ArrayList<Listable>();
                    for (AppWidgetType appWidgetType: widgetTypeList) {
                        final String editor = appWidgetType.getEditor();
                        if (editor.indexOf("%s") < 0) {
                            list.add(new ListData(editor, appWidgetType.getDescription()));
                        }
                    }
                    
                    return list;
                }
            }
        }

        return Collections.emptyList();
    }

}
