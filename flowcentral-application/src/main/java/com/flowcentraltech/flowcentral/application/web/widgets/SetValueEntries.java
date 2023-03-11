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
import com.flowcentraltech.flowcentral.application.data.SetValueDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Set value entries object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetValueEntries {

    private EntityDef entityDef;

    private List<SetValueEntry> entryList;

    private List<SetValueEntry> viewEntryList;

    public SetValueEntries(EntityDef entityDef) {
        this(entityDef, Editable.TRUE);
    }

    public SetValueEntries(EntityDef entityDef, Editable rootEditable) {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<SetValueEntry>();
        this.entryList.add(new SetValueEntry(entityDef, rootEditable.isTrue()));
        this.viewEntryList = Collections.unmodifiableList(entryList);
    }

    public SetValueEntries(EntityDef entityDef, SetValuesDef setValuesDef) throws UnifyException {
        this(entityDef, setValuesDef, Editable.TRUE);
    }

    public SetValueEntries(EntityDef entityDef, SetValuesDef setValuesDef, Editable editable) throws UnifyException {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<SetValueEntry>();
        this.viewEntryList = Collections.unmodifiableList(entryList);
        loadEntryList(setValuesDef, editable);
    }

    public void clear() throws UnifyException {
        entryList.clear();
    }

    public void removeEntry(int index) throws UnifyException {
        entryList.remove(index);
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public SetValueEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<SetValueEntry> getEntryList() {
        return viewEntryList;
    }

    public int size() {
        return entryList.size();
    }

    public void normalize() throws UnifyException {
        ListIterator<SetValueEntry> it = entryList.listIterator();
        int i = 0;
        int lim = entryList.size() - 1;
        while (it.hasNext()) {
            SetValueEntry svo = it.next();
            svo.normalize();
            if (!svo.isWithFieldName()) {
                if (i < lim) {
                    it.remove();
                }
            }
            i++;
        }

        SetValueEntry last = entryList.get(entryList.size() - 1);
        if (last.isWithFieldName()) {
            entryList.add(new SetValueEntry(entityDef, true));
        }
    }

    public SetValuesDef getSetValuesDef() throws UnifyException {
        int lim = entryList.size() - 1;
        if (lim > 0) {
            SetValuesDef.Builder svdb = SetValuesDef.newBuilder();
            for (int i = 0; i < lim; i++) {
                SetValueEntry svo = entryList.get(i);
                if (!StringUtils.isBlank(svo.getFieldName())) {
                    String param = svo.isWithParamInput() ? svo.getParamInput().getStringValue() : null;
                    svdb.addSetValueDef(svo.getType(), svo.getFieldName(), param);
                }
            }
            return svdb.build();
        }

        return null;
    }

    private void loadEntryList(SetValuesDef setValuesDef, Editable editable) throws UnifyException {
        if (setValuesDef != null) {
            for (SetValueDef setValueDef : setValuesDef.getSetValueList()) {
                SetValueEntry svo = new SetValueEntry(entityDef, editable.isTrue());
                setFieldAndInputParams(svo, setValueDef);
                entryList.add(svo);
            }
        }

        entryList.add(new SetValueEntry(entityDef, editable.isTrue()));
    }

    private void setFieldAndInputParams(SetValueEntry svo, SetValueDef setValueDef) throws UnifyException {
        svo.setFieldName(setValueDef.getFieldName());
        svo.setType(setValueDef.getType());
        svo.normalize();

        AbstractInput<?> in = svo.getParamInput();
        if (in != null) {
            in.setStringValue(setValueDef.getParam());
        }
    }
}
