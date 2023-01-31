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
package com.flowcentraltech.flowcentral.application.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ReplicationElementType;
import com.flowcentraltech.flowcentral.application.constants.ReplicationMatchType;
import com.flowcentraltech.flowcentral.application.entities.AppEntitySearchInput;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequence;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetRules;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterRestrictionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SearchInputConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SearchInputsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValueConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetRuleEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetRulesConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application replication utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationReplicationUtils {

    private static final String ELEMENT_PREFIX = "$e:";

    private static final String REPLACE_PREFIX = "$r:";

    private ApplicationReplicationUtils() {

    }

    public static ApplicationReplicationContext createApplicationReplicationContext(AppletUtilities au,
            String srcApplicationName, String destApplicationName, byte[] replicationRulesFile) throws UnifyException {
        Map<ReplicationElementType, ApplicationReplicationRule.Builder> builders = new HashMap<ReplicationElementType, ApplicationReplicationRule.Builder>();
        ApplicationReplicationRule.Builder namerb = new ApplicationReplicationRule.Builder(ReplicationMatchType.NAME);
        ApplicationReplicationRule.Builder componentrb = new ApplicationReplicationRule.Builder(
                ReplicationMatchType.PREFIX);
        ApplicationReplicationRule.Builder messagerb = new ApplicationReplicationRule.Builder(
                ReplicationMatchType.WILD_SUFFIX);
        ApplicationReplicationRule.Builder classrb = new ApplicationReplicationRule.Builder(ReplicationMatchType.CLASS);
        ApplicationReplicationRule.Builder tablerb = new ApplicationReplicationRule.Builder(
                ReplicationMatchType.PREFIX);
        ApplicationReplicationRule.Builder autoformatrb = new ApplicationReplicationRule.Builder(
                ReplicationMatchType.WILD_PREFIX);
        ApplicationReplicationRule.Builder entityrb = new ApplicationReplicationRule.Builder(
                ReplicationMatchType.ENTITY);
        ApplicationReplicationRule.Builder fieldrb = new ApplicationReplicationRule.Builder(ReplicationMatchType.WILD);
        builders.put(ReplicationElementType.NAME, namerb);
        builders.put(ReplicationElementType.COMPONENT, componentrb);
        builders.put(ReplicationElementType.MESSAGE, messagerb);
        builders.put(ReplicationElementType.CLASS, classrb);
        builders.put(ReplicationElementType.TABLE, tablerb);
        builders.put(ReplicationElementType.AUTOFORMAT, autoformatrb);
        builders.put(ReplicationElementType.ENTITY, entityrb);
        builders.put(ReplicationElementType.FIELD, fieldrb);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(replicationRulesFile)))) {
            ApplicationReplicationRule.Builder currentrb = null;
            String line = null;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) {
                    continue;
                }

                if (line.startsWith(ELEMENT_PREFIX)) {
                    final String body = line.substring(ELEMENT_PREFIX.length());
                    String[] parts = body.split("\\|");
                    if (parts.length > 2) {
                        throw new RuntimeException("Improper element definition in line " + lineNumber + ". Parts = "
                                + Arrays.asList(parts));
                    }

                    ReplicationElementType type = ReplicationElementType.fromName(parts[0]);
                    if (type == null) {
                        throw new RuntimeException("Unknown element type \'" + parts[0] + "\' in line " + lineNumber
                                + ". Parts = " + Arrays.asList(parts));
                    }

                    currentrb = builders.get(type);
                    if (parts.length == 2) {
                        currentrb.concat(parts[1]);
                    }
                } else if (line.startsWith(REPLACE_PREFIX)) {
                    final String body = line.substring(REPLACE_PREFIX.length());
                    String[] parts = body.split("=");
                    if (parts.length != 2) {
                        throw new RuntimeException("Improper replace definition in line " + lineNumber + ". Parts = "
                                + Arrays.asList(parts));
                    }

                    if (currentrb == null) {
                        throw new RuntimeException(
                                "No replication element initialized. Parts = " + Arrays.asList(parts));
                    }

                    currentrb.replace(parts[0], parts[1]);
                } else {
                    throw new RuntimeException("Unknown replacement rule definition in line " + lineNumber + ".");
                }

                lineNumber++;
            }
        } catch (Exception e) {
            throw new UnifyOperationException(e, "ApplicationReplicationUtils", e.getMessage());
        }

        entityrb.replace(srcApplicationName, destApplicationName);
        ApplicationReplicationRule nameRule = namerb.build();
        return new ApplicationReplicationContext(au, srcApplicationName, destApplicationName, nameRule,
                componentrb.build(), messagerb.build(), classrb.build(), tablerb.build(), autoformatrb.build(),
                fieldrb.build(), entityrb.build().setNameRule(nameRule));
    }

    public static FilterConfig getReplicatedFilterConfig(ApplicationReplicationContext ctx, AppFilter appFilter)
            throws UnifyException {
        FilterConfig filterConfig = InputWidgetUtils.getFilterConfig(ctx.au(), appFilter);
        if (filterConfig != null) {
            filterConfig.setDescription(ctx.messageSwap(filterConfig.getDescription()));
            filterConfig.setFilterGenerator(ctx.componentSwap(filterConfig.getFilterGenerator()));
            for (FilterRestrictionConfig restrictonConfig : filterConfig.getRestrictionList()) {
                restrictonConfig.setField(ctx.fieldSwap(restrictonConfig.getField()));
                if (restrictonConfig.getType().isFieldVal()) {
                    restrictonConfig.setParamA(ctx.fieldSwap(restrictonConfig.getParamA()));
                    restrictonConfig.setParamB(ctx.fieldSwap(restrictonConfig.getParamB()));
                }
            }
        }

        return filterConfig;
    }

    public static SetValuesConfig getReplicatedSetValuesConfig(ApplicationReplicationContext ctx, String valueGenerator,
            AppSetValues appSetValues) throws UnifyException {
        SetValuesConfig setValuesConfig = InputWidgetUtils.getSetValuesConfig(valueGenerator, appSetValues);
        if (setValuesConfig != null) {
            setValuesConfig.setDescription(ctx.messageSwap(setValuesConfig.getDescription()));
            if (!DataUtils.isBlank(setValuesConfig.getSetValueList())) {
                for (SetValueConfig setValueConfig : setValuesConfig.getSetValueList()) {
                    setValueConfig.setFieldName(ctx.fieldSwap(setValueConfig.getFieldName()));
                    if (SetValueType.GENERATOR.equals(setValueConfig.getType())) {
                        setValueConfig.setValue(ctx.componentSwap(setValueConfig.getValue()));
                    } else if (SetValueType.IMMEDIATE_FIELD.equals(setValueConfig.getType())) {
                        setValueConfig.setValue(ctx.fieldSwap(setValueConfig.getValue()));
                    }
                }
            }
        }

        return setValuesConfig;
    }

    public static WidgetRulesConfig getReplicatedWidgetRulesConfig(ApplicationReplicationContext ctx,
            AppWidgetRules widgetRules) throws UnifyException {
        WidgetRulesConfig widgetRulesConfig = InputWidgetUtils.getWidgetRulesConfig(widgetRules);
        if (widgetRulesConfig != null && widgetRulesConfig.getEntryList() != null) {
            for (WidgetRuleEntryConfig widgetRuleEntryConfig : widgetRulesConfig.getEntryList()) {
                widgetRuleEntryConfig.setFieldName(ctx.fieldSwap(widgetRuleEntryConfig.getFieldName()));
                widgetRuleEntryConfig.setWidget(ctx.componentSwap(widgetRuleEntryConfig.getWidget()));
            }
        }

        return widgetRulesConfig;
    }

    public static SearchInputsConfig getReplicatedSearchInputsConfig(ApplicationReplicationContext ctx,
            AppEntitySearchInput appEntitySearchInput) throws UnifyException {
        SearchInputsConfig searchInputsConfig = InputWidgetUtils.getSearchInputConfig(appEntitySearchInput);
        if (searchInputsConfig != null) {
            searchInputsConfig.setDescription(ctx.messageSwap(searchInputsConfig.getDescription()));
            for (SearchInputConfig searchInputConfig : searchInputsConfig.getInputList()) {
                searchInputConfig.setField(ctx.fieldSwap(searchInputConfig.getField()));
                searchInputConfig.setLabel(ctx.messageSwap(searchInputConfig.getLabel()));
            }
        }

        return searchInputsConfig;
    }

    public static FieldSequenceConfig getReplicatedFieldSequenceConfig(ApplicationReplicationContext ctx,
            AppFieldSequence appFieldSequence) throws UnifyException {
        FieldSequenceConfig fieldSequenceConfig = InputWidgetUtils.getFieldSequenceConfig(appFieldSequence);
        if (fieldSequenceConfig != null) {
            for (FieldSequenceEntryConfig entryConfig : fieldSequenceConfig.getEntryList()) {
                entryConfig.setFieldName(ctx.fieldSwap(entryConfig.getFieldName()));
            }
        }

        return fieldSequenceConfig;
    }
}
