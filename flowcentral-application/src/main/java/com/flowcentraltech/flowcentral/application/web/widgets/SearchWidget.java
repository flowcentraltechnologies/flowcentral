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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.control.DynamicField;

/**
 * Search widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-search")
@UplAttributes({@UplAttribute(name = "captionSuffix", type = String.class, defaultVal = ":")})
public class SearchWidget extends AbstractValueListWidget<SearchEntry> {

    private DynamicField paramCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        paramCtrl = (DynamicField) addInternalChildWidget(
                "!ui-dynamic binding:paramInput.value descriptorBinding:paramInput.editor");
    }

    @Action
    public void normalize() throws UnifyException {
        SearchEntries searchEntries = getSearchEntries();
        if (searchEntries != null) {
            searchEntries.normalize();
        }
    }

    public String getCaptionSuffix() throws UnifyException {
        return getUplAttribute(String.class, "captionSuffix");
    }
    
    public DynamicField getParamCtrl() {
        return paramCtrl;
    }

    public SearchEntries getSearchEntries() throws UnifyException {
        return getValue(SearchEntries.class);
    }

    @Override
    protected List<SearchEntry> getItemList() throws UnifyException {
        SearchEntries searchEntries = getSearchEntries();
        if (searchEntries != null) {
            return searchEntries.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(SearchEntry searchEntry, int index) throws UnifyException {
        return createValueStore(searchEntry, index);
    }

}
