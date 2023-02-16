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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ParameterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ParametersConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportColumnConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportColumnsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportPlacementConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportPlacementsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.flowcentraltech.flowcentral.report.entities.ReportColumn;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportParameter;
import com.flowcentraltech.flowcentral.report.entities.ReportPlacement;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Reports XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("reports-xml-generator")
public class ReportsXmlGenerator extends AbstractStaticArtifactGenerator {

    private static final String REPORT_FOLDER = "apps/report/";

    @Configurable
    private ReportModuleService reportModuleService;

    public ReportsXmlGenerator() {
        super("src/main/resources/apps/report/");
    }

    public void setReportModuleService(ReportModuleService reportModuleService) {
        this.reportModuleService = reportModuleService;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        List<Long> reportConfigIdList = reportModuleService.findReportConfigurationIdList(applicationName);
        if (!DataUtils.isBlank(reportConfigIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();

            AppReportsConfig reportsConfig = new AppReportsConfig();
            List<AppReportConfig> reportConfigList = new ArrayList<AppReportConfig>();
            for (Long reportConfigId : reportConfigIdList) {
                AppReportConfig appReportConfig = new AppReportConfig();
                ReportConfiguration reportConfiguration = reportModuleService.findReportConfiguration(reportConfigId);
                final String filename = StringUtils.dashen(NameUtils.describeName(reportConfiguration.getName()))
                        + ".xml";
                openEntry(filename, zos);

                ReportConfig reportConfig = new ReportConfig();
                String descKey = getDescriptionKey(lowerCaseApplicationName, "report", reportConfiguration.getName());
                String titleKey = descKey + ".title";
                ctx.addMessage(StaticMessageCategoryType.REPORT, descKey, reportConfiguration.getDescription());
                ctx.addMessage(StaticMessageCategoryType.REPORT, titleKey, reportConfiguration.getTitle());

                reportConfig.setType(reportConfiguration.getType());
                reportConfig.setSizeType(reportConfiguration.getSizeType());
                reportConfig.setName(reportConfiguration.getName());
                reportConfig.setDescription("$m{" + descKey + "}");
                reportConfig.setReportable(reportConfiguration.getReportable());
                reportConfig.setTitle("$m{" + titleKey + "}");
                reportConfig.setTemplate(reportConfiguration.getTemplate());
                reportConfig.setWidth(reportConfiguration.getWidth());
                reportConfig.setHeight(reportConfiguration.getHeight());
                reportConfig.setProcessor(reportConfiguration.getProcessor());
                reportConfig.setShowGrandFooter(reportConfiguration.isShowGrandFooter());
                reportConfig.setInvertGroupColors(reportConfiguration.isInvertGroupColors());
                reportConfig.setLandscape(reportConfiguration.isLandscape());
                reportConfig.setShadeOddRows(reportConfiguration.isShadeOddRows());
                reportConfig.setUnderlineRows(reportConfiguration.isUnderlineRows());
                reportConfig.setAllowSecondaryTenants(reportConfiguration.isAllowSecondaryTenants());
                FilterConfig filterConfig = InputWidgetUtils.getFilterConfig(au(), reportConfiguration.getFilter());
                reportConfig.setFilter(filterConfig);

                // Columns
                if (!DataUtils.isBlank(reportConfiguration.getColumnList())) {
                    ReportColumnsConfig columns = new ReportColumnsConfig();
                    List<ReportColumnConfig> columnList = new ArrayList<ReportColumnConfig>();
                    for (ReportColumn reportColumn : reportConfiguration.getColumnList()) {
                        ReportColumnConfig reportColumnConfig = new ReportColumnConfig();
                        reportColumnConfig.setColumnOrder(reportColumn.getColumnOrder());
                        reportColumnConfig.setFieldName(reportColumn.getFieldName());
                        reportColumnConfig.setDescription(reportColumn.getDescription());
                        reportColumnConfig.setType(reportColumn.getType());
                        reportColumnConfig.setFormatter(reportColumn.getFormatter());
                        reportColumnConfig.setHorizAlignType(reportColumn.getHorizAlignType());
                        reportColumnConfig.setVertAlignType(reportColumn.getVertAlignType());
                        reportColumnConfig.setWidth(reportColumn.getWidth());
                        reportColumnConfig.setGroup(reportColumn.isGroup());
                        reportColumnConfig.setGroupOnNewPage(reportColumn.isGroupOnNewPage());
                        reportColumnConfig.setSum(reportColumn.isSum());
                        columnList.add(reportColumnConfig);
                    }

                    columns.setColumnList(columnList);
                    reportConfig.setColumns(columns);
                }

                // Placement
                if (!DataUtils.isBlank(reportConfiguration.getPlacementList())) {
                    ReportPlacementsConfig placements = new ReportPlacementsConfig();
                    List<ReportPlacementConfig> placementList = new ArrayList<ReportPlacementConfig>();
                    for (ReportPlacement reportPlacement : reportConfiguration.getPlacementList()) {
                        ReportPlacementConfig reportPlacementConfig = new ReportPlacementConfig();
                        reportPlacementConfig.setFieldName(reportPlacement.getFieldName());
                        reportPlacementConfig.setText(reportPlacement.getText());
                        reportPlacementConfig.setType(reportPlacement.getType());
                        reportPlacementConfig.setFormatter(reportPlacement.getFormatter());
                        reportPlacementConfig.setHorizAlignType(reportPlacement.getHorizAlignType());
                        reportPlacementConfig.setVertAlignType(reportPlacement.getVertAlignType());
                        reportPlacementConfig.setX(reportPlacement.getX());
                        reportPlacementConfig.setY(reportPlacement.getY());
                        reportPlacementConfig.setWidth(reportPlacement.getWidth());
                        reportPlacementConfig.setHeight(reportPlacement.getHeight());
                        reportPlacementConfig.setBold(reportPlacement.isBold());
                        placementList.add(reportPlacementConfig);
                    }

                    placements.setPlacementList(placementList);
                    reportConfig.setPlacements(placements);
                }

                // Parameters
                if (!DataUtils.isBlank(reportConfiguration.getParameterList())) {
                    ParametersConfig parameters = new ParametersConfig();
                    List<ParameterConfig> parameterList = new ArrayList<ParameterConfig>();
                    for (ReportParameter reportParameter : reportConfiguration.getParameterList()) {
                        ParameterConfig parameterConfig = new ParameterConfig();
                        parameterConfig.setName(reportParameter.getName());
                        if (!StringUtils.isBlank(reportParameter.getDescription())) {
                            descKey = getDescriptionKey(lowerCaseApplicationName, "reportparameter",
                                    reportParameter.getName());
                            ctx.addMessage(StaticMessageCategoryType.REPORT, descKey, reportParameter.getDescription());
                            parameterConfig.setDescription("$m{" + descKey + "}");
                        }

                        parameterConfig.setEditor(reportParameter.getEditor());
                        parameterConfig.setLabel(reportParameter.getLabel());
                        parameterConfig.setMandatory(reportParameter.getMandatory());
                        parameterConfig.setType(reportParameter.getType());
                        parameterConfig.setDefaultVal(reportParameter.getDefaultVal());
                        parameterList.add(parameterConfig);
                    }

                    parameters.setParameterList(parameterList);
                    reportConfig.setParameters(parameters);
                }

                ConfigurationUtils.writeConfigNoEscape(reportConfig, zos);
                closeEntry(zos);

                appReportConfig.setConfigFile(REPORT_FOLDER + filename);
                reportConfigList.add(appReportConfig);
            }

            reportsConfig.setReportList(reportConfigList);
            ctx.setReportsConfig(reportsConfig);
        }

    }

}
