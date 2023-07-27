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

import com.tcdng.unify.core.util.StringUtils;

/**
 * Application entity name parts.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationEntityNameParts {

    private String longName;

    private String applicationName;

    private String entityName;

    private String minorName;

    public ApplicationEntityNameParts(String longName, String applicationName, String entityName) {
        this.longName = longName;
        this.applicationName = applicationName;
        this.entityName = entityName;
    }

    public ApplicationEntityNameParts(String longName, String applicationName, String entityName, String minorName) {
        this.longName = longName;
        this.applicationName = applicationName;
        this.entityName = entityName;
        this.minorName = minorName;
    }

    public String getLongName() {
        return longName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getMinorName() {
        return minorName;
    }

    public boolean isWithMinorName() {
        return !StringUtils.isBlank(minorName);
    }
}
