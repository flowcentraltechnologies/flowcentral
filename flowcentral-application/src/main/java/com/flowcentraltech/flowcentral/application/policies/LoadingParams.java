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
package com.flowcentraltech.flowcentral.application.policies;

import java.util.Collections;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntries;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Loading parameters..
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class LoadingParams {

    private Restriction restriction;
    
    private Map<String, Object> values;

    public LoadingParams(SearchEntries.Entries entries) {
        this.restriction = entries.getRestriction();
        this.values = entries.getInputs();
    }

    public LoadingParams(Restriction restriction) {
        this.restriction = restriction;
        this.values = Collections.emptyMap();
    }

    public LoadingParams(Map<String, Object> values) {
        this.restriction = null;
        this.values = values;
    }

    public LoadingParams(Restriction restriction, Map<String, Object> values) {
        this.restriction = restriction;
        this.values = values;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public boolean isWithRestriction() {
        return restriction != null;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public <T> T getParam(Class<T> dataType, String name) throws UnifyException {
        return DataUtils.convert(dataType, values.get(name));
    }
    
    public void restore() {
        if (restriction != null) {
            restriction.reverseFieldSwap();
        }
    }
}
