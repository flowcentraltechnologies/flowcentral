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
package com.flowcentraltech.flowcentral.report.business;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterRestrictionDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.data.DefaultReportColumn;
import com.flowcentraltech.flowcentral.common.data.ReportColumnOptions;
import com.flowcentraltech.flowcentral.common.data.ReportFilterOptions;
import com.flowcentraltech.flowcentral.common.data.ReportJoinOptions;
import com.flowcentraltech.flowcentral.common.data.ReportListing;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.report.constants.ReportModuleNameConstants;
import com.flowcentraltech.flowcentral.report.constants.ReportModuleSysParamConstants;
import com.flowcentraltech.flowcentral.report.constants.ReportParameterConstants;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportConfigurationQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportParameter;
import com.flowcentraltech.flowcentral.report.entities.ReportParameterQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.entities.ReportableFieldQuery;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.criterion.DoubleParamRestriction;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.MultipleParamRestriction;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.RestrictionType;
import com.tcdng.unify.core.criterion.SingleParamRestriction;
import com.tcdng.unify.core.criterion.ZeroParamRestriction;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.database.sql.SqlDataSourceDialect;
import com.tcdng.unify.core.database.sql.SqlEntityInfo;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.Report.Builder;
import com.tcdng.unify.core.report.ReportColumn;
import com.tcdng.unify.core.report.ReportFormat;
import com.tcdng.unify.core.report.ReportServer;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.GetterSetterInfo;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ThemeManager;

