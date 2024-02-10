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

package com.flowcentraltech.flowcentral.common.data;

/**
 * Report listing.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReportListing {

    private String applicationName;

    private String applicationDesc;

    private String longName;

    private String description;

    private boolean allowSecondaryTenants;

    public ReportListing(String applicationName, String applicationDesc, String longName, String description,
            boolean allowSecondaryTenants) {
        this.applicationName = applicationName;
        this.applicationDesc = applicationDesc;
        this.longName = longName;
        this.description = description;
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public String getLongName() {
        return longName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }
}
