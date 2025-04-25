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

package com.flowcentraltech.flowcentral.connect.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.connect.common.constants.RestrictionType;

/**
 * Query definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class QueryDef {

    private List<FilterRestrictionDef> filterRestrictionDefList;

    public QueryDef(List<FilterRestrictionDef> filterRestrictionDefList) {
        this.filterRestrictionDefList = filterRestrictionDefList;
    }

    public FilterRestrictionDef getFilterRestrictionDef(int index) {
        return filterRestrictionDefList.get(index);
    }

    public boolean isBlank() {
        return filterRestrictionDefList == null || filterRestrictionDefList.isEmpty();
    }

    public int size() {
        return filterRestrictionDefList.size();
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<FilterRestrictionDef> filterRestrictionDefList;

        public Builder() {
            this.filterRestrictionDefList = new ArrayList<FilterRestrictionDef>();
        }
        
        public Builder addRestrictionDef(RestrictionType type, String fieldName, String paramA, String paramB,
                int depth) {
            filterRestrictionDefList.add(new FilterRestrictionDef(type, fieldName, paramA, paramB, depth));
            return this;
        }

        public QueryDef build() {
            return new QueryDef(Collections.unmodifiableList(filterRestrictionDefList));
        }
    }

	@Override
	public String toString() {
		return "QueryDef [filterRestrictionDefList=" + filterRestrictionDefList + "]";
	}

}
