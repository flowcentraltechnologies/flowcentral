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

package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.SuggestionTypeDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Suggestion text search.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-suggestiontextsearch")
@UplAttributes({ @UplAttribute(name = "ref", type = String.class, defaultVal = "application.appSuggestionRef"),
        @UplAttribute(name = "searchField", type = String.class, defaultVal = "suggestion"),
        @UplAttribute(name = "type", type = String.class)})
public class SuggestionTextSearchWidget extends EntityTextSearchWidget {

    @Override
    protected void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException {
        String suggestionType = getUplAttribute(String.class, "type");
        if (!StringUtils.isBlank(suggestionType)) {
            SuggestionTypeDef suggestionTypeDef = application().getSuggestionTypeDef(suggestionType);
            ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(suggestionType);
            if (suggestionTypeDef.isWithParent()) {
                ApplicationEntityNameParts pparts = ApplicationNameUtils
                        .getApplicationEntityNameParts(suggestionTypeDef.getParent());
                query.addRestriction(new Or()
                        .add(new And().add(new Equals("applicationName", parts.getApplicationName()))
                                .add(new Equals("suggestionTypeName", parts.getEntityName())))
                        .add(new And().add(new Equals("applicationName", pparts.getApplicationName()))
                                .add(new Equals("suggestionTypeName", pparts.getEntityName()))));
            } else {
                query.addEquals("applicationName", parts.getApplicationName()).addEquals("suggestionTypeName",
                        parts.getEntityName());
            }
            
            if (parts.isWithMinorName()) {
                query.addEquals("fieldName", parts.getMinorName());
            }
        }
    }

}
