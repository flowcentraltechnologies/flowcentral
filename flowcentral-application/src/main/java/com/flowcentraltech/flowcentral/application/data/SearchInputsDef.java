/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Search inputs definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchInputsDef implements Listable {

    private List<SearchInputDef> searchInputDefList;

    private String name;

    private String description;

    private SearchInputsDef(List<SearchInputDef> searchInputDefList, String name, String description) {
        this.searchInputDefList = searchInputDefList;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<SearchInputDef> getSearchInputDefList() {
        return searchInputDefList;
    }

    public boolean isBlank() {
        return searchInputDefList == null || searchInputDefList.isEmpty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private String description;

        private List<SearchInputDef> searchInputDefList;

        public Builder() {
            this.searchInputDefList = new ArrayList<SearchInputDef>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addSearchInputDef(SearchConditionType type, String fieldName, String widget, String label) {
            searchInputDefList.add(new SearchInputDef(type, fieldName, widget, label));
            return this;
        }

        public SearchInputsDef build() throws UnifyException {
            return new SearchInputsDef(DataUtils.unmodifiableList(searchInputDefList), name, description);
        }
    }

}
