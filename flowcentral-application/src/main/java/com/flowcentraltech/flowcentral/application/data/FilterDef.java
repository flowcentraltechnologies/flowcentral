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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.EntityBasedFilterGenerator;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Filter definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterDef implements Listable {

    private AppletUtilities au;

    private List<FilterRestrictionDef> filterRestrictionDefList;

    private String name;

    private String description;

    private String filterGenerator;

    public FilterDef(FilterDef _filterDef) {
        this.au = _filterDef.au;
        this.filterRestrictionDefList = _filterDef.filterRestrictionDefList;
        this.name = _filterDef.name;
        this.description = _filterDef.description;
        this.filterGenerator = _filterDef.filterGenerator;
    }

    private FilterDef(AppletUtilities au, List<FilterRestrictionDef> filterRestrictionDefList, String name,
            String description, String filterGenerator) {
        this.au = au;
        this.filterRestrictionDefList = filterRestrictionDefList;
        this.name = name;
        this.description = description;
        this.filterGenerator = filterGenerator;
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

    public FilterDef explodeGenerator(EntityDef entityDef, Date now) throws UnifyException {
        if (isWithFilterGenerator()) {
            Restriction restriction = getRestriction(entityDef, null, now);
            return InputWidgetUtils.getFilterDef(au, name, description, restriction);
        }
        
        return this;
    }
    
    public String getFilterGenerator() {
        return filterGenerator;
    }

    public boolean isWithFilterGenerator() {
        return !StringUtils.isBlank(filterGenerator);
    }

    public List<FilterRestrictionDef> getFilterRestrictionDefList() {
        return filterRestrictionDefList;
    }

    public Restriction getRestriction(EntityDef entityDef, ValueStoreReader valueStoreReader, Date now) throws UnifyException {
        if (isWithFilterGenerator()) {
            return au.getComponent(EntityBasedFilterGenerator.class, filterGenerator)
                    .generate(valueStoreReader, null);
        }
        
        return InputWidgetUtils.getRestriction(entityDef, this, au.specialParamProvider(), now);
    }

    public ObjectFilter getObjectFilter(EntityDef entityDef, ValueStoreReader valueStoreReader, Date now) throws UnifyException {
        return new ObjectFilter(getRestriction(entityDef, valueStoreReader, now));
    }

    public boolean isBlank() {
        return filterRestrictionDefList == null || filterRestrictionDefList.isEmpty();
    }

    @Override
    public String toString() {
        return "FilterDef [au=" + au + ", filterRestrictionDefList=" + filterRestrictionDefList + ", name=" + name
                + ", description=" + description + ", filterGenerator=" + filterGenerator + "]";
    }

    public static Builder newBuilder(AppletUtilities au) {
        return new Builder(au);
    }

    public static class Builder {

        private AppletUtilities au;

        private String name;

        private String description;

        private String filterGenerator;

        private List<FilterRestrictionDef> filterRestrictionDefList;

        public Builder(AppletUtilities au) {
            this.au = au;
             this.filterRestrictionDefList = new ArrayList<FilterRestrictionDef>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder filterGenerator(String filterGenerator) {
            this.filterGenerator = filterGenerator;
            return this;
        }

        public Builder addRestrictionDef(FilterConditionType type, String fieldName, String paramA, String paramB,
                int depth) {
            filterRestrictionDefList.add(new FilterRestrictionDef(type, fieldName, paramA, paramB, depth));
            return this;
        }

        public FilterDef build() throws UnifyException {
            return new FilterDef(au, DataUtils.unmodifiableList(filterRestrictionDefList), name, description, filterGenerator);
        }
    }

}
