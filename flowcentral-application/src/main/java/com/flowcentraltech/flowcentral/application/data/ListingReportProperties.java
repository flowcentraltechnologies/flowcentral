/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Map;

/**
 * Listing report properties.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ListingReportProperties extends ListingProperties {

    public static final String PROPERTY_DOCSTYLE = "prop.document.style";
    
    private String name;

    private boolean bodyBorderless;
    
    public ListingReportProperties(String name, Map<String, Object> properties) {
        super(properties);
        this.name = name;
    }

    public ListingReportProperties(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isBodyBorderless() {
        return bodyBorderless;
    }

    public void setBodyBorderless(boolean bodyBorderless) {
        this.bodyBorderless = bodyBorderless;
    }
    
}
