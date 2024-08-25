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
package com.flowcentraltech.flowcentral.application.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.AppletPropDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Application module service tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationModuleServiceTest extends AbstractFlowCentralTest {

    private ApplicationModuleService ams;

    private AppletUtilities au;

    @Test
    public void testIsComponentPresent() throws Exception {
        assertFalse(ams.isComponentPresent(AppEntity.class, "manageProduct.promotion"));
        assertFalse(ams.isComponentPresent(AppTable.class, "manageProduct.promotionTable"));
        assertFalse(ams.isComponentPresent(AppForm.class, "manageProduct.promotionForm"));
        assertFalse(ams.isComponentPresent(AppApplet.class, "manageProduct.promotionProduct"));

        assertTrue(ams.isComponentPresent(AppEntity.class, "manageProduct.product"));
        assertTrue(ams.isComponentPresent(AppTable.class, "manageProduct.productTable"));
        assertTrue(ams.isComponentPresent(AppForm.class, "manageProduct.productForm"));
        assertTrue(ams.isComponentPresent(AppApplet.class, "manageProduct.manageProduct"));
    }

    @Test
    public void testGetAppletDefs() throws Exception {
        List<AppletDef> appletList = ams.getAppletDefs("manageProduct");
        assertNotNull(appletList);
        assertEquals(1, appletList.size());

        AppletDef appletDef = appletList.get(0);
        assertNotNull(appletDef);
        assertEquals("manageProduct", appletDef.getName());
        assertEquals("Manage Product", appletDef.getDescription());
        assertNotNull(appletDef.getPropDefList());
        assertEquals(1, appletDef.getPropDefList().size());
    }

    @Test
    public void testGetAppletDef() throws Exception {
        AppletDef appletDef = ams.getAppletDef("manageProduct.manageProduct");
        assertNotNull(appletDef);
        assertEquals("manageProduct", appletDef.getName());
        assertEquals("Manage Product", appletDef.getDescription());

        AppletPropDef appletPropDef = appletDef.getPropDef("searchTable");
        assertNotNull(appletPropDef);
        assertEquals("searchTable", appletPropDef.getName());
        assertEquals("manageProduct.productTable", appletPropDef.getValue());
        assertEquals("manageProduct.productTable", appletPropDef.getValue(String.class));

        assertEquals("manageProduct.productTable", appletDef.getPropValue(String.class, "searchTable"));
    }

    @Test
    public void testGetWidgetTypeDef() throws Exception {
        WidgetTypeDef widgetTypeDef = ams.getWidgetTypeDef("manageProduct.name");
        assertNotNull(widgetTypeDef);
        assertEquals("name", widgetTypeDef.getName());
        assertEquals("Short Name", widgetTypeDef.getDescription());
        assertEquals(EntityFieldDataType.STRING.dataType(), widgetTypeDef.getDataType());
        assertEquals(InputType.STRING, widgetTypeDef.getInputType());
        assertEquals("!ui-name", widgetTypeDef.getEditor());
        assertEquals("!ui-label", widgetTypeDef.getRenderer());

        widgetTypeDef = ams.getWidgetTypeDef("manageProduct.amount");
        assertNotNull(widgetTypeDef);
        assertEquals("amount", widgetTypeDef.getName());
        assertEquals("Amount", widgetTypeDef.getDescription());
        assertEquals(EntityFieldDataType.DOUBLE.dataType(), widgetTypeDef.getDataType());
        assertEquals(InputType.DOUBLE, widgetTypeDef.getInputType());
        assertEquals("!ui-decimal precision:14 scale:2", widgetTypeDef.getEditor());
        assertEquals("!ui-label formatter:$d{!decimalformat precision:14 scale:2} style:$s{text-align:right;}",
                widgetTypeDef.getRenderer());
    }

    @Test
    public void testGetWidgetTypeDefReuse() throws Exception {
        WidgetTypeDef widgetTypeDef1 = ams.getWidgetTypeDef("manageProduct.name");
        WidgetTypeDef widgetTypeDef2 = ams.getWidgetTypeDef("manageProduct.name");
        WidgetTypeDef widgetTypeDef3 = ams.getWidgetTypeDef("manageProduct.name");
        assertNotNull(widgetTypeDef1);
        assertNotNull(widgetTypeDef2);
        assertNotNull(widgetTypeDef3);
        assertTrue(widgetTypeDef1 == widgetTypeDef2);
        assertTrue(widgetTypeDef2 == widgetTypeDef3);
    }

    @Test
    public void testGetEntityDef() throws Exception {
        EntityClassDef entityClassDef = ams.getEntityClassDef("manageProduct.product");
        EntityDef entityDef = entityClassDef.getEntityDef();
        assertNotNull(entityDef);
        assertEquals("product", entityDef.getName());
        assertEquals("Product", entityDef.getDescription());
        assertEquals("com.flowcentraltech.flowcentral.application.entities.Product", entityClassDef.getEntityClass().getName());

        EntityFieldDef entityFieldDef = entityDef.getFieldDef("category");
        assertNotNull(entityFieldDef);
        assertEquals("category", entityFieldDef.getFieldName());
        assertEquals("Category", entityFieldDef.getFieldLabel());
        assertEquals(EntityFieldDataType.ENUM_REF, entityFieldDef.getDataType());
        assertEquals(EntityFieldType.STATIC, entityFieldDef.getType());
        assertEquals("prodcatlist", entityFieldDef.getInputWidgetTypeDef().getName());
        assertEquals("productcategorylist", entityFieldDef.getReferences());

        entityFieldDef = entityDef.getFieldDef("salesPrice");
        assertNotNull(entityFieldDef);
        assertEquals("salesPrice", entityFieldDef.getFieldName());
        assertEquals("Sales Price", entityFieldDef.getFieldLabel());
        assertEquals(EntityFieldDataType.DOUBLE, entityFieldDef.getDataType());
        assertEquals(EntityFieldType.STATIC, entityFieldDef.getType());
        assertEquals("amount", entityFieldDef.getInputWidgetTypeDef().getName());
        assertNull(entityFieldDef.getReferences());
    }

    @Test
    public void testGetEntityDefReuse() throws Exception {
        EntityDef entityDef1 = ams.getEntityDef("manageProduct.product");
        EntityDef entityDef2 = ams.getEntityDef("manageProduct.product");
        EntityDef entityDef3 = ams.getEntityDef("manageProduct.product");
        assertNotNull(entityDef1);
        assertNotNull(entityDef2);
        assertNotNull(entityDef3);
        assertTrue(entityDef1 == entityDef2);
        assertTrue(entityDef2 == entityDef3);
    }

    @Test
    public void testFilterDefGetRestriction() throws Exception {
        EntityDef entityDef = ams.getEntityDef("manageProduct.product");
        AppletDef appletDef = ams.getAppletDef("manageProduct.manageProduct");
        AppletFilterDef filterDef = appletDef.getFilterDef("expensiveShirt");
        assertNotNull(filterDef);

        Restriction restriction = filterDef.getFilterDef().getRestriction(entityDef, null,  au.getNow());
        assertNotNull(restriction);
        String filterTxt = au.translate(restriction, entityDef);
        assertEquals("Category == 'Shirts' AND (Cost Price > 30.0 OR Sales Price > 50.0)", filterTxt);
    }

    @Test
    public void testGetTableDef() throws Exception {
        TableDef tableDef = ams.getTableDef("manageProduct.productTable");
        EntityClassDef entityClassDef = ams.getEntityClassDef(tableDef.getEntityDef().getLongName());
        assertNotNull(tableDef);
        assertEquals("productTable", tableDef.getName());
        assertEquals("Product", tableDef.getDescription());
        assertEquals("com.flowcentraltech.flowcentral.application.entities.Product", entityClassDef.getEntityClass().getName());
        assertEquals(20, tableDef.getItemsPerPage());

        List<TableColumnDef> columnList = tableDef.getVisibleColumnDefList();
        assertNotNull(columnList);
        assertEquals(5, columnList.size());

        TableColumnDef tableColumnDef = columnList.get(0);
        assertNotNull(tableColumnDef);
        assertEquals("name", tableColumnDef.getFieldName());
        assertEquals("!ui-label binding:name", tableColumnDef.getCellRenderer());
        assertEquals("width:15%;", tableColumnDef.getHeaderStyle());

        tableColumnDef = columnList.get(1);
        assertNotNull(tableColumnDef);
        assertEquals("description", tableColumnDef.getFieldName());
        assertEquals("!ui-label binding:description", tableColumnDef.getCellRenderer());
        assertEquals("width:43%;", tableColumnDef.getHeaderStyle());

        tableColumnDef = columnList.get(2);
        assertNotNull(tableColumnDef);
        assertEquals("costPrice", tableColumnDef.getFieldName());
        assertEquals(
                "!ui-label formatter:$d{!decimalformat precision:14 scale:2} style:$s{text-align:right;} binding:costPrice",
                tableColumnDef.getCellRenderer());
        assertEquals("width:14%;", tableColumnDef.getHeaderStyle());

        tableColumnDef = columnList.get(3);
        assertNotNull(tableColumnDef);
        assertEquals("salesPrice", tableColumnDef.getFieldName());
        assertEquals(
                "!ui-label formatter:$d{!decimalformat precision:14 scale:2} style:$s{text-align:right;} binding:salesPrice",
                tableColumnDef.getCellRenderer());
        assertEquals("width:14%;", tableColumnDef.getHeaderStyle());

        tableColumnDef = columnList.get(4);
        assertNotNull(tableColumnDef);
        assertEquals("categoryDesc", tableColumnDef.getFieldName());
        assertEquals("!ui-label binding:categoryDesc", tableColumnDef.getCellRenderer());
        assertEquals("width:14%;", tableColumnDef.getHeaderStyle());
    }

    @Test
    public void testGetTableDefReuse() throws Exception {
        TableDef tableDef1 = ams.getTableDef("manageProduct.productTable");
        TableDef tableDef2 = ams.getTableDef("manageProduct.productTable");
        TableDef tableDef3 = ams.getTableDef("manageProduct.productTable");
        assertNotNull(tableDef1);
        assertNotNull(tableDef2);
        assertNotNull(tableDef3);
        assertTrue(tableDef1 == tableDef2);
        assertTrue(tableDef2 == tableDef3);
    }

    @Test
    public void testGetFormDef() throws Exception {
        FormDef formDef = ams.getFormDef("manageProduct.productForm");
        EntityClassDef entityClassDef = ams.getEntityClassDef(formDef.getEntityDef().getLongName());
        assertNotNull(formDef);
        assertEquals("productForm", formDef.getName());
        assertEquals("Product", formDef.getDescription());
        assertEquals("com.flowcentraltech.flowcentral.application.entities.Product", entityClassDef.getEntityClass().getName());
        assertEquals(1, formDef.getTabCount());

        FormTabDef formTabDef = formDef.getFormTabDef(0);
        assertNotNull(formTabDef);
        assertEquals("Basic Details", formTabDef.getLabel());
        List<FormSectionDef> sectionList = formTabDef.getFormSectionDefList();
        assertNotNull(sectionList);
        assertEquals(2, sectionList.size());

        FormSectionDef formSectionDef = sectionList.get(0);
        assertNotNull(formSectionDef);
        List<FormFieldDef> fieldList = formSectionDef.getFormFieldDefList();
        assertNotNull(fieldList);
        assertEquals(FormColumnsType.TYPE_2, formSectionDef.getColumns());
        assertEquals(4, fieldList.size());

        FormFieldDef formFieldDef = fieldList.get(0);
        assertNotNull(formFieldDef);
        assertEquals("name", formFieldDef.getFieldName());
        assertEquals("Name", formFieldDef.getFieldLabel());
        assertEquals("!ui-name binding:name style:$s{width:100%;}", formFieldDef.getRenderer());
        assertEquals(0, formFieldDef.getColumn());

        formFieldDef = fieldList.get(1);
        assertNotNull(formFieldDef);
        assertEquals("category", formFieldDef.getFieldName());
        assertEquals("Category", formFieldDef.getFieldLabel());
        assertEquals(
                "!ui-select list:$s{productcategorylist} blankOption:$m{blank.none} binding:category style:$s{width:100%;}",
                formFieldDef.getRenderer());
        assertEquals(0, formFieldDef.getColumn());

        formFieldDef = fieldList.get(2);
        assertNotNull(formFieldDef);
        assertEquals("costPrice", formFieldDef.getFieldName());
        assertEquals("Cost Price", formFieldDef.getFieldLabel());
        assertEquals("!ui-decimal precision:14 scale:2 binding:costPrice style:$s{width:100%;}",
                formFieldDef.getRenderer());
        assertEquals(1, formFieldDef.getColumn());

        formFieldDef = fieldList.get(3);
        assertNotNull(formFieldDef);
        assertEquals("salesPrice", formFieldDef.getFieldName());
        assertEquals("Sales Price", formFieldDef.getFieldLabel());
        assertEquals("!ui-decimal precision:14 scale:2 binding:salesPrice style:$s{width:100%;}",
                formFieldDef.getRenderer());
        assertEquals(1, formFieldDef.getColumn());

        formSectionDef = sectionList.get(1);
        assertNotNull(formSectionDef);
        fieldList = formSectionDef.getFormFieldDefList();
        assertNotNull(fieldList);
        assertEquals(FormColumnsType.TYPE_1, formSectionDef.getColumns());
        assertEquals(2, fieldList.size());

        formFieldDef = fieldList.get(0);
        assertNotNull(formFieldDef);
        assertEquals("description", formFieldDef.getFieldName());
        assertEquals("Description", formFieldDef.getFieldLabel());
        assertEquals("!ui-text binding:description style:$s{width:100%;}", formFieldDef.getRenderer());
        assertEquals(0, formFieldDef.getColumn());

        formFieldDef = fieldList.get(1);
        assertNotNull(formFieldDef);
        assertEquals("notes", formFieldDef.getFieldName());
        assertEquals("Notes", formFieldDef.getFieldLabel());
        assertEquals("!ui-textarea binding:notes style:$s{width:100%;}", formFieldDef.getRenderer());
        assertEquals(0, formFieldDef.getColumn());
    }

    @Test
    public void testGetFormDefReuse() throws Exception {
        FormDef formDef1 = ams.getFormDef("manageProduct.productForm");
        FormDef formDef2 = ams.getFormDef("manageProduct.productForm");
        FormDef formDef3 = ams.getFormDef("manageProduct.productForm");
        assertNotNull(formDef1);
        assertNotNull(formDef2);
        assertNotNull(formDef3);
        assertTrue(formDef1 == formDef2);
        assertTrue(formDef2 == formDef3);
    }

    @Override
    protected void onSetup() throws Exception {
        ams = (ApplicationModuleService) getComponent(ApplicationModuleNameConstants.APPLICATION_MODULE_SERVICE);
        au = (AppletUtilities) getComponent(ApplicationModuleNameConstants.APPLET_UTILITIES);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}
