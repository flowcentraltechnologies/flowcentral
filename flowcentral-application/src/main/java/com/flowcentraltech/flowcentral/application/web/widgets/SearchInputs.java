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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;

/**
 * Search inputs object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchInputs {

    private EntityDef entityDef;

    private List<SearchInputEntry> entryList;

    private List<SearchInputEntry> viewEntryList;

    public SearchInputs(EntityDef entityDef, SearchInputsDef searchInputsDef) throws UnifyException {
        this(entityDef, searchInputsDef, Editable.TRUE);
    }

    public SearchInputs(EntityDef entityDef, SearchInputsDef searchInputsDef, Editable editable) throws UnifyException {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<SearchInputEntry>();
        this.viewEntryList = Collections.unmodifiableList(entryList);
        loadEntryList(searchInputsDef, editable);
    }

    public int addSearchInputEntry(SearchConditionType condition, String fieldName, String widget, String label,
            Editable editable) throws UnifyException {
        SearchInputEntry sie = new SearchInputEntry(entityDef, editable.isTrue());
        setFieldAndInputParams(sie, condition, fieldName, widget, label);
        entryList.add(sie);
        return entryList.size() - 1;
    }

    public void clear() throws UnifyException {
        entryList.clear();
    }

    public void moveUpEntry(int index) throws UnifyException {
        if (index > 0) {
            SearchInputEntry svo = entryList.remove(index);
            entryList.add(index - 1, svo);
        }
    }

    public void moveDownEntry(int index) throws UnifyException {
        if (index < entryList.size() - 2) {
            SearchInputEntry svo = entryList.remove(index);
            entryList.add(index + 1, svo);
        }
    }

    public void removeEntry(int index) throws UnifyException {
        entryList.remove(index);
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public SearchInputEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<SearchInputEntry> getEntryList() {
        return viewEntryList;
    }

    public int size() {
        return entryList.size();
    }

    public void normalize() throws UnifyException {
        ListIterator<SearchInputEntry> it = entryList.listIterator();
        int i = 0;
        int lim = entryList.size() - 1;
        while (it.hasNext()) {
            SearchInputEntry sie = it.next();
            sie.normalize(entityDef);
            if (!sie.isWithLabel()) {
                if (i < lim) {
                    it.remove();
                }
            }
            i++;
        }

        SearchInputEntry last = entryList.get(entryList.size() - 1);
        if (last.isWithFieldName()) {
            entryList.add(new SearchInputEntry(entityDef, true));
        }
    }

    public SearchInputsDef getSearchInputsDef() throws UnifyException {
        int lim = entryList.size() - 1;
        if (lim > 0) {
            SearchInputsDef.Builder sidb = SearchInputsDef.newBuilder();
            for (int i = 0; i < lim; i++) {
                SearchInputEntry sie = entryList.get(i);
                if (sie.isValidEntry()) {
                    sidb.addSearchInputDef(sie.getCondition(), sie.getFieldName(), sie.getWidget(), sie.getLabel());
                }
            }
            return sidb.build();
        }

        return null;
    }

    private void loadEntryList(SearchInputsDef searchInputsDef, Editable editable) throws UnifyException {
        if (searchInputsDef != null) {
            for (SearchInputDef searchInputDef : searchInputsDef.getSearchInputDefList()) {
                SearchInputEntry sie = new SearchInputEntry(entityDef, editable.isTrue());
                setFieldAndInputParams(sie, searchInputDef.getType(), searchInputDef.getFieldName(),
                        searchInputDef.getWidget(), searchInputDef.getLabel());
                entryList.add(sie);
            }
        }

        entryList.add(new SearchInputEntry(entityDef, editable.isTrue()));
    }

    private void setFieldAndInputParams(SearchInputEntry sie, SearchConditionType type, String fieldName, String widget,
            String label) throws UnifyException {
        sie.setCondition(type);
        sie.setFieldName(fieldName);
        sie.setLabel(label);
        sie.setWidget(widget);
        sie.normalize(entityDef);
    }
}
