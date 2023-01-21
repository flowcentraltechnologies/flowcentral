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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.application.entities.AppAppletPropQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAppletQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPage;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPageQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.AppFormElementQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormRelatedList;
import com.flowcentraltech.flowcentral.application.entities.AppFormRelatedListQuery;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppRefQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableQuery;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of application usage service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(ApplicationModuleNameConstants.APPLICATION_USAGE_SERVICE)
public class ApplicationUsageServiceImpl extends AbstractFlowCentralService implements UsageProvider {

    @Override
    public List<Usage> findApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        List<Usage> usageList = new ArrayList<Usage>();
        // App applet
        if (UsageType.isQualifiesApplet(usageType)) {
            List<AppApplet> appletList = environment().listAll(new AppAppletQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
            for (AppApplet appApplet : appletList) {
                Usage usage = new Usage(UsageType.APPLET, "AppApplet",
                        appApplet.getApplicationName() + "." + appApplet.getName(), "entity", appApplet.getEntity());
                usageList.add(usage);
            }

            List<AppAppletProp> appletPropList = environment().listAll(
                    new AppAppletPropQuery().applicationNameNot(applicationName).valueBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "appletName", "name", "value"));
            for (AppAppletProp appAppletProp : appletPropList) {
                Usage usage = new Usage(UsageType.APPLET, "AppAppletProp",
                        appAppletProp.getApplicationName() + "." + appAppletProp.getAppletName(),
                        appAppletProp.getName(), appAppletProp.getValue());
                usageList.add(usage);
            }

            List<AppAssignmentPage> appAssignmentPageList = environment()
                    .listAll(new AppAssignmentPageQuery().applicationNameNot(applicationName)
                            .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
            for (AppAssignmentPage appAssignmentPage : appAssignmentPageList) {
                Usage usage = new Usage(UsageType.APPLET, "AppAssignmentPage",
                        appAssignmentPage.getApplicationName() + "." + appAssignmentPage.getName(), "entity",
                        appAssignmentPage.getEntity());
                usageList.add(usage);
            }
        }

        // App entity field
        if (UsageType.isQualifiesEntity(usageType)) {
            List<AppEntityField> appEntityFieldList = environment().listAll(new AppEntityFieldQuery()
                    .applicationNameNot(applicationName).referencesBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "references"));
            for (AppEntityField appEntityField : appEntityFieldList) {
                Usage usage = new Usage(
                        UsageType.ENTITY, "AppEntityField", appEntityField.getApplicationName() + "."
                                + appEntityField.getAppEntityName() + "." + appEntityField.getName(),
                        "references", appEntityField.getReferences());
                usageList.add(usage);
            }

            appEntityFieldList = environment().listAll(new AppEntityFieldQuery().applicationNameNot(applicationName)
                    .inputWidgetBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "inputWidget"));
            for (AppEntityField appEntityField : appEntityFieldList) {
                Usage usage = new Usage(
                        UsageType.ENTITY, "AppEntityField", appEntityField.getApplicationName() + "."
                                + appEntityField.getAppEntityName() + "." + appEntityField.getName(),
                        "inputWidget", appEntityField.getInputWidget());
                usageList.add(usage);
            }

