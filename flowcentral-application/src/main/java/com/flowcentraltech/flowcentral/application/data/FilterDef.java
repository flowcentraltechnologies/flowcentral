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

import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.configuration.constants.ChildListActionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.CriteriaUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Filter definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterDef implements Listable {

    private List<FilterRestrictionDef> filterRestrictionDefList;

    private Restriction restriction;

    private ObjectFilter objectFilter;

    private String name;

    private String description;

    private String preferredForm;

    private String preferredChildListApplet;
    
    private ChildListActionType childListActionType;
   
    public FilterDef(FilterDef _filterDef) {
        this.filterRestrictionDefList = _filterDef.filterRestrictionDefList;
        this.name = _filterDef.name;
        this.description = _filterDef.description;
        this.preferredForm = _filterDef.preferredForm;
        this.preferredChildListApplet = _filterDef.preferredChildListApplet;
        this.childListActionType = _filterDef.childListActionType;
    }

    private FilterDef(List<FilterRestrictionDef> filterRestrictionDefList, String name, String description,
            String preferredForm, String preferredChildListApplet, ChildListActionType childListActionType) {
        this.filterRestrictionDefList = filterRestrictionDefList;
        this.name = name;
        this.description = description;
        this.preferredForm = preferredForm;
        this.preferredChildListApplet = preferredChildListApplet;
        this.childListActionType = childListActionType;
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

    public String getPreferredForm() {
        return preferredForm;
    }

    public String getPreferredChildListApplet() {
        return preferredChildListApplet;
    }

    public ChildListActionType getChildListActionType() {
        return childListActionType;
    }

    public boolean isShowPopupChildListAction() {
        return childListActionType != null && childListActionType.isShowPopup();
    }
    
    public boolean isShowMultiSelectChildListAction() {
        return ChildListActionType.SHOW_MULTISELECT.equals(childListActionType);
    }

    public boolean isShowTreeMultiSelectChildListAction() {
        return ChildListActionType.SHOW_TREEMULTISELECT.equals(childListActionType);
    }

    public boolean isHideAddWidgetChildListAction() {
        return ChildListActionType.HIDE_ADDWIDGET.equals(childListActionType);
    }
    
    public boolean isWithPreferredForm() {
        return !StringUtils.isBlank(preferredForm);
    }

    public boolean isWithPreferredChildListApplet() {
        return !StringUtils.isBlank(preferredChildListApplet);
    }

    public List<FilterRestrictionDef> getFilterRestrictionDefList() {
        return filterRestrictionDefList;
    }

    public Restriction getRestriction(EntityDef entityDef, SpecialParamProvider specialParamProvider, Date now)
            throws UnifyException {
        if (restriction == null) {
            synchronized (filterRestrictionDefList) {
                if (restriction == null) {
                    restriction = CriteriaUtils.unmodifiableRestriction(
                            InputWidgetUtils.getRestriction(entityDef, this, specialParamProvider, now));
                }
            }
        }

        return restriction;
    }

    public ObjectFilter getObjectFilter(EntityDef entityDef, SpecialParamProvider specialParamProvider, Date now)
            throws UnifyException {
        if (objectFilter == null) {
            synchronized (filterRestrictionDefList) {
                if (objectFilter == null) {
                    objectFilter = new ObjectFilter(getRestriction(entityDef, specialParamProvider, now));
                }
            }
        }

        return objectFilter;
    }

    public boolean isBlank() {
        return filterRestrictionDefList == null || filterRestrictionDefList.isEmpty();
    }

    @Override
    public String toString() {
        return "FilterDef [filterRestrictionDefList=" + filterRestrictionDefList + ", restriction=" + restriction
                + ", objectFilter=" + objectFilter + ", name=" + name + ", description=" + description
                + ", preferredForm=" + preferredForm + ", preferredChildListApplet=" + preferredChildListApplet + "]";
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private String description;

        private String preferredForm;

        private String preferredChildListApplet;
        
        private ChildListActionType childListActionType;
        
        private List<FilterRestrictionDef> filterRestrictionDefList;

        public Builder() {
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

        public Builder preferredForm(String preferredForm) {
            this.preferredForm = preferredForm;
            return this;
        }

        public Builder preferredChildListApplet(String preferredChildListApplet) {
            this.preferredChildListApplet = preferredChildListApplet;
            return this;
        }

        public Builder childListActionType(ChildListActionType childListActionType) {
            this.childListActionType = childListActionType;
            return this;
        }

        public Builder addRestrictionDef(FilterConditionType type, String fieldName, String paramA, String paramB,
                int depth) {
            filterRestrictionDefList.add(new FilterRestrictionDef(type, fieldName, paramA, paramB, depth));
            return this;
        }

        public FilterDef build() throws UnifyException {
            return new FilterDef(DataUtils.unmodifiableList(filterRestrictionDefList), name, description,
                    preferredForm, preferredChildListApplet, childListActionType);
        }
    }

}
