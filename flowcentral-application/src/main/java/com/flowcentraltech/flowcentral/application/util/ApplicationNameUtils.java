/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

    private static final FactoryMap<String, ApplicationEntityNameParts> applicationNameParts;

    private static final FactoryMap<String, EntityAssignRuleNameParts> assignRuleNameParts;

    static {
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

    public static EntityAssignRuleNameParts getEntityAssignRuleNameParts(String assignRule) throws UnifyException {
        return assignRuleNameParts.get(assignRule);
    }

    public static String getApplicationEntityLongName(String applicationName, String entityName) {
        return StringUtils.dotify(applicationName, entityName);
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

    public static List<ListData> getListableList(List<? extends BaseApplicationEntity> appEntityList) throws UnifyException {
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
    
    public static String addVestigialNamePart(String longName, String vestigial) {
        return longName + VESTIGIAL_INFIX + vestigial;
    }
    
    public static String removeVestigialNamePart(String longName) {
        int index = longName.indexOf(VESTIGIAL_INFIX);
        if (index > 0) {
            return longName.substring(0, index);
        }
        
        return longName;
    }
    
    public static String getVestigialNamePart(String longName) {
        int index = longName.indexOf(VESTIGIAL_INFIX);
        if (index > 0) {
            return longName.substring(index + VESTIGIAL_INFIX.length());
        }
        
        return null;
    }
}
