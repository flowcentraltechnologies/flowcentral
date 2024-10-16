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

package com.flowcentraltech.flowcentral.report.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Report entity utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ReportEntityUtils {

    private ReportEntityUtils() {

    }

    public static List<ReportableField> removeDuplicates(final List<ReportableField> fieldList) {
        final List<ReportableField> resultList = new ArrayList<ReportableField>();
        final Set<String> present = new HashSet<String>();
        for (ReportableField field: fieldList) {
            if (present.contains(field.getName())) {
                continue;
            }
            
            present.add(field.getName());
            resultList.add(field);
        }
        
        return resultList;
    }
    
    public static List<ReportableField> getEntityBaseTypeReportableFieldList(MessageResolver msgResolver,
            EntityBaseType type, FormatterOptions formatOptions) throws UnifyException {
        List<ReportableField> resultList = new ArrayList<ReportableField>();
        for (AppEntityField appEntityField : ApplicationEntityUtils.getEntityBaseTypeFieldList(msgResolver, type,
                ConfigType.STATIC)) {
            if (appEntityField.isReportable()) {
                ReportableField reportableField = new ReportableField();
                ReportEntityUtils.populateReportableField(reportableField, appEntityField, formatOptions);
                resultList.add(reportableField);
            }
        }

        return resultList;
    }

    public static List<ReportableField> getReportableFieldList(MessageResolver msgResolver,
            List<AppEntityField> fieldList, FormatterOptions formatOptions) throws UnifyException {
        List<ReportableField> resultList = new ArrayList<ReportableField>();
        for (AppEntityField appEntityField : fieldList) {
            if (appEntityField.isReportable()) {
                ReportableField reportableField = new ReportableField();
                ReportEntityUtils.populateReportableField(reportableField, appEntityField, formatOptions);
                resultList.add(reportableField);
            }
        }

        return resultList;
    }

    public static void populateReportableField(ReportableField reportableField, AppEntityField appEntityField,
            FormatterOptions formatOptions) throws UnifyException {
        String description = NameUtils.describeName(appEntityField.getName());
        EntityFieldDataType entityFieldDataType = appEntityField.getDataType();
        Class<?> dataClazz = null;
        if (entityFieldDataType.isListOnly()) {
            // TODO Get parent data type
            dataClazz = String.class;
        } else {
            if (entityFieldDataType.dataType() != null) {
                dataClazz = entityFieldDataType.dataType().javaClass(); // TODO Enumerations?
                if (Number.class.isAssignableFrom(dataClazz)) {
                    reportableField.setHorizontalAlign(HAlignType.RIGHT.name());
                }
            }

            if (StringUtils.isBlank(reportableField.getFormatter())) {
                reportableField.setFormatter(ReportEntityUtils.resolveFormatter(entityFieldDataType, formatOptions));
            }
        }

        if (dataClazz != null) {
            reportableField.setWidth(-1);
            reportableField.setDescription(description);
            reportableField.setName(appEntityField.getName());
            reportableField.setParameterOnly(false);
            reportableField.setType(ConverterUtils.getWrapperClassName(dataClazz));
        }
    }

    public static String resolveFormatter(EntityFieldDataType entityFieldDataType, FormatterOptions formatOptions) {
        if (entityFieldDataType.isDecimal()) {
            return formatOptions.getDecimalFormatter();
        } else if (entityFieldDataType.isInteger()) {
            return formatOptions.getIntegerFormatter();
        } else if (entityFieldDataType.isDate()) {
            return formatOptions.getDateFormatter();
        } else if (entityFieldDataType.isTimestamp()) {
            return formatOptions.getTimestampFormatter();
        }

        return null;
    }
}
