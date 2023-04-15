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
package com.flowcentraltech.flowcentral.application.web.data;

/**
 * Formats.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Formats {

    public static final Formats DEFAULT = new Formats();
    
    private String decimalFormat;

    private String dateFormat;

    private String timestampFormat;

    private Formats() {
        this(null, null, null);
    }

    public Formats(String decimalFormat, String dateFormat, String timestampFormat) {
        this.decimalFormat = decimalFormat != null ? decimalFormat : "###,###.00";
        this.dateFormat = dateFormat != null ? dateFormat : "yyyy-MM-dd";
        this.timestampFormat = timestampFormat != null ? timestampFormat : "yyyy-MM-dd HH:mm:ss";
    }

    public String getDecimalFormat() {
        return decimalFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }
    
}