            appEntityFieldList = environment().listAll(new AppEntityFieldQuery().applicationNameNot(applicationName)
                    .suggestionTypeBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "suggestionType"));
            for (AppEntityField appEntityField : appEntityFieldList) {
                Usage usage = new Usage(
                        UsageType.ENTITY, "AppEntityField", appEntityField.getApplicationName() + "."
                                + appEntityField.getAppEntityName() + "." + appEntityField.getName(),
                        "suggestionType", appEntityField.getSuggestionType());
                usageList.add(usage);
            }

            appEntityFieldList = environment().listAll(new AppEntityFieldQuery().applicationNameNot(applicationName)
                    .lingualWidgetBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "lingualWidget"));
            for (AppEntityField appEntityField : appEntityFieldList) {
                Usage usage = new Usage(
                        UsageType.ENTITY, "AppEntityField", appEntityField.getApplicationName() + "."
                                + appEntityField.getAppEntityName() + "." + appEntityField.getName(),
                        "lingualWidget", appEntityField.getLingualWidget());
                usageList.add(usage);
            }
        }

        // App form
        if (UsageType.isQualifiesForm(usageType)) {
            List<AppForm> appFormList = environment().listAll(new AppFormQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
            for (AppForm appForm : appFormList) {
                Usage usage = new Usage(UsageType.FORM, "AppForm", appForm.getApplicationName(),
                        appForm.getName() + "." + "entity", appForm.getEntity());
                usageList.add(usage);
            }

            List<AppFormElement> appFormElementList = environment().listAll(new AppFormElementQuery()
                    .applicationNameNot(applicationName).tabAppletBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "tabApplet"));
            for (AppFormElement appFormElement : appFormElementList) {
                Usage usage = new Usage(
                        UsageType.FORM, "AppFormElement", appFormElement.getApplicationName() + "."
                                + appFormElement.getAppFormName() + "." + appFormElement.getElementName(),
                        "tabApplet", appFormElement.getTabApplet());
                usageList.add(usage);
            }

            appFormElementList = environment().listAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .tabReferenceBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "tabReference"));
            for (AppFormElement appFormElement : appFormElementList) {
                Usage usage = new Usage(
                        UsageType.FORM, "AppFormElement", appFormElement.getApplicationName() + "."
                                + appFormElement.getAppFormName() + "." + appFormElement.getElementName(),
                        "tabReference", appFormElement.getTabReference());
                usageList.add(usage);
            }

            appFormElementList = environment().listAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .tabMappedFormBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "tabMappedForm"));
            for (AppFormElement appFormElement : appFormElementList) {
                Usage usage = new Usage(
                        UsageType.FORM, "AppFormElement", appFormElement.getApplicationName() + "."
                                + appFormElement.getAppFormName() + "." + appFormElement.getElementName(),
                        "tabMappedForm", appFormElement.getTabMappedForm());
                usageList.add(usage);
            }

            appFormElementList = environment().listAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .inputReferenceBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "inputReference"));
            for (AppFormElement appFormElement : appFormElementList) {
                Usage usage = new Usage(UsageType.FORM, "AppFormElement",
                        appFormElement.getApplicationName() + "." + appFormElement.getAppFormName() + "."
                                + appFormElement.getElementName(),
                        "inputReference", appFormElement.getInputReference());
                usageList.add(usage);
            }

            appFormElementList = environment().listAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .inputWidgetBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "inputWidget"));
            for (AppFormElement appFormElement : appFormElementList) {
                Usage usage = new Usage(
                        UsageType.FORM, "AppFormElement", appFormElement.getApplicationName() + "."
                                + appFormElement.getAppFormName() + "." + appFormElement.getElementName(),
                        "inputWidget", appFormElement.getInputWidget());
                usageList.add(usage);
            }

            List<AppFormRelatedList> appFormRelatedList = environment().listAll(new AppFormRelatedListQuery()
                    .applicationNameNot(applicationName).appletBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "name", "applet"));
            for (AppFormRelatedList appFormElement : appFormRelatedList) {
                Usage usage = new Usage(
                        UsageType.FORM, "AppFormRelatedList", appFormElement.getApplicationName() + "."
                                + appFormElement.getAppFormName() + "." + appFormElement.getName(),
                        "applet", appFormElement.getApplet());
                usageList.add(usage);
            }
        }

        // App ref
        if (UsageType.isQualifiesRef(usageType)) {
            List<AppRef> appRefList = environment().listAll(new AppRefQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
            for (AppRef appRef : appRefList) {
                Usage usage = new Usage(UsageType.REF, "AppRef", appRef.getApplicationName(),
                        appRef.getName() + "." + "entity", appRef.getEntity());
                usageList.add(usage);
            }
        }

        // App table
        if (UsageType.isQualifiesTable(usageType)) {
            List<AppTable> appTableList = environment().listAll(new AppTableQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
            for (AppTable appTable : appTableList) {
                Usage usage = new Usage(UsageType.TABLE, "AppTable", appTable.getApplicationName(),
                        appTable.getName() + "." + "entity", appTable.getEntity());
                usageList.add(usage);
            }
        }

        return usageList;
    }

    @Override
    public long countApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        long usages = 0L;
        // App applet
        if (UsageType.isQualifiesApplet(usageType)) {
            usages += environment().countAll(new AppAppletQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
 
            usages += environment().countAll(
                    new AppAppletPropQuery().applicationNameNot(applicationName).valueBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "appletName", "name", "value"));
 
            usages += environment()
                    .countAll(new AppAssignmentPageQuery().applicationNameNot(applicationName)
                            .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
        }

        // App entity field
        if (UsageType.isQualifiesEntity(usageType)) {
            usages +=  environment().countAll(new AppEntityFieldQuery()
                    .applicationNameNot(applicationName).referencesBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "references"));
 
            usages +=  environment().countAll(new AppEntityFieldQuery().applicationNameNot(applicationName)
                    .inputWidgetBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "inputWidget"));
 
            usages +=  environment().countAll(new AppEntityFieldQuery().applicationNameNot(applicationName)
                    .suggestionTypeBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "suggestionType"));
 
            usages += environment().countAll(new AppEntityFieldQuery().applicationNameNot(applicationName)
                    .lingualWidgetBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appEntityName", "name", "lingualWidget"));
         }

        // App form
        if (UsageType.isQualifiesForm(usageType)) {
            usages +=  environment().countAll(new AppFormQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));

            usages +=   environment().countAll(new AppFormElementQuery()
                    .applicationNameNot(applicationName).tabAppletBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "tabApplet"));

            usages +=   environment().countAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .tabReferenceBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "tabReference"));

            usages +=   environment().countAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .tabMappedFormBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "tabMappedForm"));

            usages +=   environment().countAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .inputReferenceBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "inputReference"));

            usages +=  environment().countAll(new AppFormElementQuery().applicationNameNot(applicationName)
                    .inputWidgetBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "elementName", "inputWidget"));

            usages +=   environment().countAll(new AppFormRelatedListQuery()
                    .applicationNameNot(applicationName).appletBeginsWith(applicationNameBase)
                    .addSelect("applicationName", "appFormName", "name", "applet"));
        }

        // App ref
        if (UsageType.isQualifiesRef(usageType)) {
            usages +=  environment().countAll(new AppRefQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
        }

        // App table
        if (UsageType.isQualifiesTable(usageType)) {
            usages +=  environment().countAll(new AppTableQuery().applicationNameNot(applicationName)
                    .entityBeginsWith(applicationNameBase).addSelect("applicationName", "name", "entity"));
        }

        return usages;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }
}
