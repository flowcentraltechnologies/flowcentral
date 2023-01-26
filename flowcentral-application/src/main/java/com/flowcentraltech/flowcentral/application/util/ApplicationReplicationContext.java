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

/**
 * Application replication context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationReplicationContext {

    private final AppletUtilities au;

    private final ApplicationReplicationRule nameRule;

    private final ApplicationReplicationRule componentRule;

    private final ApplicationReplicationRule messageRule;

    private final ApplicationReplicationRule classRule;

    private final ApplicationReplicationRule tableRule;

    private final ApplicationReplicationRule autoFormatRule;

    private final ApplicationReplicationRule entityRule;

    public ApplicationReplicationContext(AppletUtilities au, ApplicationReplicationRule nameRule,
            ApplicationReplicationRule componentRule, ApplicationReplicationRule messageRule,
            ApplicationReplicationRule classRule, ApplicationReplicationRule tableRule,
            ApplicationReplicationRule autoFormatRule, ApplicationReplicationRule entityRule) {
        this.au = au;
        this.nameRule = nameRule;
        this.componentRule = componentRule;
        this.messageRule = messageRule;
        this.classRule = classRule;
        this.tableRule = tableRule;
        this.autoFormatRule = autoFormatRule;
        this.entityRule = entityRule;
    }

    public AppletUtilities au() {
        return au;
    }

    public String nameSwap(String name) {
        return nameRule.apply(name);
    }

    public String componentSwap(String component) {
        return componentRule.apply(component);
    }

    public String messageSwap(String message) {
        return messageRule.apply(message);
    }

    public String classSwap(String className) {
        return classRule.apply(className);
    }

    public String tableSwap(String tableName) {
        return tableRule.apply(tableName);
    }

    public String autoFormatSwap(String autoFormat) {
        return autoFormatRule.apply(autoFormat);
    }

    public String entitySwap(String entityName) {
        return entityRule.apply(entityName);
    }
}
