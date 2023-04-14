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

package com.flowcentraltech.flowcentral.common.constants;

/**
 * Component type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum ComponentType {

    WIDGET(
            "WDG",
            "Wdg"),
    ENTITY(
            "ENT",
            "Ent"),
    REFERENCE(
            "REF",
            "Ref"),
    APPLET(
            "APL",
            "Apt"),
    CHART(
            "CRT",
            "Crt"),
    DASHBOARD(
            "DSH",
            "Dsh"),
    NOTIFICATION_TEMPLATE(
            "NTP",
            "NotTmpl"),
    REPORT_CONFIGURATION(
            "RPC",
            "RptConf"),
    FORM(
            "FRM",
            "Frm"),
    TABLE(
            "TBL",
            "Tbl"),
    WORKFLOW(
            "WRK",
            "Wf");

    private final String code;

    private final String term;

    private ComponentType(String code, String term) {
        this.code = code;
        this.term = term;
    }

    public String code() {
        return this.code;
    }

    public String term() {
        return this.term;
    }

}
