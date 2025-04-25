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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.flowcentraltech.flowcentral.application.constants.PropertySequenceType;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceEntryDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Property sequence object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PropertySequence {

    private PropertySequenceType type;

    private EntityDef entityDef;

    private List<PropertySequenceEntry> entryList;

    private List<PropertySequenceEntry> viewEntryList;

    public PropertySequence(PropertySequenceType type, EntityDef entityDef,
            PropertySequenceDef propertySequenceDef, Editable editable) throws UnifyException {
        this.type = type;
        this.entityDef = entityDef;
        this.entryList = new ArrayList<PropertySequenceEntry>();
        this.viewEntryList = Collections.unmodifiableList(entryList);
        loadEntryList(propertySequenceDef, editable);
    }

    public int addSequenceEntry(String property, String label, Editable editable) throws UnifyException {
        PropertySequenceEntry se = new PropertySequenceEntry(entityDef, type, editable.isTrue());
        setProperty(se, property, label);
        entryList.add(se);
        return entryList.size() - 1;
    }

    public void clear() throws UnifyException {
        entryList.clear();
    }

    public void moveUpEntry(int index) throws UnifyException {
        if (index > 0) {
            PropertySequenceEntry se = entryList.remove(index);
            entryList.add(index - 1, se);
        }
    }

    public void moveDownEntry(int index) throws UnifyException {
        if (index < entryList.size() - 2) {
            PropertySequenceEntry se = entryList.remove(index);
            entryList.add(index + 1, se);
        }
    }

    public void removeEntry(int index) throws UnifyException {
        entryList.remove(index);
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public PropertySequenceType getType() {
        return type;
    }

    public PropertySequenceEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<PropertySequenceEntry> getEntryList() {
        return viewEntryList;
    }

    public int size() {
        return entryList.size();
    }

    public void normalize() throws UnifyException {
        ListIterator<PropertySequenceEntry> it = entryList.listIterator();
        int i = 0;
        int lim = entryList.size() - 1;
        while (it.hasNext()) {
            PropertySequenceEntry svo = it.next();
            svo.normalize();
            if (!svo.isWithProperty()) {
                if (i < lim) {
                    it.remove();
                }
            }
            i++;
        }

        PropertySequenceEntry last = entryList.get(entryList.size() - 1);
        if (last.isWithProperty()) {
            entryList.add(new PropertySequenceEntry(entityDef, type, true));
        }
    }

    public PropertySequenceDef getSequenceDef() throws UnifyException {
        int lim = entryList.size() - 1;
        if (lim > 0) {
            PropertySequenceDef.Builder fsb = PropertySequenceDef.newBuilder();
            for (int i = 0; i < lim; i++) {
                PropertySequenceEntry fso = entryList.get(i);
                if (!StringUtils.isBlank(fso.getProperty())) {
                    fsb.addSequenceEntryDef(fso.getProperty(), fso.getLabel());
                }
            }
            return fsb.build();
        }

        return null;
    }

    private void loadEntryList(PropertySequenceDef propertySequenceDef, Editable editable) throws UnifyException {
        if (propertySequenceDef != null) {
            for (PropertySequenceEntryDef propertySequenceEntryDef : propertySequenceDef.getSequenceList()) {
                PropertySequenceEntry fso = new PropertySequenceEntry(entityDef, type, editable.isTrue());
                setProperty(fso, propertySequenceEntryDef.getProperty(), propertySequenceEntryDef.getLabel());
                entryList.add(fso);
            }
        }

        entryList.add(new PropertySequenceEntry(entityDef, type, editable.isTrue()));
    }

    private void setProperty(PropertySequenceEntry so, String property, String label) throws UnifyException {
        so.setProperty(property);
        so.setLabel(label);
        so.normalize();
    }
}
