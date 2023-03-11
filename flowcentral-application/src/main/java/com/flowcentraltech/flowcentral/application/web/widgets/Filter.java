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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleErrorConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterRestrictionDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.ResolvedCondition;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.criterion.CriteriaBuilder;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Filter object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Filter {

    private EntityDef entityDef;

    private LabelSuggestionDef labelSuggestionDef;

    private FilterConditionList conditionList;

    private List<FilterCondition> viewConditionList;

    private FilterConditionListType listType;

    private Long ownerInstId;

    private String paramList;

    public Filter(Long ownerInstId, String paramList, EntityDef entityDef, LabelSuggestionDef labelSuggestionDef,
            FilterConditionListType listType) {
        this(ownerInstId, paramList, entityDef, labelSuggestionDef, listType, Editable.TRUE);
    }

    public Filter(Long ownerInstId, String paramList, EntityDef entityDef, LabelSuggestionDef labelSuggestionDef,
            FilterConditionListType listType, Editable rootEditable) {
        this.entityDef = entityDef;
        this.labelSuggestionDef = labelSuggestionDef;
        this.conditionList = new FilterConditionList();
        this.conditionList.add(new FilterCondition(entityDef, labelSuggestionDef, ownerInstId, FilterConditionType.AND,
                listType, 0, rootEditable.isTrue()));
        this.viewConditionList = Collections.unmodifiableList(conditionList);
        this.listType = listType;
        this.ownerInstId = ownerInstId;
        this.paramList = paramList;
    }

    public Filter(Long ownerInstId, String paramList, EntityDef entityDef, FilterDef filterDef,
            FilterConditionListType listType) throws UnifyException {
        this(ownerInstId, paramList, entityDef, filterDef, listType, Editable.TRUE);
    }

    public Filter(Long ownerInstId, String paramList, EntityDef entityDef, FilterDef filterDef,
            FilterConditionListType listType, Editable editable) throws UnifyException {
        this.entityDef = entityDef;
        this.conditionList = new FilterConditionList();
        this.viewConditionList = Collections.unmodifiableList(conditionList);
        this.listType = listType;
        this.ownerInstId = ownerInstId;
        this.paramList = paramList;
        loadConditionList(filterDef, editable);
    }

    public int addCompoundCondition(int compoundIndex, CompoundType type) throws UnifyException {
        return addCompoundCondition(compoundIndex, type, Editable.TRUE);
    }

    public int addCompoundCondition(int compoundIndex, CompoundType type, Editable editable) throws UnifyException {
        if (CompoundType.AND.equals(type)) {
            return addCondition(compoundIndex, FilterConditionType.AND, editable.isTrue());
        }

        return addCondition(compoundIndex, FilterConditionType.OR, editable.isTrue());
    }

    public int addSimpleCondition(int compoundIndex) throws UnifyException {
        return addCondition(compoundIndex, null, true);
    }

    public int addSimpleCondition(int compoundIndex, Editable editable) throws UnifyException {
        return addCondition(compoundIndex, null, editable.isTrue());
    }

    public int addSimpleCondition(int compoundIndex, FilterConditionType type, String fieldName) throws UnifyException {
        return addSimpleCondition(compoundIndex, type, fieldName, null, null, Editable.TRUE);
    }

    public int addSimpleCondition(int compoundIndex, FilterConditionType type, String fieldName, Editable editable)
            throws UnifyException {
        return addSimpleCondition(compoundIndex, type, fieldName, null, null, editable);
    }

    public int addSimpleCondition(int compoundIndex, FilterConditionType type, String fieldName, String paramA)
            throws UnifyException {
        return addSimpleCondition(compoundIndex, type, fieldName, paramA, null, Editable.TRUE);
    }

    public int addSimpleCondition(int compoundIndex, FilterConditionType type, String fieldName, String paramA,
            Editable editable) throws UnifyException {
        return addSimpleCondition(compoundIndex, type, fieldName, paramA, null, editable);
    }

    public int addSimpleCondition(int compoundIndex, FilterConditionType type, String fieldName, String paramA,
            String paramB) throws UnifyException {
        return addSimpleCondition(compoundIndex, type, fieldName, paramA, paramB, Editable.TRUE);
    }

    public int addSimpleCondition(int compoundIndex, FilterConditionType type, String fieldName, String paramA,
            String paramB, Editable editable) throws UnifyException {
        int index = addCondition(compoundIndex, type, editable.isTrue());
        FilterCondition fo = conditionList.get(index);
        setFieldAndInputParams(fo, fieldName, paramA, paramB);
        return index;
    }

    public void clear() throws UnifyException {
        removeCondition(0);
    }

    public void removeCondition(int index) throws UnifyException {
        if (index == 0) {
            conditionList.removeRange(1, conditionList.size());
            return;
        }

        FilterCondition fo = conditionList.get(index);
        if (fo != null && fo.getType() != null && fo.getType().isCompound()) {
            int cDepth = fo.getDepth() + 1;
            int i = index + 1;
            for (; i < conditionList.size(); i++) {
                if (conditionList.get(i).getDepth() < cDepth) {
                    break;
                }
            }

            conditionList.removeRange(index, i);
            return;
        }

        conditionList.remove(index);
    }

    public void swapLogic(int compoundIndex) {
        conditionList.get(compoundIndex).swapLogic();
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public LabelSuggestionDef getLabelSuggestionDef() {
        return labelSuggestionDef;
    }

    public Long getOwnerInstId() {
        return ownerInstId;
    }

    public String getParamList() {
        return paramList;
    }

    public FilterCondition getCondition(int index) {
        return conditionList.get(index);
    }

    public List<FilterCondition> getConditionList() {
        return viewConditionList;
    }

    public int size() {
        return conditionList.size();
    }

    public FilterConditionListType getListType() {
        return listType;
    }

    public void normalize() throws UnifyException {
        for (FilterCondition fo : conditionList) {
            fo.normalize();
        }
    }

    public FilterDef getFilterDef(AppletUtilities au) throws UnifyException {
        if (conditionList.size() > 1) {
            FilterDef.Builder fdb = FilterDef.newBuilder(au);
            for (FilterCondition fo : conditionList) {
                if (fo.getType().isFieldVal() || fo.getType().isParameterVal()) {
                    fdb.addRestrictionDef(fo.getType(), fo.getFieldName(), fo.getParamFieldA(), fo.getParamFieldB(),
                            fo.getDepth());
                } else {
                    String paramA = fo.isWithParamInputA() ? fo.getParamInputA().getStringValue() : null;
                    String paramB = fo.isWithParamInputB() ? fo.getParamInputB().getStringValue() : null;
                    fdb.addRestrictionDef(fo.getType(), fo.getFieldName(), paramA, paramB, fo.getDepth());
                }
            }
            return fdb.build();
        }

        return null;
    }

    public CompoundRestriction getRestriction(Date now) throws UnifyException {
        if (conditionList.size() > 1) {
            CriteriaBuilder cb = new CriteriaBuilder();
            if (addCompoundCriteria(cb, conditionList.get(0), 1, now) > 0) {
                return cb.build();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Filter [entityDef=" + entityDef + ", labelSuggestionDef=" + labelSuggestionDef + ", conditionList="
                + conditionList + ", viewConditionList=" + viewConditionList + ", listType=" + listType
                + ", ownerInstId=" + ownerInstId + ", paramList=" + paramList + "]";
    }

    private class FilterConditionList extends ArrayList<FilterCondition> {

        private static final long serialVersionUID = 5913942222681181614L;

        public void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }

    private int addCondition(int compoundIndex, FilterConditionType type, boolean editable) throws UnifyException {
        FilterCondition po = conditionList.get(compoundIndex);
        if (!po.getType().isCompound()) {
            throw new UnifyException(ApplicationModuleErrorConstants.FILTERCONDITIONINFO_INSERT_IN_SIMPLE_CONDITION);
        }

        int cDepth = po.getDepth() + 1;
        int i = compoundIndex + 1;
        for (; i < conditionList.size(); i++) {
            if (conditionList.get(i).getDepth() < cDepth) {
                break;
            }
        }

        if (i < conditionList.size()) {
            conditionList.set(i,
                    new FilterCondition(entityDef, labelSuggestionDef, ownerInstId, type, listType, cDepth, editable));
        } else {
            conditionList.add(
                    new FilterCondition(entityDef, labelSuggestionDef, ownerInstId, type, listType, cDepth, editable));
        }

        return i;
    }

    private void loadConditionList(FilterDef filterDef, Editable editable) throws UnifyException {
        int depthOffset = 0;
        if (filterDef == null || filterDef.isBlank()
                || !filterDef.getFilterRestrictionDefList().get(0).getType().isCompound()) {
            conditionList.add(new FilterCondition(entityDef, labelSuggestionDef, ownerInstId, FilterConditionType.AND,
                    listType, 0, editable.isTrue()));
            depthOffset++;
        }

        if (filterDef != null) {
            for (FilterRestrictionDef filterRestrictionDef : filterDef.getFilterRestrictionDefList()) {
                FilterCondition fo = new FilterCondition(entityDef, labelSuggestionDef, ownerInstId,
                        filterRestrictionDef.getType(), listType, filterRestrictionDef.getDepth() + depthOffset,
                        editable.isTrue());
                setFieldAndInputParams(fo, filterRestrictionDef.getFieldName(), filterRestrictionDef.getParamA(),
                        filterRestrictionDef.getParamB());
                conditionList.add(fo);
            }
        }
    }

    private void setFieldAndInputParams(FilterCondition fo, String fieldName, String paramA, String paramB)
            throws UnifyException {
        fo.setFieldName(fieldName);
        fo.normalize();

        if (fo.getType().isFieldVal() || fo.getType().isParameterVal()) {
            fo.setParamFieldA(paramA);
            fo.setParamFieldB(paramB);
        } else {
            AbstractInput<?> in = fo.getParamInputA();
            if (in != null) {
                in.setStringValue(paramA);
            }

            in = fo.getParamInputB();
            if (in != null) {
                in.setStringValue(paramB);
            }
        }
    }

    private int addCompoundCriteria(CriteriaBuilder cb, FilterCondition fo, int nIndex, Date now)
            throws UnifyException {
        if (FilterConditionType.AND.equals(fo.getType())) {
            cb.beginAnd();
        } else {
            cb.beginOr();
        }

        int len = conditionList.size();
        int depth = fo.getDepth();
        int i = nIndex;
        for (; i > 0 && i < len; i++) {
            FilterCondition sfo = conditionList.get(i);
            if (!sfo.isValidState()) {
                return -1;
            }

            if (sfo.getDepth() > depth) {
                if (sfo.getType().isCompound()) {
                    i = addCompoundCriteria(cb, sfo, i + 1, now) - 1;
                } else {
                    addSimpleCriteria(cb, sfo, now);
                }
            } else {
                break;
            }
        }

        cb.endCompound();
        return i;
    }

    @SuppressWarnings("unchecked")
    private void addSimpleCriteria(CriteriaBuilder cb, FilterCondition fo, Date now) throws UnifyException {
        FilterConditionType type = fo.getType();
        if (type.isFieldVal() || type.isParameterVal()) {
            type.addSimpleCriteria(cb, fo.getFieldName(), fo.getParamFieldA(), fo.getParamFieldB());
        } else {
            Object paramA = null;
            Object paramB = null;
            if (fo.isWithParamInputA()) {
                paramA = fo.getParamInputA().getValue();
            }

            if (fo.isWithParamInputB()) {
                paramB = fo.getParamInputB().getValue();
            }

            EntityFieldDef entityFieldDef = fo.getEntityFieldDef();
            if (type.isLingual() && entityFieldDef.isString()) {
                ResolvedCondition resolved = InputWidgetUtils.resolveLingualStringCondition(entityFieldDef, now, type,
                        paramA, paramB);
                type = resolved.getType();
                paramA = resolved.getParamA();
                paramB = resolved.getParamB();
            } else if (entityFieldDef.isDate() || entityFieldDef.isTimestamp()) {
                ResolvedCondition resolved = InputWidgetUtils.resolveDateCondition(entityFieldDef, now, type, paramA,
                        paramB);
                type = resolved.getType();
                paramA = resolved.getParamA();
                paramB = resolved.getParamB();
            }

            if (type.isAmongst()) {
                paramA = DataUtils.convert(List.class, String.class,
                        Arrays.asList(CommonInputUtils.breakdownCollectionString((String) paramA)));
            }
            
            if (type != null) { 
                type.addSimpleCriteria(cb, fo.getFieldName(), paramA, paramB);
            }
        }
    }
}
