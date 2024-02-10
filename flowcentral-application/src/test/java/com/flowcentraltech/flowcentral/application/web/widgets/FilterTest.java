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
package com.flowcentraltech.flowcentral.application.web.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.RestrictionTranslator;

/**
 * Filter object test.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterTest extends AbstractFlowCentralTest {

    private static EntityDef ed;

    private static LabelSuggestionDef labelSuggestionDef;

    private RestrictionTranslator rt;

    private AppletUtilities au;

    @Test
    public void testBlankFilter() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(1, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddCompoundAndToRoot() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addCompoundCondition(0, CompoundType.AND, Editable.TRUE);
        assertEquals(1, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddCompoundOrToRoot() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addCompoundCondition(0, CompoundType.OR, Editable.TRUE);
        assertEquals(1, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.OR, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddBlankSimpleCondition() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addSimpleCondition(0, Editable.TRUE);
        assertEquals(1, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertNull(fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddSimpleConditionZeroParam() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addSimpleCondition(0, FilterConditionType.IS_NULL, "costPrice", Editable.TRUE);
        assertEquals(1, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.IS_NULL, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddSimpleConditionSingleParam() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addSimpleCondition(0, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45",
                Editable.TRUE);
        assertEquals(1, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.GREATER_OR_EQUAL, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(20.45), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddSimpleConditionDoubleParam() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addSimpleCondition(0, FilterConditionType.BETWEEN, "costPrice", "20.45", "42.10",
                Editable.TRUE);
        assertEquals(1, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.BETWEEN, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(20.45), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNotNull(fco.getParamInputB());
        fi = fco.getParamInputB();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(42.10), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddMultipleSimpleCondition() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.TRUE);
        assertEquals(1, addIndex);
        addIndex = fo.addSimpleCondition(0, FilterConditionType.BETWEEN, "costPrice", "20.45", "42.10", Editable.TRUE);
        assertEquals(2, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(3, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.LIKE, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:stringconditionlist"));
        assertEquals("name", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(String.class, fi.getType());
        assertEquals("plane", fi.getValue());
        assertEquals("!ui-name style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(2);
        assertEquals(FilterConditionType.BETWEEN, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(20.45), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNotNull(fco.getParamInputB());
        fi = fco.getParamInputB();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(42.10), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddDeepConditionsSequential() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.TRUE);
        assertEquals(1, addIndex);
        addIndex = fo.addCompoundCondition(0, CompoundType.OR, Editable.TRUE);
        assertEquals(2, addIndex);
        addIndex = fo.addSimpleCondition(2, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45", Editable.TRUE);
        assertEquals(3, addIndex);
        addIndex = fo.addSimpleCondition(2, FilterConditionType.LESS_OR_EQUAL, "costPrice", "42.10", Editable.TRUE);
        assertEquals(4, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(5, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.LIKE, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:stringconditionlist"));
        assertEquals("name", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(String.class, fi.getType());
        assertEquals("plane", fi.getValue());
        assertEquals("!ui-name style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(2);
        assertEquals(FilterConditionType.OR, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(3);
        assertEquals(FilterConditionType.GREATER_OR_EQUAL, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(20.45), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(2, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(4);
        assertEquals(FilterConditionType.LESS_OR_EQUAL, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(42.10), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(2, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testAddDeepConditionsRandom() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        int addIndex = fo.addCompoundCondition(0, CompoundType.OR, Editable.TRUE);
        assertEquals(1, addIndex);
        addIndex = fo.addSimpleCondition(1, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45", Editable.TRUE);
        assertEquals(2, addIndex);
        addIndex = fo.addSimpleCondition(1, FilterConditionType.LESS_OR_EQUAL, "costPrice", "42.10", Editable.TRUE);
        assertEquals(3, addIndex);
        addIndex = fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.TRUE);
        assertEquals(4, addIndex);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(5, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.OR, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(2);
        assertEquals(FilterConditionType.GREATER_OR_EQUAL, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(20.45), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(2, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(3);
        assertEquals(FilterConditionType.LESS_OR_EQUAL, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(42.10), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(2, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(4);
        assertEquals(FilterConditionType.LIKE, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:stringconditionlist"));
        assertEquals("name", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        fi = fco.getParamInputA();
        assertEquals(String.class, fi.getType());
        assertEquals("plane", fi.getValue());
        assertEquals("!ui-name style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testRemoveBlank() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);

        fo.removeCondition(0);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(1, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());
    }

    @Test
    public void testRemoveFromMultipleSimpleCondition() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.FALSE);
        fo.addSimpleCondition(0, FilterConditionType.BETWEEN, "costPrice", "20.45", "42.10", Editable.FALSE);

        fo.removeCondition(1);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertTrue(fco.isEditable());
        assertEquals(0, fco.getDepth());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.BETWEEN, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:numberconditionlist"));
        assertEquals("costPrice", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(20.45), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertNotNull(fco.getParamInputB());
        fi = fco.getParamInputB();
        assertEquals(Double.class, fi.getType());
        assertEquals(Double.valueOf(42.10), fi.getValue());
        assertEquals("!ui-decimal precision:14 scale:2 style:$s{width:100%;}", fi.getEditor());
        assertEquals(1, fco.getDepth());
        assertFalse(fco.isEditable());
    }

    @Test
    public void testRemoveCompoundCondition() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addCompoundCondition(0, CompoundType.OR, Editable.FALSE);
        fo.addSimpleCondition(1, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45", Editable.FALSE);
        fo.addSimpleCondition(1, FilterConditionType.LESS_OR_EQUAL, "costPrice", "42.10", Editable.FALSE);
        fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.FALSE);

        fo.removeCondition(1);

        List<FilterCondition> conditionList = fo.getConditionList();
        assertNotNull(conditionList);
        assertEquals(2, conditionList.size());

        FilterCondition fco = conditionList.get(0);
        assertEquals(FilterConditionType.AND, fco.getType());
        assertNull(fco.getTypeSelector());
        assertNull(fco.getFieldName());
        assertNull(fco.getParamInputA());
        assertNull(fco.getParamInputB());
        assertEquals(0, fco.getDepth());
        assertTrue(fco.isEditable());

        fco = conditionList.get(1);
        assertEquals(FilterConditionType.LIKE, fco.getType());
        assertNotNull(fco.getTypeSelector());
        assertTrue(fco.getTypeSelector().contains("list:stringconditionlist"));
        assertEquals("name", fco.getFieldName());
        assertNotNull(fco.getParamInputA());
        AbstractInput<?> fi = fco.getParamInputA();
        assertEquals(String.class, fi.getType());
        assertEquals("plane", fi.getValue());
        assertEquals("!ui-name style:$s{width:100%;}", fi.getEditor());
        assertNull(fco.getParamInputB());
        assertEquals(1, fco.getDepth());
        assertFalse(fco.isEditable());
    }

    @Test
    public void testGetRestrictionBlankFilter() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNull(restriction);
    }

    @Test
    public void testGetRestrictionSimpleConditionZeroParam() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addSimpleCondition(0, FilterConditionType.IS_NULL, "costPrice", Editable.TRUE);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNotNull(restriction);
        assertEquals("$f{costPrice} is null", rt.translate(restriction));
        assertEquals("Cost Price is null", au.translate(restriction, ed));
    }

    @Test
    public void testGetRestrictionSimpleConditionSingleParam() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addSimpleCondition(0, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45", Editable.TRUE);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNotNull(restriction);
        assertEquals("$f{costPrice} >= 20.45", rt.translate(restriction));
        assertEquals("Cost Price >= 20.45", au.translate(restriction, ed));
    }

    @Test
    public void testGetRestrictionSimpleConditionDoubleParam() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addSimpleCondition(0, FilterConditionType.BETWEEN, "costPrice", "20.45", "42.10", Editable.TRUE);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNotNull(restriction);
        assertEquals("$f{costPrice} between (20.45, 42.1)", rt.translate(restriction));
        assertEquals("Cost Price between (20.45, 42.1)", au.translate(restriction, ed));
    }

    @Test
    public void testGetRestrictionMultipleSimpleCondition() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.TRUE);
        fo.addSimpleCondition(0, FilterConditionType.BETWEEN, "costPrice", "20.45", "42.10", Editable.TRUE);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNotNull(restriction);
        assertEquals("$f{name} like 'plane' AND $f{costPrice} between (20.45, 42.1)", rt.translate(restriction));
        assertEquals("Name like 'plane' AND Cost Price between (20.45, 42.1)", au.translate(restriction, ed));
    }

    @Test
    public void testGetRestrictionDeepConditionsSequential() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.TRUE);
        fo.addCompoundCondition(0, CompoundType.OR, Editable.TRUE);
        fo.addSimpleCondition(2, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45", Editable.TRUE);
        fo.addSimpleCondition(2, FilterConditionType.LESS_OR_EQUAL, "costPrice", "42.10", Editable.TRUE);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNotNull(restriction);
        assertEquals("$f{name} like 'plane' AND ($f{costPrice} >= 20.45 OR $f{costPrice} <= 42.1)",
                rt.translate(restriction));
        assertEquals("Name like 'plane' AND (Cost Price >= 20.45 OR Cost Price <= 42.1)",
                au.translate(restriction, ed));
    }

    @Test
    public void testGetRestrictionDeepConditionsRandom() throws Exception {
        Filter fo = new Filter(null, null, getEntityDef(), labelSuggestionDef, FilterConditionListType.IMMEDIATE_ONLY);
        fo.addCompoundCondition(0, CompoundType.OR, Editable.TRUE);
        fo.addSimpleCondition(1, FilterConditionType.GREATER_OR_EQUAL, "costPrice", "20.45", Editable.TRUE);
        fo.addSimpleCondition(1, FilterConditionType.LESS_OR_EQUAL, "costPrice", "42.10", Editable.TRUE);
        fo.addSimpleCondition(0, FilterConditionType.LIKE, "name", "plane", Editable.TRUE);
        CompoundRestriction restriction = fo.getRestriction(new Date());
        assertNotNull(restriction);
        assertEquals("($f{costPrice} >= 20.45 OR $f{costPrice} <= 42.1) AND $f{name} like 'plane'",
                rt.translate(restriction));
        assertEquals("(Cost Price >= 20.45 OR Cost Price <= 42.1) AND Name like 'plane'",
                au.translate(restriction, ed));
    }

    @Override
    protected void onSetup() throws Exception {
        rt = (RestrictionTranslator) getComponent(ApplicationComponents.APPLICATION_RESTRICTIONTRANSLATOR);
        au = (AppletUtilities) getComponent(ApplicationModuleNameConstants.APPLET_UTILITIES);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

    protected EntityDef getEntityDef() throws Exception {
        if (ed == null) {
            WidgetTypeDef amtWidgetTypeDef = new WidgetTypeDef(DataType.DOUBLE, InputType.DOUBLE, "application.amount",
                    "Amount", 1L, 1L, "!ui-decimal precision:14 scale:2", "!ui-label");
            WidgetTypeDef textWidgetTypeDef = new WidgetTypeDef(DataType.STRING, InputType.STRING, "application.text", "Text", 1L,
                    1L, "!ui-text", "!ui-label");
            ed = EntityDef
                    .newBuilder(ConfigType.STATIC, "com.flowcentraltech.flowcentral.application.web.widgets.Product",
                            "Product", null, null, null, false, false, false, false, "application.product", "Products", 1L, 1L)
                    .addFieldDef(textWidgetTypeDef, 
                            new WidgetTypeDef(DataType.STRING, InputType.STRING, "application.name", "Short Name", 1L,
                                    1L, "!ui-name", "!ui-label"),
                            
                            EntityFieldDataType.STRING, EntityFieldType.STATIC, "name", "Name")
                    .addFieldDef(textWidgetTypeDef,
                            new WidgetTypeDef(DataType.STRING, InputType.STRING, "application.text", "Text", 1L, 1L,
                                    "!ui-text", "!ui-label"),
                            EntityFieldDataType.STRING, EntityFieldType.STATIC, "description", "Description")
                    .addFieldDef(textWidgetTypeDef, amtWidgetTypeDef, EntityFieldDataType.DOUBLE, EntityFieldType.STATIC, "costPrice",
                            "Cost Price")
                    .addFieldDef(textWidgetTypeDef, amtWidgetTypeDef, EntityFieldDataType.DOUBLE, EntityFieldType.STATIC, "salesPrice",
                            "Sales Price")
                    .build();
        }

        return ed;
    }

}
