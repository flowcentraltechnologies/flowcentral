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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Inline CRUD object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class InlineCRUD<T extends InlineCRUDEntry> {

    private BeanTable table;

    private Class<T> entryClass;

    public InlineCRUD(AppletUtilities au, TableDef tableDef, Class<T> entryClass) {
        this.table = new BeanTable(au, tableDef, true);
        this.entryClass = entryClass;
    }

    public BeanTable getTable() {
        return table;
    }

    @SuppressWarnings("unchecked")
    public void addEntry() throws UnifyException {
        T entry = createInst(); // TODO On load set default values
        ((List<T>) table.getSourceObject()).add(entry);
    }

    @SuppressWarnings("unchecked")
    public void deleteEntry(int index) throws UnifyException {
        List<T> _entries = (List<T>) table.getSourceObject();
        _entries.remove(index);
        table.fireOnChange();
    }

    public void loadEntries(InlineCRUDTablePolicy tablePolicy, List<T> entries) throws UnifyException {
        if (tablePolicy == null) {
            throw new IllegalArgumentException("Inline CRUD table policy is required.");
        }

        List<T> _entries = new ArrayList<T>(entries);
        table.setPolicy(tablePolicy);
        table.setSourceObject(_entries);
        addEntry();
    }

    @SuppressWarnings("unchecked")
    public List<T> unload() throws UnifyException {
        List<T> entries = (List<T>) table.getSourceObject();
        List<T> _entries = new ArrayList<T>(entries);
        _entries.remove(_entries.size() - 1);
        return _entries;
    }

    private T createInst() throws UnifyException {
        T entry = ReflectUtils.newInstance(entryClass);
        InlineCRUDTablePolicy policy = (InlineCRUDTablePolicy) table.getEntryPolicy();
        if (policy != null) {
            policy.onCreate(table.getAu(), entry);
        }

        return entry;
    }

}
