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
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceEntryDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Field sequence object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FieldSequence {

    private EntityDef entityDef;

    private List<FieldSequenceEntry> entryList;

    private List<FieldSequenceEntry> viewEntryList;

    public FieldSequence(EntityDef entityDef, FieldSequenceDef fieldSequenceDef) throws UnifyException {
        this(entityDef, fieldSequenceDef, Editable.TRUE);
    }

    public FieldSequence(EntityDef entityDef, FieldSequenceDef fieldSequenceDef, Editable editable)
            throws UnifyException {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<FieldSequenceEntry>();
        this.viewEntryList = Collections.unmodifiableList(entryList);
        loadEntryList(fieldSequenceDef, editable);
    }

    public int addFieldSequenceEntry(String fieldName, String param, Editable editable) throws UnifyException {
        FieldSequenceEntry fse = new FieldSequenceEntry(entityDef, editable.isTrue());
        setFieldAndInputParams(fse, fieldName, param);
        entryList.add(fse);
        return entryList.size() - 1;
    }

    public void clear() throws UnifyException {
        entryList.clear();
    }

    public void moveUpEntry(int index) throws UnifyException {
        if (index > 0) {
            FieldSequenceEntry fse = entryList.remove(index);
            entryList.add(index - 1, fse);
        }
    }

    public void moveDownEntry(int index) throws UnifyException {
        if (index < entryList.size() - 2) {
            FieldSequenceEntry fse = entryList.remove(index);
            entryList.add(index + 1, fse);
        }
    }

    public void removeEntry(int index) throws UnifyException {
        entryList.remove(index);
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public FieldSequenceEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<FieldSequenceEntry> getEntryList() {
        return viewEntryList;
    }

    public int size() {
        return entryList.size();
    }

    public void normalize() throws UnifyException {
        ListIterator<FieldSequenceEntry> it = entryList.listIterator();
        int i = 0;
        int lim = entryList.size() - 1;
        while (it.hasNext()) {
            FieldSequenceEntry svo = it.next();
            svo.normalize();
            if (!svo.isWithFieldName()) {
                if (i < lim) {
                    it.remove();
                }
            }
            i++;
        }

        FieldSequenceEntry last = entryList.get(entryList.size() - 1);
        if (last.isWithFieldName()) {
            entryList.add(new FieldSequenceEntry(entityDef, true));
        }
    }

    public FieldSequenceDef getFieldSequenceDef() throws UnifyException {
        int lim = entryList.size() - 1;
        if (lim > 0) {
            FieldSequenceDef.Builder fsb = FieldSequenceDef.newBuilder();
            for (int i = 0; i < lim; i++) {
                FieldSequenceEntry fso = entryList.get(i);
                if (!StringUtils.isBlank(fso.getFieldName())) {
                    fsb.addFieldSequenceEntryDef(fso.getFieldName(), fso.getParam());
                }
            }
            return fsb.build();
        }

        return null;
    }

    private void loadEntryList(FieldSequenceDef fieldSequenceDef, Editable editable) throws UnifyException {
        if (fieldSequenceDef != null) {
            for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                FieldSequenceEntry fso = new FieldSequenceEntry(entityDef, editable.isTrue());
                setFieldAndInputParams(fso, fieldSequenceEntryDef.getFieldName(),
                        fieldSequenceEntryDef.getStandardFormatCode());
                entryList.add(fso);
            }
        }

        entryList.add(new FieldSequenceEntry(entityDef, editable.isTrue()));
    }

    private void setFieldAndInputParams(FieldSequenceEntry fso, String fieldName, String param) throws UnifyException {
        fso.setFieldName(fieldName);
        fso.setParam(param);
        fso.normalize();
    }
}
