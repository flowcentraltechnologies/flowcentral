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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.DataChangeType;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.DiffEntity;
import com.flowcentraltech.flowcentral.application.data.DiffEntityField;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.common.util.RestrictionUtils;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
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
     *                      applet utility
     * @param formDef
     *                      the form definition
     * @param leftEntityId
     *                      the left entity ID
     * @param rightEntityId
     *                      the right entity ID
     * @param formats
     *                      formats object
     * @return the difference
     * @throws UnifyException
     *                        if an error occurs
     */
    public static Diff diff(AppletUtilities au, FormDef formDef, Long leftEntityId, Long rightEntityId,
            Formats.Instance formats) throws UnifyException {
        return DiffUtils.diff(au, formDef, leftEntityId, rightEntityId, formats, null);
    }

    @SuppressWarnings("unchecked")
    private static Diff diff(AppletUtilities au, FormDef formDef, Long leftEntityId, Long rightEntityId,
            Formats.Instance formats, String label) throws UnifyException {
        final Date now = au.getNow();
        final EntityDef entityDef = formDef.getEntityDef();
        final EntityClassDef entityClassDef = au.getEntityClassDef(entityDef.getLongName());
        final Entity left = leftEntityId != null
                ? au.environment().find((Class<? extends Entity>) entityClassDef.getEntityClass(), leftEntityId)
                : null;
        final Entity right = rightEntityId != null
                ? au.environment().find((Class<? extends Entity>) entityClassDef.getEntityClass(), rightEntityId)
                : null;

        List<FormTabDef> tabs = formDef.getFormTabDefList();
        FormTabDef mainTabDef = tabs.get(0);
        List<DiffEntityField> lfields = Collections.emptyList();
        List<DiffEntityField> rfields = Collections.emptyList();
        if (left != null && right == null) {
            lfields = getFields(mainTabDef, entityDef, left, DataChangeType.NEW, formats);
        } else if (left == null && right != null) {
            rfields = getFields(mainTabDef, entityDef, right, DataChangeType.DELETED, formats);
        } else if (left != null && right != null) {
            lfields = new ArrayList<DiffEntityField>();
            rfields = new ArrayList<DiffEntityField>();
            for (FormSectionDef formSectionDef : mainTabDef.getFormSectionDefList()) {
                for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                    final String fieldName = formFieldDef.getFieldName();
                    final Object lval = ReflectUtils.getBeanProperty(left, fieldName);
                    final Object rval = ReflectUtils.getBeanProperty(right, fieldName);
                    final String lvalStr = formats.format(lval);
                    final String rvalStr = formats.format(rval);
                    DataChangeType lChangeType = DataChangeType.NONE;
                    DataChangeType rChangeType = DataChangeType.NONE;
                    if (!DataUtils.equals(lvalStr, rvalStr)) {
                        lChangeType = lvalStr != null ? (rvalStr != null ? DataChangeType.UPDATED : DataChangeType.NEW)
                                : DataChangeType.NONE;

                        rChangeType = lvalStr != null ? (rvalStr != null ? DataChangeType.NONE : DataChangeType.NONE)
                                : DataChangeType.DELETED;
                    }

                    final boolean number = entityDef.getFieldDef(fieldName).isNumber();
                    lfields.add(
                            new DiffEntityField(lChangeType, fieldName, formFieldDef.getFieldLabel(), lvalStr, number));
                    rfields.add(
                            new DiffEntityField(rChangeType, fieldName, formFieldDef.getFieldLabel(), rvalStr, number));
                }
            }
        }

        final DiffEntity dleft = new DiffEntity(label == null ? au.resolveSessionMessage("$m{formdiff.draft}") : label,
                lfields);
        final DiffEntity dright = new DiffEntity(
                label == null ? au.resolveSessionMessage("$m{formdiff.original}") : label, rfields);

        final List<Diff> children = new ArrayList<Diff>();
        final int len = tabs.size();
        for (int i = 1; i < len; i++) {
            FormTabDef childTabDef = tabs.get(i);
            TabContentType contentType = childTabDef.getContentType();
            switch (contentType) {
                case CHILD:
                case CHILD_LIST: {
                    List<Long> lChildIdList = getChildIdList(au, childTabDef, entityDef, left, now,
                            contentType.isChildList());
                    List<Long> rChildIdList = getChildIdList(au, childTabDef, entityDef, right, now,
                            contentType.isChildList());
                    final int clen = lChildIdList.size() < rChildIdList.size() ? rChildIdList.size()
                            : lChildIdList.size();
                    if (clen > 0) {
                        AppletDef _appletDef = au.getAppletDef(childTabDef.getApplet());
                        String cFormName = _appletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM);
                        if (!StringUtils.isBlank(cFormName)) {
                            FormDef cformDef = au.getFormDef(cFormName);
                            for (int j = 0; j < clen; j++) {
                                Long cleftEntityId = j < lChildIdList.size() ? lChildIdList.get(j) : null;
                                Long crightEntityId = j < rChildIdList.size() ? rChildIdList.get(j) : null;
                                Diff _diff = DiffUtils.diff(au, cformDef, cleftEntityId, crightEntityId, formats,
                                        childTabDef.getLabel());
                                children.add(_diff);
                            }
                        } else {
                            // Assignment
                        }
                    }
                }
                    break;
                case FIELD_SEQUENCE:
                    break;
                case FILTER_CONDITION:
                    break;
                case MINIFORM:
                    break;
                case MINIFORM_CHANGELOG:
                    break;
                case MINIFORM_MAPPED:
                    break;
                case PARAM_VALUES:
                    break;
                case PROPERTY_LIST:
                    break;
                case PROPERTY_SEQUENCE:
                    break;
                case SEARCH_INPUT:
                    break;
                case SET_VALUES:
                    break;
                case USAGE_LIST:
                    break;
                case WIDGET_RULES:
                    break;
                default:
                    break;
            }
        }

        return new Diff(dleft, dright, children);
    }

    @SuppressWarnings("unchecked")
    private static List<Long> getChildIdList(AppletUtilities au, FormTabDef childTabDef, EntityDef parentEntityDef,
            Entity parentInst, Date now, boolean list) throws UnifyException {
        if (parentInst != null) {
            Restriction childRestriction = au.getChildRestriction(parentEntityDef, childTabDef.getReference(),
                    parentInst);
            if (list) {
                Restriction tabRestriction = childTabDef.getRestriction(FilterType.TAB,
                        new BeanValueStore(parentInst).getReader(), now);
                childRestriction = RestrictionUtils.and(childRestriction, tabRestriction);
            }

            EntityClassDef cEntityDef = au.getAppletEntityClassDef(childTabDef.getApplet());
            List<Long> resultList = au.environment().valueList(Long.class, "id",
                    Query.of((Class<? extends Entity>) cEntityDef.getEntityClass()).addRestriction(childRestriction));
            Collections.sort(resultList);
            return resultList;
        }

        return Collections.emptyList();
    }

    private static List<DiffEntityField> getFields(FormTabDef formTabDef, EntityDef entityDef, Entity inst,
            DataChangeType changeType, Formats.Instance formats) throws UnifyException {
        List<DiffEntityField> fields = new ArrayList<DiffEntityField>();
        for (FormSectionDef formSectionDef : formTabDef.getFormSectionDefList()) {
            for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                final String fieldName = formFieldDef.getFieldName();
                final Object val = ReflectUtils.getBeanProperty(inst, fieldName);
                final String valStr = formats.format(val);
                fields.add(new DiffEntityField(val == null ? DataChangeType.NONE : changeType, fieldName,
                        formFieldDef.getFieldLabel(), valStr, entityDef.getFieldDef(fieldName).isNumber()));
            }
        }

        return fields;
    }
}
