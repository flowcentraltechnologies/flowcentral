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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.common.data.Listable;

/**
 * Entity search input definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntitySearchInputDef implements Listable {

    private SearchInputsDef searchInputsDef;

    public EntitySearchInputDef(SearchInputsDef searchInputsDef) {
        this.searchInputsDef = searchInputsDef;
    }

    @Override
    public String getListDescription() {
        return searchInputsDef.getListDescription();
    }

    @Override
    public String getListKey() {
        return searchInputsDef.getListKey();
    }

    public SearchInputsDef getSearchInputsDef() {
        return searchInputsDef;
    }
    
    public String getName() {
        return searchInputsDef.getName();
    }

    public String getRestrictionResolverName() {
        return searchInputsDef.getRestrictionResolverName();
    }

}
