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
package com.flowcentraltech.flowcentral.application.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application name utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationNameUtils {

    public static final String RESERVED_FC_PREFIX = "__";

    public static final String VESTIGIAL_INFIX = "_0_";

    public static final String PSEUDO_INFIX = "_1_";

    public static final String WORKFLOW_COPY_UPDATE_DRAFT_PATH_SUFFIX = "wcuds";

    private static final String WORKFLOW_COPY_CREATE_WORKFLOW_NAME_SUFFIX = "wccw";

    private static final String WORKFLOW_COPY_UPDATE_WORKFLOW_NAME_SUFFIX = "wcuw";

    private static final String WORKFLOW_COPY_DELETE_WORKFLOW_NAME_SUFFIX = "wcdw";

    private static final String WORKFLOW_LOADING_APPLET_NAME_SUFFIX = "wla";

    private static final FactoryMap<String, AtomicInteger> tabSequences;

    private static final FactoryMap<String, ApplicationEntityNameParts> applicationNameParts;

    private static final FactoryMap<String, EntityAssignRuleNameParts> assignRuleNameParts;

    private static final AtomicLong newPseudoIdCounter = new AtomicLong();

    static {
        tabSequences = new FactoryMap<String, AtomicInteger>()
            {

                @Override
                protected AtomicInteger create(String tabName, Object... params) throws Exception {
                    return new AtomicInteger();
                }

            };

        applicationNameParts = new FactoryMap<String, ApplicationEntityNameParts>()
            {

                @Override
                protected ApplicationEntityNameParts create(String longName, Object... arg1) throws Exception {
                    try {
                        String[] po = StringUtils.dotSplit(longName);
                        return po.length == 3 ? new ApplicationEntityNameParts(longName, po[0], po[1], po[2])
                                : new ApplicationEntityNameParts(longName, po[0], po[1]);
                    } catch (Exception e) {
                        throw new RuntimeException("Name parts error: longName = " + longName, e);
                    }
                }

            };

        assignRuleNameParts = new FactoryMap<String, EntityAssignRuleNameParts>()
            {

                @Override
                protected EntityAssignRuleNameParts create(String assignRule, Object... arg1) throws Exception {
                    try {
                        String[] po = StringUtils.charSplit(assignRule, ':');
                        if (po.length == 4) {
                            return new EntityAssignRuleNameParts(po[0], po[1], po[2], po[3]);
                        } else {
                            return new EntityAssignRuleNameParts(po[0], po[1], po[2], null);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Name parts error: assignRule = " + assignRule, e);
                    }
                }

            };
    }

    private ApplicationNameUtils() {

    }

    public static String getNextSequencedTabName(String tabName) throws UnifyException {
        return tabName + "_" + tabSequences.get(tabName).incrementAndGet();
    }

    public static EntityAssignRuleNameParts getEntityAssignRuleNameParts(String assignRule) throws UnifyException {
        return assignRuleNameParts.get(assignRule);
    }

    public static String getApplicationEntityLongName(String applicationName, String entityName) {
        return StringUtils.dotify(applicationName, entityName);
    }

    public static String getApplicationEntityLongName(String applicationName, String entityName, String minorName) {
        return StringUtils.dotify(applicationName, entityName, minorName);
    }

    public static boolean isLongName(String longName) throws UnifyException {
        return longName.indexOf('.') > 0;
    }

    public static ApplicationEntityNameParts getApplicationEntityNameParts(String longName) throws UnifyException {
        return applicationNameParts.get(longName);
    }

    public static String ensureLongNameReference(String defaultApplicationName, String entityName) {
        if (!StringUtils.isBlank(entityName)) {
            if (entityName.indexOf('.') > 0) {
                return entityName;
            }

            return ApplicationNameUtils.getApplicationEntityLongName(defaultApplicationName, entityName);
        }

        return null;
    }

    public static List<String> getApplicationEntityLongNames(List<? extends BaseApplicationEntity> appEntityList) {
        if (!DataUtils.isBlank(appEntityList)) {
            List<String> list = new ArrayList<String>();
            for (BaseApplicationEntity appEntity : appEntityList) {
                list.add(ApplicationNameUtils.getApplicationEntityLongName(appEntity.getApplicationName(),
                        appEntity.getName()));
            }

            return list;
        }

        return Collections.emptyList();
    }

    public static List<ListData> getListableList(List<? extends BaseApplicationEntity> appEntityList)
            throws UnifyException {
        if (!DataUtils.isBlank(appEntityList)) {
            List<ListData> list = new ArrayList<ListData>();
            for (BaseApplicationEntity appEntity : appEntityList) {
                list.add(new ListData(ApplicationNameUtils.getApplicationEntityLongName(appEntity.getApplicationName(),
                        appEntity.getName()), appEntity.getDescription()));
            }

            DataUtils.sortAscending(list, ListData.class, "listDescription");
            return list;
        }

        return Collections.emptyList();
    }

    public static String getWorkflowLoadingAppletName(String applicationName, String loadingTableName) {
        return getApplicationEntityLongName(applicationName, loadingTableName + WORKFLOW_LOADING_APPLET_NAME_SUFFIX);
    }

    public static String getWorkflowCopyCreateWorkflowName(String appletName) {
        return appletName + WORKFLOW_COPY_CREATE_WORKFLOW_NAME_SUFFIX;
    }

    public static String getWorkflowCopyUpdateWorkflowName(String appletName) {
        return appletName + WORKFLOW_COPY_UPDATE_WORKFLOW_NAME_SUFFIX;
    }

    public static String getWorkflowCopyDeleteWorkflowName(String appletName) {
        return appletName + WORKFLOW_COPY_DELETE_WORKFLOW_NAME_SUFFIX;
    }

    public static String addPseudoNamePart(String longName) {
        return longName + PSEUDO_INFIX + newPseudoIdCounter.incrementAndGet();
    }

    public static String addVestigialNamePart(String longName, String vestigial) {
        return longName + VESTIGIAL_INFIX + vestigial;
    }

    public static AppletNameParts getAppletNameParts(String extAppletName) {
        int index = extAppletName.indexOf(VESTIGIAL_INFIX);
        if (index > 0) {
            return new AppletNameParts(extAppletName, extAppletName.substring(0, index),
                    extAppletName.substring(index + VESTIGIAL_INFIX.length()), null);
        }

        index = extAppletName.indexOf(PSEUDO_INFIX);
        if (index > 0) {
            return new AppletNameParts(extAppletName, extAppletName.substring(0, index), null,
                    extAppletName.substring(index + PSEUDO_INFIX.length()));
        }

        return new AppletNameParts(extAppletName);
    }
}
