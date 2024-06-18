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

package com.flowcentraltech.flowcentral.report.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.annotation.Format;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.data.ReportInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppEntityConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ParameterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportColumnConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportPlacementConfig;
import com.flowcentraltech.flowcentral.report.constants.ReportModuleNameConstants;
import com.flowcentraltech.flowcentral.report.entities.ReportColumn;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportConfigurationQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportParameter;
import com.flowcentraltech.flowcentral.report.entities.ReportPlacement;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.EntityTypeUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application reports installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ReportModuleNameConstants.APPLICATION_REPORT_INSTALLER)
public class ApplicationReportInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Configurable
    private MessageResolver messageResolver;

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Long applicationId = applicationInstall.getApplicationId();
        final String applicationName = applicationInstall.getApplicationConfig().getName();

        logDebug(taskMonitor, "Executing report installer...");
        // Install reports for configurable entities
        logDebug(taskMonitor, "Installing reportable entities...");
        environment().updateAll(new ReportableDefinitionQuery().applicationId(applicationId).isNotActualCustom(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getEntitiesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEntitiesConfig().getEntityList())) {
            for (AppEntityConfig appEntityConfig : applicationConfig.getEntitiesConfig().getEntityList()) {
                if (appEntityConfig.getReportable()) {
                    String description = resolveApplicationMessage("$m{report.managedreport.description}",
                            resolveApplicationMessage(appEntityConfig.getDescription()));
                    String entity = ApplicationNameUtils.ensureLongNameReference(applicationName,
                            appEntityConfig.getName());
                    logDebug(taskMonitor, "Installing managed reportable [{0}]...", description);
                    ReportableDefinition oldReportableDefinition = environment()
                            .findLean(new ReportableDefinitionQuery().applicationId(applicationId)
                                    .name(appEntityConfig.getName()));
                    if (oldReportableDefinition == null) {
                        ReportableDefinition reportableDefinition = new ReportableDefinition();
                        reportableDefinition.setApplicationId(applicationId);
                        reportableDefinition.setName(appEntityConfig.getName());
                        reportableDefinition.setEntity(entity);
                        reportableDefinition.setTitle(description);
                        reportableDefinition.setDescription(description);
                        reportableDefinition.setDeprecated(false);
                        reportableDefinition.setConfigType(ConfigType.STATIC_INSTALL);
                        populateChildList(appEntityConfig, reportableDefinition);
                        environment().create(reportableDefinition);
                    } else {
                        // Update old definition
                        if (ConfigUtils.isSetInstall(oldReportableDefinition)) {
                            oldReportableDefinition.setEntity(entity);
                            oldReportableDefinition.setTitle(description);
                            oldReportableDefinition.setDescription(description);
                        }

                        oldReportableDefinition.setDeprecated(false);
                        populateChildList(appEntityConfig, oldReportableDefinition);
                        environment().updateByIdVersion(oldReportableDefinition);
                        oldReportableDefinition.getId();
                    }

                    registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE,
                            PrivilegeNameUtils.getReportablePrivilegeName(ApplicationNameUtils
                                    .ensureLongNameReference(applicationName, appEntityConfig.getName())),
                            description);
                }
            }
        }

        // Install configured reports
        environment().updateAll(new ReportConfigurationQuery().applicationId(applicationId).isNotActualCustom(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getReportsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getReportsConfig().getReportList())) {
            for (AppReportConfig applicationReportConfig : applicationConfig.getReportsConfig().getReportList()) {
                ReportInstall reportInstall = getConfigurationLoader()
                        .loadReportInstallation(applicationReportConfig.getConfigFile());
                ReportConfig reportConfig = reportInstall.getReportConfig();
                String description = resolveApplicationMessage(reportConfig.getDescription());
                logDebug(taskMonitor, "Installing configured report [{0}]...", description);
                String title = reportConfig.getTitle();
                if (title == null) {
                    title = description;
                }

                ReportConfiguration oldReportConfiguration = environment().findLean(
                        new ReportConfigurationQuery().applicationId(applicationId).name(reportConfig.getName()));
                String reportable = ApplicationNameUtils.ensureLongNameReference(applicationName,
                        reportConfig.getReportable());

                if (oldReportConfiguration == null) {
                    ReportConfiguration reportConfiguration = new ReportConfiguration();
                    reportConfiguration.setApplicationId(applicationId);
                    reportConfiguration.setType(reportConfig.getType());
                    reportConfiguration.setSummaryDatasource(reportConfig.getSummaryDatasource());
                    reportConfiguration.setSizeType(reportConfig.getSizeType());
                    reportConfiguration.setName(reportConfig.getName());
                    reportConfiguration.setDescription(description);
                    reportConfiguration.setReportable(reportable);
                    reportConfiguration.setTitle(title);
                    reportConfiguration.setTemplate(reportConfig.getTemplate());
                    reportConfiguration.setWidth(reportConfig.getWidth());
                    reportConfiguration.setHeight(reportConfig.getHeight());
                    reportConfiguration.setMarginBottom(reportConfig.getMarginBottom());
                    reportConfiguration.setMarginLeft(reportConfig.getMarginLeft());
                    reportConfiguration.setMarginRight(reportConfig.getMarginRight());
                    reportConfiguration.setMarginTop(reportConfig.getMarginTop());
                    reportConfiguration.setProcessor(reportConfig.getProcessor());
                    reportConfiguration.setLetterGenerator(reportConfig.getLetterGenerator());
                    reportConfiguration.setShowGrandFooter(reportConfig.getShowGrandFooter());
                    reportConfiguration.setInvertGroupColors(reportConfig.getInvertGroupColors());
                    reportConfiguration.setLandscape(reportConfig.getLandscape());
                    reportConfiguration.setShadeOddRows(reportConfig.getShadeOddRows());
                    reportConfiguration.setUnderlineRows(reportConfig.getUnderlineRows());
                    reportConfiguration.setAllowSecondaryTenants(reportConfig.getAllowSecondaryTenants());
                    reportConfiguration.setFilter(InputWidgetUtils.newAppFilter(reportConfig.getFilter()));
                    reportConfiguration.setDeprecated(false);
                    reportConfiguration.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(reportConfig, reportConfiguration);
                    environment().create(reportConfiguration);
                } else {
                    if (ConfigUtils.isSetInstall(oldReportConfiguration)) {
                        oldReportConfiguration.setType(reportConfig.getType());
                        oldReportConfiguration.setSummaryDatasource(reportConfig.getSummaryDatasource());
                        oldReportConfiguration.setSizeType(reportConfig.getSizeType());
                        oldReportConfiguration.setDescription(description);
                        oldReportConfiguration.setReportable(reportable);
                        oldReportConfiguration.setTitle(title);
                        oldReportConfiguration.setTemplate(reportConfig.getTemplate());
                        oldReportConfiguration.setWidth(reportConfig.getWidth());
                        oldReportConfiguration.setHeight(reportConfig.getHeight());
                        oldReportConfiguration.setMarginBottom(reportConfig.getMarginBottom());
                        oldReportConfiguration.setMarginLeft(reportConfig.getMarginLeft());
                        oldReportConfiguration.setMarginRight(reportConfig.getMarginRight());
                        oldReportConfiguration.setMarginTop(reportConfig.getMarginTop());
                        oldReportConfiguration.setProcessor(reportConfig.getProcessor());
                        oldReportConfiguration.setLetterGenerator(reportConfig.getLetterGenerator());
                        oldReportConfiguration.setShowGrandFooter(reportConfig.getShowGrandFooter());
                        oldReportConfiguration.setInvertGroupColors(reportConfig.getInvertGroupColors());
                        oldReportConfiguration.setLandscape(reportConfig.getLandscape());
                        oldReportConfiguration.setShadeOddRows(reportConfig.getShadeOddRows());
                        oldReportConfiguration.setUnderlineRows(reportConfig.getUnderlineRows());
                        oldReportConfiguration.setAllowSecondaryTenants(reportConfig.getAllowSecondaryTenants());
                        oldReportConfiguration.setFilter(InputWidgetUtils.newAppFilter(reportConfig.getFilter()));
                    }

                    oldReportConfiguration.setDeprecated(false);
                    populateChildList(reportConfig, oldReportConfiguration);
                    environment().updateByIdVersion(oldReportConfiguration);
                }

                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTCONFIG_CATEGORY_CODE,
                        PrivilegeNameUtils.getReportConfigPrivilegeName(
                                ApplicationNameUtils.ensureLongNameReference(applicationName, reportConfig.getName())),
                        description);
            }
        }
    }

    @Override
    public void restoreApplicationArtifacts(TaskMonitor taskMonitor, ApplicationRestore applicationRestore)
            throws UnifyException {
        final AppConfig applicationConfig = applicationRestore.getApplicationConfig();
        final Long applicationId = applicationRestore.getApplicationId();
        final String applicationName = applicationRestore.getApplicationConfig().getName();

        logDebug(taskMonitor, "Executing report restore...");

        // Reportable definitions
        logDebug(taskMonitor, "Restoring reportable entities...");
        if (applicationConfig.getEntitiesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEntitiesConfig().getEntityList())) {
            for (AppEntityConfig appEntityConfig : applicationConfig.getEntitiesConfig().getEntityList()) {
                if (appEntityConfig.getReportable()) {
                    String description = resolveApplicationMessage("$m{report.managedreport.description}",
                            resolveApplicationMessage(appEntityConfig.getDescription()));
                    String entity = ApplicationNameUtils.ensureLongNameReference(applicationName,
                            appEntityConfig.getName());
                    logDebug(taskMonitor, "Restoring managed reportable [{0}]...", description);
                    ReportableDefinition reportableDefinition = new ReportableDefinition();
                    reportableDefinition.setApplicationId(applicationId);
                    reportableDefinition.setName(appEntityConfig.getName());
                    reportableDefinition.setEntity(entity);
                    reportableDefinition.setTitle(description);
                    reportableDefinition.setDescription(description);
                    reportableDefinition.setDeprecated(false);
                    reportableDefinition.setConfigType(ConfigType.CUSTOM);
                    populateChildList(appEntityConfig, reportableDefinition);
                    environment().create(reportableDefinition);

                    registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE,
                            PrivilegeNameUtils.getReportablePrivilegeName(ApplicationNameUtils
                                    .ensureLongNameReference(applicationName, appEntityConfig.getName())),
                            description);
                }
            }
        }

        // Configured reports
        if (applicationConfig.getReportsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getReportsConfig().getReportList())) {
            for (AppReportConfig applicationReportConfig : applicationConfig.getReportsConfig().getReportList()) {
                ReportInstall reportInstall = getConfigurationLoader()
                        .loadReportInstallation(applicationReportConfig.getConfigFile());
                ReportConfig reportConfig = reportInstall.getReportConfig();
                String description = resolveApplicationMessage(reportConfig.getDescription());
                logDebug(taskMonitor, "Restoring configured report [{0}]...", description);
                String title = reportConfig.getTitle();
                if (title == null) {
                    title = description;
                }

                String reportable = ApplicationNameUtils.ensureLongNameReference(applicationName,
                        reportConfig.getReportable());

                ReportConfiguration reportConfiguration = new ReportConfiguration();
                reportConfiguration.setApplicationId(applicationId);
                reportConfiguration.setType(reportConfig.getType());
                reportConfiguration.setSummaryDatasource(reportConfig.getSummaryDatasource());
                reportConfiguration.setSizeType(reportConfig.getSizeType());
                reportConfiguration.setName(reportConfig.getName());
                reportConfiguration.setDescription(description);
                reportConfiguration.setReportable(reportable);
                reportConfiguration.setTitle(title);
                reportConfiguration.setTemplate(reportConfig.getTemplate());
                reportConfiguration.setWidth(reportConfig.getWidth());
                reportConfiguration.setHeight(reportConfig.getHeight());
                reportConfiguration.setMarginBottom(reportConfig.getMarginBottom());
                reportConfiguration.setMarginLeft(reportConfig.getMarginLeft());
                reportConfiguration.setMarginRight(reportConfig.getMarginRight());
                reportConfiguration.setMarginTop(reportConfig.getMarginTop());
                reportConfiguration.setProcessor(reportConfig.getProcessor());
                reportConfiguration.setLetterGenerator(reportConfig.getLetterGenerator());
                reportConfiguration.setShowGrandFooter(reportConfig.getShowGrandFooter());
                reportConfiguration.setInvertGroupColors(reportConfig.getInvertGroupColors());
                reportConfiguration.setLandscape(reportConfig.getLandscape());
                reportConfiguration.setShadeOddRows(reportConfig.getShadeOddRows());
                reportConfiguration.setUnderlineRows(reportConfig.getUnderlineRows());
                reportConfiguration.setAllowSecondaryTenants(reportConfig.getAllowSecondaryTenants());
                reportConfiguration.setFilter(InputWidgetUtils.newAppFilter(reportConfig.getFilter()));
                reportConfiguration.setDeprecated(false);
                reportConfiguration.setConfigType(ConfigType.CUSTOM);
                populateChildList(reportConfig, reportConfiguration);
                environment().create(reportConfiguration);

                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTCONFIG_CATEGORY_CODE,
                        PrivilegeNameUtils.getReportConfigPrivilegeName(
                                ApplicationNameUtils.ensureLongNameReference(applicationName, reportConfig.getName())),
                        description);
            }
        }
    }

    @Override
    public void replicateApplicationArtifacts(TaskMonitor taskMonitor, Long srcApplicationId, Long destApplicationId,
            ApplicationReplicationContext ctx) throws UnifyException {
        // Reportables
        logDebug(taskMonitor, "Replicating reportables...");
        List<Long> reportableIdList = environment().valueList(Long.class, "id",
                new ReportableDefinitionQuery().applicationId(srcApplicationId));
        for (Long reportableId : reportableIdList) {
            ReportableDefinition srcReportableDefinition = environment().find(ReportableDefinition.class, reportableId);
            String oldDescription = srcReportableDefinition.getDescription();
            srcReportableDefinition.setApplicationId(destApplicationId);
            srcReportableDefinition.setName(ctx.nameSwap(srcReportableDefinition.getName()));
            srcReportableDefinition.setDescription(ctx.messageSwap(srcReportableDefinition.getDescription()));
            srcReportableDefinition.setTitle(ctx.messageSwap(srcReportableDefinition.getTitle()));
            srcReportableDefinition.setEntity(ctx.entitySwap(srcReportableDefinition.getEntity()));

            for (ReportableField reportableField : srcReportableDefinition.getFieldList()) {
                reportableField.setName(ctx.fieldSwap(reportableField.getName()));
                reportableField.setDescription(ctx.messageSwap(reportableField.getDescription()));
            }

            environment().create(srcReportableDefinition);
            registerPrivilege(destApplicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE,
                    PrivilegeNameUtils.getReportablePrivilegeName(ApplicationNameUtils
                            .ensureLongNameReference(ctx.getDestApplicationName(), srcReportableDefinition.getName())),
                    srcReportableDefinition.getDescription());
            logDebug(taskMonitor, "Reportable [{0}] -> [{1}]...", oldDescription,
                    srcReportableDefinition.getDescription());
        }

        // Reports
        logDebug(taskMonitor, "Replicating report configurations...");
        List<Long> reportIdList = environment().valueList(Long.class, "id",
                new ReportConfigurationQuery().applicationId(srcApplicationId));
        for (Long reportId : reportIdList) {
            ReportConfiguration srcReportConfiguration = environment().find(ReportConfiguration.class, reportId);
            String oldDescription = srcReportConfiguration.getDescription();
            srcReportConfiguration.setApplicationId(destApplicationId);
            srcReportConfiguration.setName(ctx.nameSwap(srcReportConfiguration.getName()));
            srcReportConfiguration.setDescription(ctx.messageSwap(srcReportConfiguration.getDescription()));
            srcReportConfiguration.setTitle(ctx.messageSwap(srcReportConfiguration.getTitle()));
            srcReportConfiguration.setReportable(ctx.entitySwap(srcReportConfiguration.getReportable()));

            ApplicationReplicationUtils.applyReplicationRules(ctx, srcReportConfiguration.getFilter());

            for (ReportColumn reportColumn : srcReportConfiguration.getColumnList()) {
                reportColumn.setFieldName(ctx.fieldSwap(reportColumn.getFieldName()));
                reportColumn.setDescription(ctx.messageSwap(reportColumn.getDescription()));
            }

            for (ReportPlacement reportPlacement : srcReportConfiguration.getPlacementList()) {
                reportPlacement.setFieldName(ctx.fieldSwap(reportPlacement.getFieldName()));
            }

            environment().create(srcReportConfiguration);
            registerPrivilege(destApplicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTCONFIG_CATEGORY_CODE,
                    PrivilegeNameUtils.getReportConfigPrivilegeName(ApplicationNameUtils
                            .ensureLongNameReference(ctx.getDestApplicationName(), srcReportConfiguration.getName())),
                    srcReportConfiguration.getDescription());
            logDebug(taskMonitor, "Report configuration [{0}] -> [{1}]...", oldDescription,
                    srcReportConfiguration.getDescription());
        }

    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("report configurations", new ReportConfigurationQuery()),
                new DeletionParams("reportables", new ReportableDefinitionQuery()));
    }

    @SuppressWarnings("unchecked")
    private void populateChildList(AppEntityConfig appEntityConfig, ReportableDefinition reportableDefinition)
            throws UnifyException {
        List<ReportableField> reportableFieldList = new ArrayList<ReportableField>();
        Class<? extends Entity> entityClass = EntityTypeUtils.isReservedType(appEntityConfig.getType()) ? null
                : (Class<? extends Entity>) ReflectUtils.classForName(appEntityConfig.getType());
        if (!DataUtils.isBlank(appEntityConfig.getEntityFieldList())) {
            for (EntityFieldConfig rfd : appEntityConfig.getEntityFieldList()) {
                if (rfd.getReportable() && !EntityFieldDataType.SCRATCH.equals(rfd.getType())) {
                    ReportableField reportableField = new ReportableField();
                    String description = null;
                    Format fa = entityClass != null
                            ? ReflectUtils.getField(entityClass, rfd.getName()).getAnnotation(Format.class)
                            : null;
                    if (fa != null) {
                        description = AnnotationUtils.getAnnotationString(fa.description());
                        if (description != null) {
                            description = resolveApplicationMessage(description);
                        }

                        String formatter = AnnotationUtils.getAnnotationString(fa.formatter());
                        reportableField.setFormatter(formatter);
                        reportableField.setHorizontalAlign(fa.halign().name());
                        reportableField.setWidth(fa.widthRatio());
                    } else {
                        if (Number.class.isAssignableFrom(rfd.getType().dataType().javaClass())) {
                            reportableField.setHorizontalAlign(HAlignType.RIGHT.name());
                        }
                        reportableField.setWidth(-1);
                    }

                    if (description == null) {
                        description = NameUtils.describeName(rfd.getName());
                    }

                    reportableField.setDescription(description);
                    reportableField.setName(rfd.getName());
                    reportableField.setParameterOnly(false);
                    reportableField.setType(ConverterUtils.getWrapperClassName(rfd.getType().dataType().javaClass()));
                    reportableFieldList.add(reportableField);
                }
            }
        }

        List<ReportableField> baseReportableFieldList = ReportEntityUtils.getEntityBaseTypeReportableFieldList(
                messageResolver,
                entityClass != null
                        ? ApplicationEntityUtils.getEntityBaseType((Class<? extends BaseEntity>) entityClass)
                        : appEntityConfig.getBaseType(),
                FormatterOptions.DEFAULT);
        reportableFieldList.addAll(baseReportableFieldList);
        reportableDefinition.setFieldList(reportableFieldList);
    }

    private void populateChildList(ReportConfig reportConfig, ReportConfiguration reportConfiguration)
            throws UnifyException {
        // Columns
        if (reportConfig.getColumns() != null && DataUtils.isNotBlank(reportConfig.getColumns().getColumnList())) {
            List<ReportColumn> columnList = new ArrayList<ReportColumn>();
            for (ReportColumnConfig columnConfig : reportConfig.getColumns().getColumnList()) {
                ReportColumn reportColumn = new ReportColumn();
                reportColumn.setColumnOrder(columnConfig.getColumnOrder());
                reportColumn.setFieldName(columnConfig.getFieldName());
                reportColumn.setDescription(columnConfig.getDescription());
                reportColumn.setRenderWidget(columnConfig.getRenderWidget());
                reportColumn.setType(columnConfig.getType());
                reportColumn.setFormatter(columnConfig.getFormatter());
                reportColumn.setHorizAlignType(columnConfig.getHorizAlignType());
                reportColumn.setVertAlignType(columnConfig.getVertAlignType());
                reportColumn.setWidth(columnConfig.getWidth());
                reportColumn.setBold(columnConfig.isBold());
                reportColumn.setGroup(columnConfig.isGroup());
                reportColumn.setGroupOnNewPage(columnConfig.isGroupOnNewPage());
                reportColumn.setSum(columnConfig.isSum());
                columnList.add(reportColumn);
            }

            reportConfiguration.setColumnList(columnList);
        }

        // Placements
        if (reportConfig.getPlacements() != null
                && DataUtils.isNotBlank(reportConfig.getPlacements().getPlacementList())) {
            List<ReportPlacement> placementList = new ArrayList<ReportPlacement>();
            for (ReportPlacementConfig placementConfig : reportConfig.getPlacements().getPlacementList()) {
                ReportPlacement reportPlacement = new ReportPlacement();
                reportPlacement.setFieldName(placementConfig.getFieldName());
                reportPlacement.setText(placementConfig.getText());
                reportPlacement.setType(placementConfig.getType());
                reportPlacement.setFormatter(placementConfig.getFormatter());
                reportPlacement.setHorizAlignType(placementConfig.getHorizAlignType());
                reportPlacement.setVertAlignType(placementConfig.getVertAlignType());
                reportPlacement.setXOffsetType(placementConfig.getXOffsetType());
                reportPlacement.setYOffsetType(placementConfig.getYOffsetType());
                reportPlacement.setX(placementConfig.getX());
                reportPlacement.setY(placementConfig.getY());
                reportPlacement.setWidth(placementConfig.getWidth());
                reportPlacement.setHeight(placementConfig.getHeight());
                reportPlacement.setBold(placementConfig.isBold());
                placementList.add(reportPlacement);
            }

            reportConfiguration.setPlacementList(placementList);
        }

        // Parameters
        if (reportConfig.getParameters() != null
                && DataUtils.isNotBlank(reportConfig.getParameters().getParameterList())) {
            List<ReportParameter> parameterList = new ArrayList<ReportParameter>();
            for (ParameterConfig parameterConfig : reportConfig.getParameters().getParameterList()) {
                ReportParameter reportParameter = new ReportParameter();
                reportParameter.setName(parameterConfig.getName());
                String description = parameterConfig.getDescription();
                if (StringUtils.isBlank(description)) {
                    description = resolveApplicationMessage(parameterConfig.getLabel());
                }

                reportParameter.setDescription(description);
                reportParameter.setEditor(parameterConfig.getEditor());
                reportParameter.setLabel(parameterConfig.getLabel());
                reportParameter.setMandatory(parameterConfig.isMandatory());
                reportParameter.setType(parameterConfig.getType());
                reportParameter.setDefaultVal(parameterConfig.getDefaultVal());
                parameterList.add(reportParameter);
            }

            reportConfiguration.setParameterList(parameterList);
        }

    }

}
