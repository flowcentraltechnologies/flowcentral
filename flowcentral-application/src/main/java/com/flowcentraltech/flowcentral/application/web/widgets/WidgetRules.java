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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRuleEntryDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Widget rules object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WidgetRules {

    private EntityDef entityDef;

    private List<WidgetRuleEntry> entryList;

    private List<WidgetRuleEntry> viewEntryList;

    public WidgetRules(EntityDef entityDef) {
        this(entityDef, Editable.TRUE);
    }

    public WidgetRules(EntityDef entityDef, Editable rootEditable) {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<WidgetRuleEntry>();
        this.entryList.add(new WidgetRuleEntry(entityDef, rootEditable.isTrue()));
        this.viewEntryList = Collections.unmodifiableList(entryList);
    }

    public WidgetRules(EntityDef entityDef, WidgetRulesDef widgetRulesDef) throws UnifyException {
        this(entityDef, widgetRulesDef, Editable.TRUE);
    }

    public WidgetRules(EntityDef entityDef, WidgetRulesDef widgetRulesDef, Editable editable) throws UnifyException {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<WidgetRuleEntry>();
        this.viewEntryList = Collections.unmodifiableList(entryList);
        loadEntryList(widgetRulesDef, editable);
    }

    public int addWidgetRuleEntry(String fieldName, String widget, Editable editable) throws UnifyException {
        WidgetRuleEntry svo = new WidgetRuleEntry(entityDef, editable.isTrue());
        setFieldAndInputWidgets(svo, fieldName, widget);
        entryList.add(svo);
        return entryList.size() - 1;
    }

    public void clear() throws UnifyException {
        entryList.clear();
    }

    public void moveUpEntry(int index) throws UnifyException {
        if (index > 0) {
            WidgetRuleEntry svo = entryList.remove(index);
            entryList.add(index - 1, svo);
        }
    }

    public void moveDownEntry(int index) throws UnifyException {
        if (index < entryList.size() - 2) {
            WidgetRuleEntry svo = entryList.remove(index);
            entryList.add(index + 1, svo);
        }
    }

    public void removeEntry(int index) throws UnifyException {
        entryList.remove(index);
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public WidgetRuleEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<WidgetRuleEntry> getEntryList() {
        return viewEntryList;
    }

    public int size() {
        return entryList.size();
    }

    public void normalize() throws UnifyException {
        ListIterator<WidgetRuleEntry> it = entryList.listIterator();
        int i = 0;
        int lim = entryList.size() - 1;
        while (it.hasNext()) {
            WidgetRuleEntry svo = it.next();
            svo.normalize();
            if (!svo.isWithFieldName()) {
                if (i < lim) {
                    it.remove();
                }
            }
            i++;
        }

        WidgetRuleEntry last = entryList.get(entryList.size() - 1);
        if (last.isWithFieldName()) {
            entryList.add(new WidgetRuleEntry(entityDef, true));
        }
    }

    public WidgetRulesDef getWidgetRulesDef() throws UnifyException {
        int lim = entryList.size() - 1;
        if (lim > 0) {
            WidgetRulesDef.Builder wrb = WidgetRulesDef.newBuilder();
            for (int i = 0; i < lim; i++) {
                WidgetRuleEntry fso = entryList.get(i);
                if (!StringUtils.isBlank(fso.getFieldName())) {
                    wrb.addWidgetRuleEntryDef(fso.getFieldName(), fso.getWidget());
                }
            }
            return wrb.build();
        }

        return null;
    }

    private void loadEntryList(WidgetRulesDef widgetRulesDef, Editable editable) throws UnifyException {
        if (widgetRulesDef != null) {
            for (WidgetRuleEntryDef widgetRuleEntryDef : widgetRulesDef.getWidgetRuleEntryList()) {
                WidgetRuleEntry fso = new WidgetRuleEntry(entityDef, editable.isTrue());
                setFieldAndInputWidgets(fso, widgetRuleEntryDef.getFieldName(), widgetRuleEntryDef.getWidget());
                entryList.add(fso);
            }
        }

        entryList.add(new WidgetRuleEntry(entityDef, editable.isTrue()));
    }

    private void setFieldAndInputWidgets(WidgetRuleEntry fso, String fieldName, String widget) throws UnifyException {
        fso.setFieldName(fieldName);
        fso.setWidget(widget);
        fso.normalize();
    }
}
