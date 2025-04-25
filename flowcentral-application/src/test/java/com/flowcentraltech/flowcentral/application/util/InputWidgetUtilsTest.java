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

package com.flowcentraltech.flowcentral.application.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterRestrictionConfig;
import com.tcdng.unify.core.AbstractUnifyComponentTest;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Between;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Tests input widget utilities class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class InputWidgetUtilsTest extends AbstractUnifyComponentTest {

    private AppletUtilities au;

    @Test
    public void testFilterDefinitionFromSimpleRestriction() throws UnifyException {
        String definition = InputWidgetUtils.getFilterDefinition(au, new IsNull("name"));
        assertTrue(Arrays.asList("NL]0]name]\r\n", "NL]0]name]\n").contains(definition));

        definition = InputWidgetUtils.getFilterDefinition(au, new Equals("name", "Amina"));
        assertTrue(Arrays.asList("EQ]0]name]Amina]\r\n", "EQ]0]name]Amina]\n").contains(definition));

        Calendar cal = Calendar.getInstance();
        cal.set(2021, 11, 25);
        Date date1 = CalendarUtils.getMidnightDate(cal.getTime());

        cal.set(2021, 10, 4);
        Date date2 = CalendarUtils.getMidnightDate(cal.getTime());

        definition = InputWidgetUtils.getFilterDefinition(au, new Between("birthDt", date1, date2));
        assertTrue(Arrays.asList("BT]0]birthDt]2021-12-25 00:00:00.000]2021-11-04 00:00:00.000]\r\n",
                "BT]0]birthDt]2021-12-25 00:00:00.000]2021-11-04 00:00:00.000]\n").contains(definition));

        definition = InputWidgetUtils.getFilterDefinition(au, new Amongst("name", Arrays.asList("Amina", "Zainab")));
        assertTrue(Arrays.asList("IN]0]name]Amina|Zainab]\r\n", "IN]0]name]Amina|Zainab]\n").contains(definition));
    }

    @Test
    public void testFilterDefinitionFromCompoundRestriction() throws UnifyException {
        String definition = InputWidgetUtils
                .getFilterDefinition(au, new And().add(new IsNull("name")).add(new Equals("name", "Amina")));
        assertTrue(
                Arrays.asList("AND]0]\r\nNL]1]name]\r\nEQ]1]name]Amina]\r\n", "AND]0]\nNL]1]name]\nEQ]1]name]Amina]\n")
                        .contains(definition));
    }

    @Test
    public void testFilterDefinitionFromCompoundRestrictionDeep() throws UnifyException {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 11, 25);
        Date date1 = CalendarUtils.getMidnightDate(cal.getTime());
        cal.set(2021, 10, 4);
        Date date2 = CalendarUtils.getMidnightDate(cal.getTime());
        String definition = InputWidgetUtils.getFilterDefinition(au, new And().add(new IsNull("name"))
                .add(new Equals("name", "Amina")).add(new Or().add(new Between("birthDt", date1, date2))
                        .add(new Amongst("name", Arrays.asList("Amina", "Zainab")))));
        assertTrue(Arrays.asList(
                "AND]0]\r\nNL]1]name]\r\nEQ]1]name]Amina]\r\nOR]1]\r\nBT]2]birthDt]2021-12-25 00:00:00.000]2021-11-04 00:00:00.000]\r\nIN]2]name]Amina|Zainab]\r\n",
                "AND]0]\nNL]1]name]\nEQ]1]name]Amina]\nOR]1]\nBT]2]birthDt]2021-12-25 00:00:00.000]2021-11-04 00:00:00.000]\nIN]2]name]Amina|Zainab]\n")
                .contains(definition));
    }

    @Test
    public void testGetFilterConfig() throws Exception {
        FilterConfig filterConfig = InputWidgetUtils
                .getFilterConfig(au, new AppAppletFilter("test1", "Test 1", "EQ]0]type]BSE]\r\n" + ""));
        assertNotNull(filterConfig);
        assertEquals("test1", filterConfig.getName());
        assertEquals("Test 1", filterConfig.getDescription());
        List<FilterRestrictionConfig> list = filterConfig.getRestrictionList();
        assertNotNull(list);
        assertEquals(1, list.size());

        FilterRestrictionConfig cfg = list.get(0);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.EQUALS, cfg.getType());
        assertEquals("type", cfg.getField());
        assertEquals("BSE", cfg.getParamA());
        assertNull(cfg.getParamB());

        filterConfig = InputWidgetUtils.getFilterConfig(au, new AppAppletFilter("test2", "Test 2",
                "AND]0]\r\n" + "IN]1]type]MEL|MEA|CEN|DIM]\r\n" + "NL]1]entity]\r\n" + ""));
        assertNotNull(filterConfig);
        assertEquals("test2", filterConfig.getName());
        assertEquals("Test 2", filterConfig.getDescription());
        list = filterConfig.getRestrictionList();
        assertNotNull(list);
        assertEquals(1, list.size());

        cfg = list.get(0);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.AND, cfg.getType());
        assertNull(cfg.getField());
        assertNull(cfg.getParamA());
        assertNull(cfg.getParamB());

        list = cfg.getRestrictionList();
        assertNotNull(list);
        assertEquals(2, list.size());

        cfg = list.get(0);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.AMONGST, cfg.getType());
        assertEquals("type", cfg.getField());
        assertEquals("MEL|MEA|CEN|DIM", cfg.getParamA());
        assertNull(cfg.getParamB());

        cfg = list.get(1);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.IS_NULL, cfg.getType());
        assertEquals("entity", cfg.getField());
        assertNull(cfg.getParamA());
        assertNull(cfg.getParamB());

        filterConfig = InputWidgetUtils
                .getFilterConfig(au, new AppAppletFilter("test3", "Test 3", "AND]0]\r\n" + "EQ]1]category]SHT]\r\n"
                        + "OR]1]\r\n" + "GT]2]costPrice]30.00]\r\n" + "GT]2]salesPrice]50.00]\r\n" + ""));
        assertNotNull(filterConfig);
        assertEquals("test3", filterConfig.getName());
        assertEquals("Test 3", filterConfig.getDescription());
        list = filterConfig.getRestrictionList();
        assertNotNull(list);
        assertEquals(1, list.size());

        cfg = list.get(0);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.AND, cfg.getType());
        assertNull(cfg.getField());
        assertNull(cfg.getParamA());
        assertNull(cfg.getParamB());

        list = cfg.getRestrictionList();
        assertNotNull(list);
        assertEquals(2, list.size());

        cfg = list.get(0);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.EQUALS, cfg.getType());
        assertEquals("category", cfg.getField());
        assertEquals("SHT", cfg.getParamA());
        assertNull(cfg.getParamB());

        cfg = list.get(1);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.OR, cfg.getType());
        assertNull(cfg.getField());
        assertNull(cfg.getParamA());
        assertNull(cfg.getParamB());

        list = cfg.getRestrictionList();
        assertNotNull(list);
        assertEquals(2, list.size());

        cfg = list.get(0);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.GREATER_THAN, cfg.getType());
        assertEquals("costPrice", cfg.getField());
        assertEquals("30.00", cfg.getParamA());
        assertNull(cfg.getParamB());

        cfg = list.get(1);
        assertNotNull(cfg);
        assertEquals(FilterConditionType.GREATER_THAN, cfg.getType());
        assertEquals("salesPrice", cfg.getField());
        assertEquals("50.00", cfg.getParamA());
        assertNull(cfg.getParamB());
    }

    @Override
    protected void onSetup() throws Exception {
        au = (AppletUtilities) getComponent(ApplicationModuleNameConstants.APPLET_UTILITIES);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}
