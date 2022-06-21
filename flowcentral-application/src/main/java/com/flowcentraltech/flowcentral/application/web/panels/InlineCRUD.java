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
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
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

    public void addEntry() throws UnifyException {
        addEntry(true);
    }

    public void insertEntries(List<T> entries, int index) throws UnifyException {
        insertEntries(entries, index, false);
    }

    @SuppressWarnings("unchecked")
    public void insertEntries(List<T> entries, int index, boolean replace) throws UnifyException {
        List<T> _entries = (List<T>) table.getSourceObject();
        if (replace && index < (_entries.size() - 1)) {
            _entries.remove(index);
        }
        
        _entries.addAll(index, entries);
        table.fireOnTableChange();
    }

    @SuppressWarnings("unchecked")
    public void deleteEntry(int index) throws UnifyException {
        List<T> _entries = (List<T>) table.getSourceObject();
        _entries.remove(index);
        table.fireOnTableChange();
    }

    public void fireOnRowChange(int rowIndex, String trigger) throws UnifyException {
        table.fireOnRowChange(rowIndex, trigger);
    }

    public void loadEntries(InlineCRUDTablePolicy<T> tablePolicy, List<T> entries,
            ValueStoreReader parentReader) throws UnifyException {
        if (tablePolicy == null) {
            throw new IllegalArgumentException("Inline CRUD table policy is required.");
        }

        List<T> _entries = new ArrayList<T>(entries);
        table.setPolicy(tablePolicy);
        table.setParentReader(parentReader);
        table.setSourceObject(_entries);
        addEntry(false);
    }

    public void clearEntries() throws UnifyException {
        table.setSourceObject(new ArrayList<T>());
        addEntry(false);
    }

    @SuppressWarnings("unchecked")
    public List<T> unload() throws UnifyException {
        List<T> entries = (List<T>) table.getSourceObject();
        List<T> _entries = new ArrayList<T>(entries);
        _entries.remove(_entries.size() - 1);
        return _entries;
    }

    @SuppressWarnings("unchecked")
    public List<T> entries() throws UnifyException {
        List<T> entries = (List<T>) table.getSourceObject();
        return Collections.unmodifiableList(entries);
    }
    
    @SuppressWarnings("unchecked")
    private void addEntry(boolean fireTableChange) throws UnifyException {
        createAndAddInst((List<T>) table.getSourceObject());
        if (fireTableChange) {
            table.fireOnTableChange();
        }
    }

    @SuppressWarnings("unchecked")
    private void createAndAddInst(List<T> items) throws UnifyException {
        T item = ReflectUtils.newInstance(entryClass);
        InlineCRUDTablePolicy<T> policy = (InlineCRUDTablePolicy<T>) table.getEntryPolicy();
        if (policy != null) {
            policy.onAddItem(table.getParentReader(), items, item, table.getItemCount());
        }

        items.add(item);
    }

}
