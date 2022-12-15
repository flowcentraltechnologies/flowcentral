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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntitySearchInputDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputDef;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.ILike;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Search entries object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchEntries {

    private final EntityDef entityDef;

    private final LabelSuggestionDef labelSuggestion;

    private final String searchConfigName;

    private final int columns;

    private List<SearchEntry> entryList;

    public SearchEntries(EntityDef entityDef, LabelSuggestionDef labelSuggestion, String searchConfigName,
            int columns) {
        this.entityDef = entityDef;
        this.labelSuggestion = labelSuggestion;
        this.searchConfigName = searchConfigName;
        this.columns = columns;
    }

    public SearchEntries(EntityDef entityDef, String searchConfigName, int columns) {
        this.entityDef = entityDef;
        this.labelSuggestion = null;
        this.searchConfigName = searchConfigName;
        this.columns = columns;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public List<SearchEntry> getEntryList() {
        return entryList;
    }

    public SearchEntry getEntry(int index) {
        return entryList.get(index);
    }

    public void clear() {
        if (entryList != null) {
            for (SearchEntry searchEntry : entryList) {
                searchEntry.getParamInput().setValue(null);
            }
        }
    }

    public int size() {
        return entryList.size();
    }

    public int getColumns() {
        return columns;
    }

    public Restriction getRestriction() throws UnifyException {
        if (entryList != null) {
            And and = new And();
            for (SearchEntry searchEntry : entryList) {
                Object val = searchEntry.getParamInput().getValue();
                if (val != null) {
                    // TODO
                    EntityFieldDef entityFieldDef = searchEntry.getEntityFieldDef();
                    EntityFieldDataType dataType = entityFieldDef.isWithResolvedTypeFieldDef()
                            ? entityFieldDef.getResolvedTypeFieldDef().getDataType()
                            : entityFieldDef.getDataType();
                    if (EntityFieldDataType.STRING.equals(dataType)) {
                        and.add(new ILike(entityFieldDef.getFieldName(), val));
                    } else {
                        and.add(new Equals(entityFieldDef.getFieldName(), val));
                    }
                }
            }

            if (!and.isEmpty()) {
                return and;
            }
        }

        return null;
    }

    public void normalize() throws UnifyException {
        if (entryList == null) {
            entryList = new ArrayList<SearchEntry>();
            if (!StringUtils.isBlank(searchConfigName)) {
                EntitySearchInputDef entitySearchInputDef = entityDef.getEntitySearchInputDef(searchConfigName);
                for (SearchInputDef searchInputDef : entitySearchInputDef.getSearchInputsDef()
                        .getSearchInputDefList()) {
                    if (searchInputDef.getFieldName().startsWith("f:")) {
                        // Field
                        final String fieldName = searchInputDef.getFieldName().substring(2);
                        SearchEntry searchEntry = new SearchEntry(entityDef, searchInputDef.getLabel(), fieldName,
                                searchInputDef.getType());
                        searchEntry.normalize(searchInputDef.getWidget());
                        entryList.add(searchEntry);
                    } else {
                        // Generator
                    }
                }
            } else {
                for (EntityFieldDef entityFieldDef : entityDef.getBasicSearchFieldDefList()) {
                    String label = getLabelSuggestion(entityFieldDef.getFieldName());
                    label = label != null ? label
                            : (entityFieldDef.isWithInputLabel() ? entityFieldDef.getInputLabel()
                                    : entityFieldDef.getFieldLabel());
                    EntityFieldDataType dataType = entityFieldDef.isWithResolvedTypeFieldDef()
                            ? entityFieldDef.getResolvedTypeFieldDef().getDataType()
                            : entityFieldDef.getDataType();
                    SearchConditionType conditionType = EntityFieldDataType.STRING.equals(dataType)
                            ? SearchConditionType.ILIKE
                            : SearchConditionType.EQUALS;
                    SearchEntry searchEntry = new SearchEntry(entityDef, label, entityFieldDef.getFieldName(),
                            conditionType);
                    searchEntry.normalize();
                    entryList.add(searchEntry);
                }
            }
        }
    }

    private String getLabelSuggestion(String fieldName) {
        return labelSuggestion != null ? labelSuggestion.getSuggestedLabel(fieldName) : null;
    }

}
