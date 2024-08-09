/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.DataChangeType;
import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.DiffEntity;
import com.flowcentraltech.flowcentral.application.data.DiffEntityField;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Diff utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class DiffUtils {

    private DiffUtils() {

    }

    /**
     * Performs a form difference.
     * 
     * @param au
     *                applet utility
     * @param formDef
     *                the form definition
     * @param left
     *                the left entity
     * @param right
     *                the right entity
     * @param formats
     *                formats object
     * @return the difference
     * @throws Exception
     *                   if an error occurs
     */
    public static Diff diff(AppletUtilities au, FormDef formDef, Entity left, Entity right, Formats.Instance formats)
            throws Exception {
        List<FormTabDef> tabs = formDef.getFormTabDefList();
        FormTabDef main = tabs.get(0);
        List<DiffEntityField> lfields = Collections.emptyList();
        List<DiffEntityField> rfields = Collections.emptyList();
        if (left != null && right == null) {
            lfields = getFields(main, left, DataChangeType.NEW, formats);
        } else if (left == null && right != null) {
            rfields = getFields(main, right, DataChangeType.DELETED, formats);
        } else if (left != null && right != null) {
            lfields = new ArrayList<DiffEntityField>();
            rfields = new ArrayList<DiffEntityField>();
            for (FormSectionDef formSectionDef : main.getFormSectionDefList()) {
                for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                    final Object lval = ReflectUtils.getBeanProperty(left, formFieldDef.getFieldName());
                    final Object rval = ReflectUtils.getBeanProperty(right, formFieldDef.getFieldName());
                    final String lvalStr = formats.format(lval);
                    final String rvalStr = formats.format(rval);
                    DataChangeType lChangeType = DataChangeType.NONE;
                    DataChangeType rChangeType = DataChangeType.NONE;
                    if (!DataUtils.equals(lvalStr, rvalStr)) {
                        lChangeType = lvalStr != null ? (rvalStr != null ? DataChangeType.UPDATED : DataChangeType.NEW)
                                : DataChangeType.NONE;

                        rChangeType = lvalStr != null ? (rvalStr != null ? DataChangeType.UPDATED : DataChangeType.NONE)
                                : DataChangeType.DELETED;
                    }

                    lfields.add(new DiffEntityField(lChangeType, formFieldDef.getFieldName(),
                            formFieldDef.getFieldLabel(), lvalStr));
                    rfields.add(new DiffEntityField(rChangeType, formFieldDef.getFieldName(),
                            formFieldDef.getFieldLabel(), rvalStr));
                }
            }
        }

        final DiffEntity dleft = new DiffEntity(main.getLabel(), lfields);
        final DiffEntity dright = new DiffEntity(main.getLabel(), rfields);

        final List<Diff> children = new ArrayList<Diff>();
        final int len = tabs.size();
        for (int i = 1; i < len; i++) {
            FormTabDef child = tabs.get(i);
            switch (child.getContentType()) {

            }
        }

        return new Diff(dleft, dright, children);
    }

    private static List<DiffEntityField> getFields(FormTabDef formTabDef, Entity inst, DataChangeType changeType,
            Formats.Instance formats) throws Exception {
        List<DiffEntityField> fields = new ArrayList<DiffEntityField>();
        for (FormSectionDef formSectionDef : formTabDef.getFormSectionDefList()) {
            for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                final Object val = ReflectUtils.getBeanProperty(inst, formFieldDef.getFieldName());
                final String valStr = formats.format(val);
                fields.add(new DiffEntityField(val == null ? DataChangeType.NONE : changeType,
                        formFieldDef.getFieldName(), formFieldDef.getFieldLabel(), valStr));
            }
        }

        return fields;
    }
}
