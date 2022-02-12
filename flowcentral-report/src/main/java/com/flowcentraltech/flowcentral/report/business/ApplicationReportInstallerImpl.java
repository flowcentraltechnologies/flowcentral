/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.report.business;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.annotation.Format;
import com.flowcentraltech.flowcentral.common.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ReportInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppEntityConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ParameterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportColumnConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportConfig;
import com.flowcentraltech.flowcentral.report.constants.ReportModuleNameConstants;
import com.flowcentraltech.flowcentral.report.entities.ReportColumn;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportConfigurationQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportParameter;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application reports installer.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(ReportModuleNameConstants.APPLICATION_REPORT_INSTALLER)
public class ApplicationReportInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Configurable
    private MessageResolver messageResolver;

    public void setMessageResolver(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Long applicationId = applicationInstall.getApplicationId();
        final String applicationName = applicationInstall.getApplicationConfig().getName();

        logDebug(taskMonitor, "Executing report installer...");
        // Install reports for configurable entities
        logDebug(taskMonitor, "Installing reportable entities...");
        if (applicationConfig.getEntitiesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEntitiesConfig().getEntityList())) {
            for (AppEntityConfig appEntityConfig : applicationConfig.getEntitiesConfig().getEntityList()) {
                if (appEntityConfig.isReportable()) {
                    String description = resolveApplicationMessage("$m{report.managedreport.description}",
                            resolveApplicationMessage(appEntityConfig.getDescription()));
                    String entity = ApplicationNameUtils.ensureLongNameReference(applicationName,
                            appEntityConfig.getName());
                    logDebug(taskMonitor, "Installing managed reportable [{0}]...", description);
                    ReportableDefinition oldReportableDefinition = environment().findLean(new ReportableDefinitionQuery()
                            .applicationId(applicationId).name(appEntityConfig.getName()));
                    if (oldReportableDefinition == null) {
                        ReportableDefinition reportableDefinition = new ReportableDefinition();
                        reportableDefinition.setApplicationId(applicationId);
                        reportableDefinition.setName(appEntityConfig.getName());
                        reportableDefinition.setEntity(entity);
                        reportableDefinition.setTitle(description);
                        reportableDefinition.setDescription(description);
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
                    reportConfiguration.setName(reportConfig.getName());
                    reportConfiguration.setDescription(description);
                    reportConfiguration.setReportable(reportable);
                    reportConfiguration.setTitle(title);
                    reportConfiguration.setTemplate(reportConfig.getTemplate());
                    reportConfiguration.setLayout(reportConfig.getLayout());
                    reportConfiguration.setProcessor(reportConfig.getProcessor());
                    reportConfiguration.setShowGrandFooter(reportConfig.isShowGrandFooter());
                    reportConfiguration.setInvertGroupColors(reportConfig.isInvertGroupColors());
                    reportConfiguration.setLandscape(reportConfig.isLandscape());
                    reportConfiguration.setShadeOddRows(reportConfig.isShadeOddRows());
                    reportConfiguration.setUnderlineRows(reportConfig.isUnderlineRows());
                    reportConfiguration.setFilter(InputWidgetUtils.newAppFilter(reportConfig.getFilter()));
                    reportConfiguration.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(reportConfig, reportConfiguration);
                    environment().create(reportConfiguration);
                } else {
                    if (ConfigUtils.isSetInstall(oldReportConfiguration)) {
                        oldReportConfiguration.setDescription(description);
                        oldReportConfiguration.setReportable(reportable);
                        oldReportConfiguration.setTitle(title);
                        oldReportConfiguration.setTemplate(reportConfig.getTemplate());
                        oldReportConfiguration.setLayout(reportConfig.getLayout());
                        oldReportConfiguration.setProcessor(reportConfig.getProcessor());
                        oldReportConfiguration.setShowGrandFooter(reportConfig.isShowGrandFooter());
                        oldReportConfiguration.setInvertGroupColors(reportConfig.isInvertGroupColors());
                        oldReportConfiguration.setLandscape(reportConfig.isLandscape());
                        oldReportConfiguration.setShadeOddRows(reportConfig.isShadeOddRows());
                        oldReportConfiguration.setUnderlineRows(reportConfig.isUnderlineRows());
                        oldReportConfiguration.setFilter(InputWidgetUtils.newAppFilter(reportConfig.getFilter()));
                    }

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

    @SuppressWarnings("unchecked")
    private void populateChildList(AppEntityConfig appEntityConfig, ReportableDefinition reportableDefinition)
            throws UnifyException {
        List<ReportableField> reportableFieldList = new ArrayList<ReportableField>();
        Class<? extends Entity> entityClass = (Class<? extends Entity>) ReflectUtils
                .classForName(appEntityConfig.getType());
        if (!DataUtils.isBlank(appEntityConfig.getEntityFieldList())) {
            for (EntityFieldConfig rfd : appEntityConfig.getEntityFieldList()) {
                if (rfd.isReportable() && !EntityFieldDataType.SCRATCH.equals(rfd.getType())) {
                    ReportableField reportableField = new ReportableField();
                    String description = null;
                    Field field = ReflectUtils.getField(entityClass, rfd.getName());
                    Format fa = field.getAnnotation(Format.class);
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
                        if (Number.class.isAssignableFrom(field.getType())) {
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
                    reportableField.setType(ConverterUtils.getWrapperClassName(field.getType()));
                    reportableFieldList.add(reportableField);
                }
            }
        }

        List<ReportableField> baseReportableFieldList = ReportEntityUtils.getEntityBaseTypeReportableFieldList(
                messageResolver, ApplicationEntityUtils.getEntityBaseType((Class<? extends BaseEntity>) entityClass));
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
                reportColumn.setType(columnConfig.getType());
                reportColumn.setFormatter(columnConfig.getFormatter());
                reportColumn.setHorizAlignType(columnConfig.getHorizAlignType());
                reportColumn.setWidth(columnConfig.getWidth());
                reportColumn.setGroup(columnConfig.isGroup());
                reportColumn.setGroupOnNewPage(columnConfig.isGroupOnNewPage());
                reportColumn.setSum(columnConfig.isSum());
                columnList.add(reportColumn);
            }

            reportConfiguration.setColumnList(columnList);
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