/**
 * Implementation of report module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(ReportModuleNameConstants.REPORT_MODULE_SERVICE)
public class ReportModuleServiceImpl extends AbstractFlowCentralService implements ReportModuleService {

    @Configurable
    private ThemeManager themeManager;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private ReportServer reportServer;

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private SpecialParamProvider specialParamProvider;
    
    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
    }

    public void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public void setReportServer(ReportServer reportServer) {
        this.reportServer = reportServer;
    }

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public void setSpecialParamProvider(SpecialParamProvider specialParamProvider) {
        this.specialParamProvider = specialParamProvider;
    }

    @Override
    public ReportConfiguration findReportConfiguration(Long reportConfigurationId) throws UnifyException {
        return environment().find(ReportConfiguration.class, reportConfigurationId);
    }

    @Override
    public List<Long> findReportConfigurationIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new ReportConfigurationQuery().applicationName(applicationName));
    }

    @Override
    public Long createReportableDefinition(ReportableDefinition reportableDefinition) throws UnifyException {
        return (Long) environment().create(reportableDefinition);
    }

    @Override
    public List<ReportableDefinition> findReportDefinitions(ReportableDefinitionQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public Long getReportableDefinitionId(String reportableName) throws UnifyException {
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(reportableName);
        return environment().value(Long.class, "id", new ReportableDefinitionQuery().applicationName(np.getApplicationName())
                .name(np.getEntityName()).mustMatch(false));
    }

    @Override
    public Long getReportConfigReportableDefinitionId(Long reportConfigurationId) throws UnifyException {
        String reportableName = environment().value(String.class, "reportable",
                new ReportConfigurationQuery().id(reportConfigurationId));
        return getReportableDefinitionId(reportableName);
    }

    @Override
    public Long createReportableField(ReportableField reportableField) throws UnifyException {
        return (Long) environment().create(reportableField);
    }

    @Override
    public ReportableField findReportableField(ReportableFieldQuery query) throws UnifyException {
        return environment().find(query);
    }

    @Override
    public List<ReportableField> findReportableFields(ReportableFieldQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public int updateReportableField(ReportableField reportableField) throws UnifyException {
        return environment().updateByIdVersion(reportableField);
    }

    @Override
    public int deleteReportableField(ReportableFieldQuery query) throws UnifyException {
        return environment().deleteAll(query);
    }

    @Override
    public List<ReportParameter> findReportParameters(ReportParameterQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public ReportColumn[] findReportableColumns(String reportableName) throws UnifyException {
        ReportableDefinition reportableDefinition = environment().find(new ReportableDefinitionQuery().name(reportableName));

        List<ReportableField> reportFieldList = environment()
                .findAll(new ReportableFieldQuery().reportableId(reportableDefinition.getId()).orderById());

        ReportColumn[] reportColumns = new ReportColumn[reportFieldList.size()];
        for (int i = 0; i < reportColumns.length; i++) {
            ReportableField reportableField = reportFieldList.get(i);
            reportColumns[i] = ReportColumn.newBuilder().title(reportableField.getDescription())
                    .name(reportableField.getName()).className(reportableField.getType())
                    .widthRatio(reportableField.getWidth()).formatter(reportableField.getFormatter())
                    .horizontalAlignment(HAlignType.fromName(reportableField.getHorizontalAlign())).build();
        }
        return reportColumns;
    }

    @Override
    public ReportOptions getDynamicReportOptions(String entityName, List<DefaultReportColumn> defaultReportColumnList)
            throws UnifyException {
        EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(entityName);
        ReportableDefinition reportableDefinition = environment()
                .listLean(new ReportableDefinitionQuery().entity(entityName));
        ReportOptions reportOptions = new ReportOptions();
        reportOptions.setReportName(ApplicationNameUtils.getApplicationEntityLongName(
                reportableDefinition.getApplicationName(), reportableDefinition.getName()));
        reportOptions.setTitle(reportableDefinition.getTitle());
        reportOptions.setRecordName(entityClassDef.getEntityClass().getName());

        Map<String, ReportableField> fieldMap = environment().listAllMap(String.class, "name",
                new ReportableFieldQuery().reportableId(reportableDefinition.getId()).parameterOnly(false));
        boolean isSelectAll = defaultReportColumnList == null;
        if (!isSelectAll) {
            for (DefaultReportColumn defaultColumn : defaultReportColumnList) {
                ReportableField reportableField = fieldMap.remove(defaultColumn.getFieldName());
                if (reportableField != null) {
                    ReportColumnOptions remoteColumnOptions = new ReportColumnOptions(reportableField.getName(),
                            defaultColumn.getCaption(), reportableField.getType(), reportableField.getFormatter(),
                            HAlignType.fromName(reportableField.getHorizontalAlign()), reportableField.getWidth(),
                            true);
                    reportOptions.addColumnOptions(remoteColumnOptions);
                }
            }
        }

        // Add what's left
        for (ReportableField reportableField : fieldMap.values()) {
            ReportColumnOptions remoteColumnOptions = new ReportColumnOptions(reportableField.getName(),
                    reportableField.getDescription(), reportableField.getType(), reportableField.getFormatter(),
                    HAlignType.fromName(reportableField.getHorizontalAlign()), reportableField.getWidth(), isSelectAll);
            reportOptions.addColumnOptions(remoteColumnOptions);
        }

        return reportOptions;
    }

    @Override
    public void generateDynamicReport(ReportOptions reportOptions, OutputStream outputStream) throws UnifyException {
        Report.Builder rb = Report.newBuilder();
        rb.code(reportOptions.getReportName());
        rb.title(reportOptions.getTitle());
        rb.dataSource(reportOptions.getDataSource());
        rb.processor(reportOptions.getProcessor());
        rb.pageWidth(reportOptions.getPageWidth());
        rb.pageHeight(reportOptions.getPageHeight());
        rb.dynamicDataSource(reportOptions.isDynamicDataSource());
        rb.printColumnNames(reportOptions.isPrintColumnNames());
        rb.printGroupColumnNames(reportOptions.isPrintGroupColumnNames());
        rb.showGrandFooter(reportOptions.isShowGrandFooter());
        rb.invertGroupColors(reportOptions.isInvertGroupColors());
        rb.underlineRows(reportOptions.isUnderlineRows());
        rb.shadeOddRows(reportOptions.isShadeOddRows());
        rb.landscape(reportOptions.isLandscape());
        rb.format(ReportFormat.fromName(reportOptions.getReportFormat()));
        if (StringUtils.isNotBlank(reportOptions.getReportLayout())) {
            rb.layout(reportOptions.getReportLayout());
        }

        if (DataUtils.isNotBlank(reportOptions.getSystemInputList())) {
            for (Input<?> input : reportOptions.getSystemInputList()) {
                rb.addParameter(input.getName(), input.getDescription(), input.getTypeValue());
            }
        }

        if (DataUtils.isNotBlank(reportOptions.getUserInputList())) {
            for (Input<?> input : reportOptions.getUserInputList()) {
                rb.addParameter(input.getName(), input.getDescription(), input.getTypeValue());
            }
        }

        List<ReportColumnOptions> reportColumnOptionsList = new ArrayList<ReportColumnOptions>(
                reportOptions.getColumnOptionsList());
        DataUtils.sortDescending(reportColumnOptionsList, ReportColumnOptions.class, "group");

        List<ReportColumnOptions> sortReportColumnOptionsList = new ArrayList<ReportColumnOptions>();
        // TODO Get from report data source. For now just get from application data
        // source
        SqlDataSourceDialect sqlDialect = (SqlDataSourceDialect) db().getDataSource().getDialect();
        Class<?> dataClass = ReflectUtils.classForName(reportOptions.getRecordName());
        SqlEntityInfo sqlEntityInfo = null;
        if (reportOptions.isReportEntityList()) {
            sqlEntityInfo = sqlDialect.findSqlEntityInfo(dataClass);
        }

        String sqlBlobTypeName = sqlDialect.getSqlBlobType();
        for (ReportColumnOptions reportColumnOptions : reportColumnOptionsList) {
            if (reportColumnOptions.isIncluded()) {
                if (reportColumnOptions.isGroup() || reportColumnOptions.getOrderType() != null) {
                    sortReportColumnOptionsList.add(reportColumnOptions);
                }

                String tableName = reportColumnOptions.getTableName();
                String columnName = reportColumnOptions.getColumnName();
                if (reportOptions.isReportEntityList()) {
                    tableName = sqlEntityInfo.getPreferredViewName();
                    columnName = sqlEntityInfo.getListFieldInfo(columnName).getPreferredColumnName();
                }

                rb.addColumn(reportColumnOptions.getDescription(), tableName, columnName, reportColumnOptions.getType(),
                        sqlBlobTypeName, reportColumnOptions.getFormatter(), reportColumnOptions.getOrderType(),
                        reportColumnOptions.getHorizontalAlignment(), reportColumnOptions.getWidth(),
                        reportColumnOptions.isGroup(), reportColumnOptions.isGroupOnNewPage(),
                        reportColumnOptions.isSum());
            }
        }

        if (reportOptions.isBeanCollection()) {
            List<?> content = reportOptions.getContent();
            for (int i = sortReportColumnOptionsList.size() - 1; i >= 0; i--) {
                ReportColumnOptions reportColumnOptions = sortReportColumnOptionsList.get(i);
                if (OrderType.ASCENDING.equals(reportColumnOptions.getOrderType())) {
                    DataUtils.sortAscending(content, dataClass, reportColumnOptions.getColumnName());
                } else {
                    DataUtils.sortDescending(content, dataClass, reportColumnOptions.getColumnName());
                }
            }
            rb.beanCollection(content);
        } else if (reportOptions.isReportEntityList()) {
            rb.table(sqlEntityInfo.getPreferredViewName());
            if (reportOptions.isWithRestriction()) {
                Restriction restriction = reportOptions.getRestriction().isSimple()
                        ? new And().add(reportOptions.getRestriction())
                        : reportOptions.getRestriction();
                buildReportFilter(rb, sqlEntityInfo, restriction);
            }
        } else {
            rb.query(reportOptions.getQuery());
            rb.table(reportOptions.getTableName());

            if (reportOptions.isWithJoinOptions()) {
                for (ReportJoinOptions rjo : reportOptions.getJoinOptionsList()) {
                    rb.addJoin(rjo.getTableA(), rjo.getColumnA(), rjo.getTableB(), rjo.getColumnB());
                }
            }

            if (reportOptions.isWithFilterOptions()) {
                buildReportFilter(rb, reportOptions.getFilterOptions());
            }
        }

        Report report = rb.build();
        setCommonReportParameters(report);
        reportServer.generateReport(report, outputStream);
    }

    @Override
    public boolean isReportable(String entityName) throws UnifyException {
        return environment().find(new ReportableDefinitionQuery().entity(entityName)) != null;
    }

    @Override
    public List<ReportListing> getRoleReportListing(String roleCode) throws UnifyException {
        List<String> privList = applicationPrivilegeManager
                .findRolePrivileges(ApplicationPrivilegeConstants.APPLICATION_REPORTCONFIG_CATEGORY_CODE, roleCode);
        if (!DataUtils.isBlank(privList)) {
            List<ReportListing> resultList = new ArrayList<ReportListing>();
            Or or = new Or();
            for (String privCode : privList) {
                ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(
                        PrivilegeNameUtils.getPrivilegeNameParts(privCode).getEntityName());
                or.add(new And().add(new Equals("applicationName", np.getApplicationName()))
                        .add(new Equals("name", np.getEntityName())));
            }

            List<ReportConfiguration> rcList = environment().listAll(new ReportConfigurationQuery().addRestriction(or)
                    .addSelect("applicationName", "applicationDesc", "name", "description"));
            for (ReportConfiguration rc : rcList) {
                resultList.add(new ReportListing(rc.getApplicationName(), rc.getApplicationDesc(),
                        ApplicationNameUtils.getApplicationEntityLongName(rc.getApplicationName(), rc.getName()),
                        rc.getDescription()));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public ReportOptions getReportOptionsForConfiguration(String reportConfigName) throws UnifyException {
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(reportConfigName);
        ReportConfiguration reportConfiguration = environment()
                .list(new ReportConfigurationQuery().applicationName(np.getApplicationName()).name(np.getEntityName()));

        ApplicationEntityNameParts rnp = ApplicationNameUtils
                .getApplicationEntityNameParts(reportConfiguration.getReportable());
        String entity = environment().value(String.class, "entity",
                new ReportableDefinitionQuery().applicationName(rnp.getApplicationName()).name(rnp.getEntityName()));

        // Report column options
        EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(entity);
        ReportOptions reportOptions = new ReportOptions();
        reportOptions.setReportLayout(reportConfiguration.getLayout());
        reportOptions.setReportName(reportConfigName);
        reportOptions.setRecordName(entityClassDef.getEntityClass().getName());
        reportOptions.setReportDescription(reportConfiguration.getDescription().toUpperCase());
        reportOptions.setTitle(resolveSessionMessage(reportConfiguration.getTitle()));
        reportOptions.setProcessor(reportConfiguration.getProcessor());
        reportOptions.setRecordName(entityClassDef.getEntityClass().getName());
        reportOptions.setShowGrandFooter(reportConfiguration.isShowGrandFooter());
        reportOptions.setInvertGroupColors(reportConfiguration.isInvertGroupColors());
        reportOptions.setLandscape(reportConfiguration.isLandscape());
        reportOptions.setShadeOddRows(reportConfiguration.isShadeOddRows());
        reportOptions.setUnderlineRows(reportConfiguration.isUnderlineRows());

        // Report parameters
        List<Input<?>> userInputList = new ArrayList<Input<?>>();
        List<Input<?>> systemInputList = new ArrayList<Input<?>>();
        for (ReportParameter reportParam : reportConfiguration.getParameterList()) {
            String label = resolveSessionMessage(reportParam.getLabel());
            Input<?> holder = DataUtils.newInput(reportParam.getType().javaClass(), reportParam.getName(), label,
                    reportParam.getEditor(), reportParam.getMandatory());
            String defaultVal = reportParam.getDefaultVal();
            if (defaultVal != null) {
                holder.setStringValue(defaultVal);
            }

            if (StringUtils.isNotBlank(reportParam.getEditor())) {
                userInputList.add(holder);
            } else {
                systemInputList.add(holder);
            }
        }
        reportOptions.setUserInputList(userInputList);
        reportOptions.setSystemInputList(systemInputList);

        return reportOptions;
    }

    @Override
    public void populateExtraConfigurationReportOptions(ReportOptions reportOptions) throws UnifyException {
        ApplicationEntityNameParts np = ApplicationNameUtils
                .getApplicationEntityNameParts(reportOptions.getReportName());
        ReportConfiguration reportConfiguration = environment()
                .list(new ReportConfigurationQuery().applicationName(np.getApplicationName()).name(np.getEntityName()));

        ApplicationEntityNameParts rnp = ApplicationNameUtils
                .getApplicationEntityNameParts(reportConfiguration.getReportable());
        String entity = environment().value(String.class, "entity",
                new ReportableDefinitionQuery().applicationName(rnp.getApplicationName()).name(rnp.getEntityName()));

        // Report column options
        EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(entity);
        SqlEntityInfo sqlEntityInfo = ((SqlDataSourceDialect) db().getDataSource().getDialect())
                .findSqlEntityInfo(entityClassDef.getEntityClass());

        Long reportableDefinitionId = environment().value(Long.class, "id",
                new ReportableDefinitionQuery().applicationName(rnp.getApplicationName()).name(rnp.getEntityName()));
        Map<String, ReportableField> fieldMap = environment().listAllMap(String.class, "name",
                new ReportableFieldQuery().reportableId(reportableDefinitionId));
        reportOptions.setTableName(sqlEntityInfo.getPreferredViewName());

        if (!reportOptions.isWithColumnOptions()) { // Populate column options only on first run
            for (com.flowcentraltech.flowcentral.report.entities.ReportColumn reportColumn : reportConfiguration
                    .getColumnList()) {
                ReportColumnOptions reportColumnOptions = new ReportColumnOptions();
                reportColumnOptions.setDescription(reportColumn.getDescription());
                reportColumnOptions.setGroup(reportColumn.isGroup());
                reportColumnOptions.setGroupOnNewPage(reportColumn.isGroupOnNewPage());
                reportColumnOptions.setSum(reportColumn.isSum());
                reportColumnOptions.setOrderType(reportColumn.getColumnOrder());
                reportColumnOptions.setIncluded(true);

                String type = reportColumn.getType();
                String formatter = reportColumn.getFormatter();
                HAlignType hAlignType = reportColumn.getHorizAlignType();
                int width = reportColumn.getWidth();
                if (sqlEntityInfo != null) {
                    reportColumnOptions.setTableName(sqlEntityInfo.getPreferredViewName());

                    if (StringUtils.isNotBlank(reportColumn.getFieldName())) {
                        reportColumnOptions.setColumnName(
                                sqlEntityInfo.getListFieldInfo(reportColumn.getFieldName()).getPreferredColumnName());

                        ReportableField reportableField = fieldMap.get(reportColumn.getFieldName());
                        if (type == null) {
                            type = reportableField.getType();
                        }

                        if (formatter == null) {
                            formatter = reportableField.getFormatter();
                        }

                        if (width <= 0 && reportableField.getWidth() != null) {
                            width = reportableField.getWidth();
                        }

                        if (hAlignType == null) {
                            hAlignType = HAlignType.fromName(reportableField.getHorizontalAlign());
                        }
                    }
                } else {
                    reportColumnOptions.setColumnName(reportColumn.getFieldName());
                }

                if (type == null) {
                    GetterSetterInfo getterSetterInfo = ReflectUtils.getGetterInfo(entityClassDef.getEntityClass(),
                            reportColumn.getFieldName());
                    type = ConverterUtils.getWrapperClassName(getterSetterInfo.getType());
                }

                reportColumnOptions.setType(type);
                reportColumnOptions.setFormatter(formatter);
                reportColumnOptions.setHorizontalAlignment(hAlignType);
                reportColumnOptions.setWidth(width);
                reportOptions.addColumnOptions(reportColumnOptions);
            }
        }

        // Filter options
        if (reportConfiguration.getFilter() != null) {
            Map<String, Object> parameters = Inputs.getTypeValuesByName(reportOptions.getSystemInputList());
            Inputs.getTypeValuesByNameIntoMap(reportOptions.getUserInputList(), parameters);
            FilterDef filterDef = InputWidgetUtils.getFilterDef(reportConfiguration.getFilter());
            ReportFilterOptions reportFilterOptions = createReportFilterOptions(sqlEntityInfo, null, parameters,
                    filterDef.getFilterRestrictionDefList(), new IndexInfo());
            reportOptions.setFilterOptions(reportFilterOptions);
        }
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private class IndexInfo {

        private int index;

        private int subCompoundIndex;
    }

    private ReportFilterOptions createReportFilterOptions(SqlEntityInfo sqlEntityInfo,
            ReportFilterOptions parentFilterOptions, Map<String, Object> parameters,
            List<FilterRestrictionDef> filterRestrictionDefList, IndexInfo indexInfo) throws UnifyException {
        ReportFilterOptions reportFilterOptions = null;
        while (indexInfo.index < filterRestrictionDefList.size()) {
            FilterRestrictionDef restrictionDef = filterRestrictionDefList.get(indexInfo.index++);
            if (restrictionDef.getDepth() == indexInfo.subCompoundIndex) {
                if (restrictionDef.isCompound()) {
                    reportFilterOptions = new ReportFilterOptions(restrictionDef.getType().restrictionType());
                    indexInfo.subCompoundIndex++;
                    createReportFilterOptions(sqlEntityInfo, reportFilterOptions, parameters, filterRestrictionDefList,
                            indexInfo);
                    if (parentFilterOptions != null) {
                        parentFilterOptions.getSubFilterOptionList().add(reportFilterOptions);
                    }
                } else {
                    Object param1 = specialParamProvider.resolveSpecialParameter(restrictionDef.getParamA());
                    Object param2= specialParamProvider.resolveSpecialParameter(restrictionDef.getParamB());
                    if (restrictionDef.isParameterVal()) {
                        if (param1 != null) {
                            param1 = parameters.get((String) param1);
                        }

                        if (param2 != null) {
                            param2 = parameters.get((String) param2);
                        }
                    }

                    if (restrictionDef.getType().isRange()) {
                        if (param1 instanceof Date) {
                            param1 = CalendarUtils.getMidnightDate((Date) param1);
                        }

                        if (param2 instanceof Date) {
                            param2 = CalendarUtils.getLastSecondDate((Date) param2);
                        }
                    }

                    reportFilterOptions = new ReportFilterOptions(restrictionDef.getType().restrictionType(),
                            sqlEntityInfo.getPreferredViewName(),
                            sqlEntityInfo.getListFieldInfo(restrictionDef.getFieldName()).getPreferredColumnName(),
                            param1, param2);
                    if (parentFilterOptions != null) {
                        parentFilterOptions.getSubFilterOptionList().add(reportFilterOptions);
                    }
                }
            } else {
                indexInfo.index--;
                break;
            }
        }

        return reportFilterOptions;
    }

    private void setCommonReportParameters(Report report) throws UnifyException {
        report.setParameter(ReportParameterConstants.APPLICATION_TITLE, "Application Title",
                getUnifyComponentContext().getInstanceName());
        report.setParameter(ReportParameterConstants.CLIENT_TITLE, "Client Title",
                systemModuleService.getSysParameterValue(String.class, SystemModuleSysParamConstants.CLIENT_TITLE));
        report.setParameter(ReportParameterConstants.REPORT_TITLE, "Report Title", report.getTitle());

        String imagePath = themeManager.expandThemeTag(systemModuleService.getSysParameterValue(String.class,
                ReportModuleSysParamConstants.REPORT_CLIENT_LOGO));
        byte[] clientLogo = IOUtils.readFileResourceInputStream(imagePath, getUnifyComponentContext().getWorkingPath());
        report.setParameter(ReportParameterConstants.CLIENT_LOGO, "Client Logo", clientLogo);

        String templatePath = systemModuleService.getSysParameterValue(String.class,
                ReportModuleSysParamConstants.REPORT_TEMPLATE_PATH);
        String template = report.getTemplate();
        if (template == null) {
            String templateParameter = ReportModuleSysParamConstants.DYNAMIC_REPORT_PORTRAIT_TEMPLATE;
            if (report.isLandscape()) {
                templateParameter = ReportModuleSysParamConstants.DYNAMIC_REPORT_LANDSCAPE_TEMPLATE;
            }
            template = systemModuleService.getSysParameterValue(String.class, templateParameter);
        }
        report.setTemplate(IOUtils.buildFilename(templatePath, template));
    }

    private void buildReportFilter(Builder rb, SqlEntityInfo sqlEntityInfo, Restriction restriction)
            throws UnifyException {
        FilterConditionType type = restriction.getConditionType();
        if (type.isCompound()) {
            rb.beginCompoundFilter(type.restrictionType());
            for (Restriction subRestriction : ((CompoundRestriction) restriction).getRestrictionList()) {
                buildReportFilter(rb, sqlEntityInfo, subRestriction);
            }
            rb.endCompoundFilter();
        } else if (type.isSingleParam()) {
            SingleParamRestriction singleParamRestriction = (SingleParamRestriction) restriction;
            rb.addSimpleFilter(type.restrictionType(), sqlEntityInfo.getPreferredViewName(),
                    sqlEntityInfo.getListFieldInfo(singleParamRestriction.getFieldName()).getPreferredColumnName(),
                    singleParamRestriction.getParam(), null);
        } else if (type.isRange()) {
            DoubleParamRestriction doubleParamRestriction = (DoubleParamRestriction) restriction;
            rb.addSimpleFilter(type.restrictionType(), sqlEntityInfo.getPreferredViewName(),
                    sqlEntityInfo.getListFieldInfo(doubleParamRestriction.getFieldName()).getPreferredColumnName(),
                    doubleParamRestriction.getFirstParam(), doubleParamRestriction.getSecondParam());
        } else if (type.isZeroParams()) {
            ZeroParamRestriction zeroParamRestriction = (ZeroParamRestriction) restriction;
            rb.addSimpleFilter(type.restrictionType(), sqlEntityInfo.getPreferredViewName(),
                    sqlEntityInfo.getListFieldInfo(zeroParamRestriction.getFieldName()).getPreferredColumnName(), null,
                    null);
        } else if (type.isAmongst()) {
            MultipleParamRestriction multipleParamRestriction = (MultipleParamRestriction) restriction;
            rb.addSimpleFilter(type.restrictionType(), sqlEntityInfo.getPreferredViewName(),
                    sqlEntityInfo.getListFieldInfo(multipleParamRestriction.getFieldName()).getPreferredColumnName(),
                    multipleParamRestriction.getParams(), null);
        }
    }

    private void buildReportFilter(Report.Builder rb, ReportFilterOptions filterOptions) {
        if (filterOptions.getOp().isCompound()) {
            rb.beginCompoundFilter(filterOptions.getOp());
            for (ReportFilterOptions subFilterOptions : filterOptions.getSubFilterOptionList()) {
                if (subFilterOptions.isCompound()) {
                    buildReportFilter(rb, subFilterOptions);
                } else {
                    rb.addSimpleFilter(subFilterOptions.getOp(), subFilterOptions.getTableName(),
                            subFilterOptions.getColumnName(), subFilterOptions.getParam1(),
                            subFilterOptions.getParam2());
                }
            }

            rb.endCompoundFilter();
        } else {
            rb.beginCompoundFilter(RestrictionType.AND);
            rb.addSimpleFilter(filterOptions.getOp(), filterOptions.getTableName(), filterOptions.getColumnName(),
                    filterOptions.getParam1(), filterOptions.getParam2());
            rb.endCompoundFilter();
        }
    }
}
