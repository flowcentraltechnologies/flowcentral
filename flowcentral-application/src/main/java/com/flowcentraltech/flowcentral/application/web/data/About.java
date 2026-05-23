/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.data;

import java.util.Date;

/**
 * About data object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class About {

    private String applicationCode;

    private String applicationName;

    private String applicationVersion;

    private Date serverDateTime;

    private String serverTimeZone;

    public About(String applicationCode, String applicationName, String applicationVersion, Date serverDateTime,
            String serverTimeZone) {
        this.applicationCode = applicationCode;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.serverDateTime = serverDateTime;
        this.serverTimeZone = serverTimeZone;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public Date getServerDateTime() {
        return serverDateTime;
    }

    public String getServerTimeZone() {
        return serverTimeZone;
    }

}
