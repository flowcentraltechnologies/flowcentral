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
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
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

    private FormValidationErrors errors;

    private String addCaption;

    public InlineCRUD(AppletUtilities au, TableDef tableDef, Class<T> entryClass) {
        this.table = new BeanTable(au, tableDef, null, BeanTable.ENTRY_ENABLED);
        this.entryClass = entryClass;
        this.errors = new FormValidationErrors();
    }

    public BeanTable getTable() {
        return table;
    }

    public String getAddCaption() {
        return addCaption;
    }

    public void setAddCaption(String addCaption) {
        this.addCaption = addCaption;
    }

    public void addEntry() throws UnifyException {
        addEntry(true);
    }

    public FormValidationErrors validate(EvaluationMode evaluationMode) throws UnifyException {
        errors.clearValidationErrors();
        table.validate(evaluationMode, errors);
        return errors;
    }

    public List<FormMessage> getValidationErrors() {
        return errors != null ? errors.getValidationErrors() : null;
    }

    public void insertEntries(List<T> entries, int index) throws UnifyException {
        insertEntries(entries, index, false);
    }

    public void replaceEntries(List<T> entries, int index) throws UnifyException {
        insertEntries(entries, index, true);
    }

    @SuppressWarnings("unchecked")
    public void deleteEntry(int index) throws UnifyException {
        List<T> _entries = (List<T>) table.getSourceObject();
        _entries.remove(index);
        table.setSourceObject(new ArrayList<T>(_entries));
        EntryActionType actionType = table.fireOnTableChange(TableChangeType.DELETE_ENTRY);
        if (actionType.isAddItem()) {
            addEntry(false);
        }
    }

    public void fireOnRowChange(String trigger, int rowIndex) throws UnifyException {
        fireOnRowChange(new RowChangeInfo(trigger, rowIndex));
    }

    public void fireOnRowChange(RowChangeInfo rowChangeInfo) throws UnifyException {
        EntryActionType actionType = table.fireOnRowChange(rowChangeInfo);
        if (actionType.isAddItem()) {
            addEntry(false);
        }
    }

    public void loadEntries(InlineCRUDTablePolicy<T> tablePolicy, List<T> entries, ValueStoreReader parentReader)
            throws UnifyException {
        if (tablePolicy == null) {
            throw new IllegalArgumentException("Inline CRUD table policy is required.");
        }

        List<T> _entries = new ArrayList<T>(entries);
        table.setPolicy(tablePolicy);
        table.setParentReader(parentReader);
        table.setSourceObject(_entries);
        if (_entries.isEmpty()) {
            addEntry(false);
        }
    }

    public void clearEntries() throws UnifyException {
        table.setSourceObject(new ArrayList<T>());
        addEntry(false);
    }

    @SuppressWarnings("unchecked")
    public List<T> unload() throws UnifyException {
        List<T> entries = (List<T>) table.getSourceObject();
        return new ArrayList<T>(entries);
    }

    @SuppressWarnings("unchecked")
    public List<T> entries() throws UnifyException {
        List<T> entries = (List<T>) table.getSourceObject();
        return Collections.unmodifiableList(entries);
    }

    @SuppressWarnings("unchecked")
    public int size() {
        List<T> entries = (List<T>) table.getSourceObject();
        return entries != null ? entries.size() : 0;
    }
    
    @SuppressWarnings("unchecked")
    public T getEntry(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Invalid index " + index + " accessing inline CRUD of size " + size());
        }
        
        List<T> entries = (List<T>) table.getSourceObject();
        return entries.get(index);
    }
    
    @SuppressWarnings("unchecked")
    private void addEntry(boolean fireTableChange) throws UnifyException {
        createAndAddInst((List<T>) table.getSourceObject());
        if (fireTableChange) {
            table.fireOnTableChange(TableChangeType.ADD_ENTRY);
        }
    }

    @SuppressWarnings("unchecked")
    private void createAndAddInst(List<T> items) throws UnifyException {
        T item = ReflectUtils.newInstance(entryClass);
        items.add(item);

        InlineCRUDTablePolicy<T> policy = (InlineCRUDTablePolicy<T>) table.getEntryPolicy();
        if (policy != null) {
            policy.onAddItem(table.getParentReader(), items, item);
        }
    }

    @SuppressWarnings("unchecked")
    private void insertEntries(List<T> entries, int index, boolean replace) throws UnifyException {
        List<T> _entries = (List<T>) table.getSourceObject();
        if (replace && index < entries.size()) {
            _entries.remove(index);
        }

        _entries.addAll(index, entries);
        table.setSourceObject(new ArrayList<T>(_entries));
        EntryActionType actionType = table
                .fireOnTableChange(replace ? TableChangeType.REPLACE_ENTRIES : TableChangeType.INSERT_ENTRIES);
        if (actionType.isAddItem()) {
            addEntry(false);
        }
    }

}
