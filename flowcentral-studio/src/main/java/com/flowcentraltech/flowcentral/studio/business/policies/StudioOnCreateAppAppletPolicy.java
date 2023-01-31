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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPage;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableColumn;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio on create application applet policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiooncreateappapplet-policy")
public class StudioOnCreateAppAppletPolicy extends StudioOnCreateComponentPolicy {

    private static final int MAX_DEFAULT_TABLE_COLUMNS = 8;

    private final Set<String> skipTableColumn = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("id",
            "versionNo", "originWorkRecId", "inWorkflow", "workBranchCode", "workDepartmentCode", "processingStatus")));

    private final Set<String> skipFormField = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("id", "versionNo", "createDt", "createdBy", "updateDt", "updatedBy",
                    "originWorkRecId", "inWorkflow", "workBranchCode", "workDepartmentCode", "processingStatus")));

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        final String applicationName = (String) getSessionAttribute(
                StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        final Long applicationId = (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        final AppApplet appApplet = (AppApplet) ctx.getInst();
        if (!appApplet.isIdBlank()) {
        	return null;
        }
        
        final AppletType type = appApplet.getType();
        if (AppletType.DATA_IMPORT.equals(type)) {
            appApplet.setIcon("file-import");
            return null;
        }
        
        if (type.isCreate() || type.isEntityList()) {
            final String entity = appApplet.getEntity();
            EntityDef entityDef = application().getEntityDef(entity);

            String tableName = null;
            if (type.isEntityList()) {
                List<String> existingTables = application().findEntityAppTables(entity);
                if (!DataUtils.isBlank(existingTables)) {
                    // Choose the first existing table
                    tableName = existingTables.get(0);
                } else {
                    // Generate and create table
                    tableName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                            appApplet.getName() + "_Tbl");
                    String tableDesc = appApplet.getLabel() + " Table";
                    ApplicationEntityNameParts tnp = ApplicationNameUtils.getApplicationEntityNameParts(tableName);
                    AppTable appTable = new AppTable();
                    appTable.setApplicationId(applicationId);
                    appTable.setEntity(entity);
                    appTable.setName(tnp.getEntityName());
                    appTable.setDescription(tableDesc);
                    appTable.setLabel(tableDesc);
                    appTable.setSortHistory(-1);
                    appTable.setItemsPerPage(20);
                    appTable.setSortable(true);
                    appTable.setHeaderToUpperCase(false);
                    appTable.setHeaderCenterAlign(false);
                    appTable.setBasicSearch(false);
                    appTable.setLimitSelectToColumns(true);
                    List<AppTableColumn> columnList = new ArrayList<AppTableColumn>();
                    for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                        if (entityFieldDef.isTableViewable() && !skipTableColumn.contains(entityFieldDef.getFieldName())) {
                            AppTableColumn appTableColumn = new AppTableColumn();
                            appTableColumn.setField(entityFieldDef.getFieldName());
                            appTableColumn.setLabel(null);
                            String widget = InputWidgetUtils.resolveEntityFieldWidget(entityFieldDef);
                            appTableColumn.setRenderWidget(widget);

                            if (entityFieldDef.isMaintainLink()) {
                                appTableColumn.setLinkAct("maintainAct");
                            }

                            appTableColumn.setWidthRatio(1);
                            appTableColumn.setSortable(true);
                            columnList.add(appTableColumn);

                            if (columnList.size() >= MAX_DEFAULT_TABLE_COLUMNS) {
                                break;
                            }
                        }
                    }
                    appTable.setColumnList(columnList);
                    application().createAppTable(appTable);
                }
            }

            List<AppAppletProp> appletPropList = new ArrayList<AppAppletProp>();
            if (type.isAssignment()) {
                // Generate assignment page
                String assignmentPageName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                        appApplet.getName() + "_Asn");
                String assignmentPageDesc = appApplet.getLabel() + " Assignment";
                ApplicationEntityNameParts anp = ApplicationNameUtils.getApplicationEntityNameParts(assignmentPageName);
                AppAssignmentPage appAssignmentPage = new AppAssignmentPage();
                appAssignmentPage.setApplicationId(applicationId);
                appAssignmentPage.setName(anp.getEntityName());
                appAssignmentPage.setDescription(assignmentPageDesc);
                appAssignmentPage.setLabel(assignmentPageDesc);
                EntityFieldDef entityFieldDef = entityDef.getFieldDef(appApplet.getAssignField());
                RefDef refDef = entityFieldDef.getRefDef();
                EntityDef refEntityDef = application().getEntityDef(refDef.getEntity());
                appAssignmentPage
                        .setAssignCaption(resolveApplicationMessage("Assigned " + refEntityDef.getLabel()));
                appAssignmentPage.setAssignList("entityinlist");
                appAssignmentPage
                        .setUnassignCaption(resolveApplicationMessage("Available " + refEntityDef.getLabel()));
                appAssignmentPage.setUnassignList("entitynotinlist");
                appAssignmentPage.setEntity(entity);
                appAssignmentPage.setBaseField(appApplet.getBaseField());
                appAssignmentPage.setAssignField(appApplet.getAssignField());
                appAssignmentPage.setRuleDescField(appApplet.getAssignDescField());
                appAssignmentPage.setConfigType(ConfigType.CUSTOM);
                application().createAppAssignmentPage(appAssignmentPage);
                
                // Add applet properties
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE, tableName));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_EDIT, "true"));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.ASSIGNMENT_PAGE, anp.getLongName()));
            } else {
                if (type.isSingleForm()) {
                    // TODO
                } else {
                    String formName = null;
                    List<String> existingForms = application().findEntityAppForms(entity);
                    if (!DataUtils.isBlank(existingForms)) {
                        // Choose first existing form
                        formName = existingForms.get(0);
                    } else {
                        // Generate and create form
                        formName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                                appApplet.getName() + "_Frm");
                        String formDesc = appApplet.getLabel() + " Form";
                        ApplicationEntityNameParts fnp = ApplicationNameUtils.getApplicationEntityNameParts(formName);
                        AppForm appForm = new AppForm();
                        appForm.setApplicationId(applicationId);
                        appForm.setType(FormType.INPUT);
                        appForm.setEntity(entity);
                        appForm.setName(fnp.getEntityName());
                        appForm.setDescription(formDesc);
                        List<AppFormElement> elementList = new ArrayList<AppFormElement>();
                        // Basic Tab
                        AppFormElement appFormElement = new AppFormElement();
                        appFormElement.setType(FormElementType.TAB); // Basic Tab
                        appFormElement.setElementName("basicDetails");
                        appFormElement.setTabContentType(TabContentType.MINIFORM);
                        appFormElement.setLabel("Basic Details");
                        appFormElement.setVisible(true);
                        appFormElement.setEditable(true);
                        appFormElement.setDisabled(false);
                        elementList.add(appFormElement);

                        // Basic Section
                        appFormElement = new AppFormElement();
                        appFormElement.setType(FormElementType.SECTION);
                        appFormElement.setElementName("basicDetails");
                        appFormElement.setSectionColumns(FormColumnsType.TYPE_2);
                        appFormElement.setLabel(null);
                        appFormElement.setVisible(true);
                        appFormElement.setEditable(true);
                        appFormElement.setDisabled(false);
                        elementList.add(appFormElement);

                        // Form
                        int column = 0;
                        List<EntityFieldDef> childList = null;
                        for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                            if (entityFieldDef.isFormViewable() && !entityFieldDef.isListOnly()
                                    && !skipFormField.contains(entityFieldDef.getFieldName())) {
                                appFormElement = new AppFormElement();
                                appFormElement.setType(FormElementType.FIELD);
                                appFormElement.setElementName(entityFieldDef.getFieldName());
                                appFormElement.setLabel(null);
                                if (!entityFieldDef.isWithInputWidget()) {
                                    String widget = InputWidgetUtils.getDefaultEntityFieldWidget(entityFieldDef.getDataType());
                                    if (widget != null) {
                                        appFormElement.setInputWidget(widget);
                                    } else {
                                        appFormElement.setInputWidget("application.text");
                                    }
                                }

                                appFormElement.setFieldColumn(column % 2);
                                appFormElement.setRequired(!entityFieldDef.isBoolean() && !entityFieldDef.isNullable());
                                appFormElement.setVisible(true);
                                appFormElement.setEditable(true);
                                appFormElement.setDisabled(false);
                                elementList.add(appFormElement);
                                column++;
                            } else if (entityFieldDef.isChild() || entityFieldDef.isChildList()) {
                                if (childList == null) {
                                    childList = new ArrayList<EntityFieldDef>();
                                }

                                childList.add(entityFieldDef);
                            }
                        }

                        // Child and child list tabs
                        if (childList != null) {
                            for (EntityFieldDef entityFieldDef : childList) {
                                appFormElement = new AppFormElement();
                                appFormElement.setType(FormElementType.TAB);
                                appFormElement.setElementName(entityFieldDef.getFieldName());
                                TabContentType childType = entityFieldDef.isChild() ? TabContentType.CHILD
                                        : TabContentType.CHILD_LIST;
                                appFormElement.setTabContentType(childType);
                                appFormElement.setLabel(entityFieldDef.getFieldLabel());
                                appFormElement.setVisible(true);
                                appFormElement.setEditable(true);
                                appFormElement.setDisabled(false);
                                appFormElement.setTabReference(entityFieldDef.getFieldName());
                                List<AppApplet> appletList = application()
                                        .findManageEntityListApplets(entityFieldDef.getRefDef().getEntity());
                                if (!DataUtils.isBlank(appletList)) {
                                    AppApplet _childAppApplet = appletList.get(0);
                                    appFormElement.setTabApplet(ApplicationNameUtils.getApplicationEntityLongName(
                                            _childAppApplet.getApplicationName(), _childAppApplet.getName()));
                                    elementList.add(appFormElement);
                                }
                            }
                        }

                        // Change log Tab
                        if (entityDef.getBaseType().isAuditType()) {
                            appFormElement = new AppFormElement();
                            appFormElement.setType(FormElementType.TAB); // Change log
                            appFormElement.setElementName("changeLog");
                            appFormElement.setTabContentType(TabContentType.MINIFORM_CHANGELOG);
                            appFormElement.setLabel(resolveSessionMessage("$m{application.form.audit}"));
                            appFormElement.setVisible(true);
                            appFormElement.setEditable(true);
                            appFormElement.setDisabled(false);
                            elementList.add(appFormElement);
                            ApplicationEntityUtils.addChangeLogFormElements(elementList);
                        }

                        appForm.setElementList(elementList);
                        application().createAppForm(appForm);
                    }
                    
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM, formName));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.MAINTAIN_FORM, formName));
                }
                
                // Add applet properties
                if (type.isEntityList()) {
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE, tableName));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_NEW, "true"));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_QUICKFILTER, "true"));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_BASICSEARCHONLY, "true"));
                    if (entityDef.isReportable()) {
                        appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_REPORT, "true"));
                    }

                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM_SAVE_CLOSE, "true"));
                }

                appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM_SAVE, "true"));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM_SAVE_NEXT, "true"));

                appletPropList.add(new AppAppletProp(AppletPropertyConstants.MAINTAIN_FORM_UPDATE, "true"));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.MAINTAIN_FORM_DELETE, "true"));
            }

            appApplet.setPropList(appletPropList);
        }
        
        return null;
    }

}
