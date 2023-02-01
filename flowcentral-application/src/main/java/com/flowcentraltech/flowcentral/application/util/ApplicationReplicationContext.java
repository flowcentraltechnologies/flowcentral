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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.tcdng.unify.core.UnifyException;

/**
 * Application replication context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationReplicationContext {

    private final AppletUtilities au;

    private final String srcApplicationName;

    private final String destApplicationName;

    private final ApplicationReplicationRule nameRule;

    private final ApplicationReplicationRule componentRule;

    private final ApplicationReplicationRule messageRule;

    private final ApplicationReplicationRule classRule;

    private final ApplicationReplicationRule tableRule;

    private final ApplicationReplicationRule autoFormatRule;

    private final ApplicationReplicationRule fieldRule;

    private final ApplicationReplicationRule entityRule;

    public ApplicationReplicationContext(AppletUtilities au, String srcApplicationName, String destApplicationName,
            ApplicationReplicationRule nameRule, ApplicationReplicationRule componentRule,
            ApplicationReplicationRule messageRule, ApplicationReplicationRule classRule,
            ApplicationReplicationRule tableRule, ApplicationReplicationRule autoFormatRule,
            ApplicationReplicationRule fieldRule, ApplicationReplicationRule entityRule) {
        this.au = au;
        this.srcApplicationName = srcApplicationName;
        this.destApplicationName = destApplicationName;
        this.nameRule = nameRule;
        this.componentRule = componentRule;
        this.messageRule = messageRule;
        this.classRule = classRule;
        this.tableRule = tableRule;
        this.autoFormatRule = autoFormatRule;
        this.fieldRule = fieldRule;
        this.entityRule = entityRule;
    }

    public AppletUtilities au() {
        return au;
    }

    public String getSrcApplicationName() {
        return srcApplicationName;
    }

    public String getDestApplicationName() {
        return destApplicationName;
    }

    public String nameSwap(String name) throws UnifyException {
        return nameRule.apply(name);
    }

    public String componentSwap(String component) throws UnifyException {
        return componentRule.apply(component);
    }

    public String messageSwap(String message) throws UnifyException {
        return messageRule.apply(message);
    }

    public String classSwap(String className) throws UnifyException {
        return classRule.apply(className);
    }

    public String tableSwap(String tableName) throws UnifyException {
        return tableRule.apply(tableName);
    }

    public String fieldSwap(String fieldName) throws UnifyException {
        return fieldRule.apply(fieldName);
    }

    public String autoFormatSwap(String autoFormat) throws UnifyException {
        return autoFormatRule.apply(autoFormat);
    }

    public String entitySwap(String entityName) throws UnifyException {
        return entityRule.apply(entityName);
    }
}
