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

package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntityQuery;

/**
 * Application applet property query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppAppletPropQuery extends BaseConfigEntityQuery<AppAppletProp> {

    public AppAppletPropQuery() {
        super(AppAppletProp.class);
    }

    public AppAppletPropQuery appAppletId(Long appAppletId) {
        return (AppAppletPropQuery) addEquals("appAppletId", appAppletId);
    }

    public AppAppletPropQuery applicationNameNot(String applicationName) {
        return (AppAppletPropQuery) addNotEquals("applicationName", applicationName);
    }

    public AppAppletPropQuery applicationName(String applicationName) {
        return (AppAppletPropQuery) addEquals("applicationName", applicationName);
    }

    public AppAppletPropQuery appletName(String appletName) {
        return (AppAppletPropQuery) addEquals("appletName", appletName);
    }

    public AppAppletPropQuery name(String name) {
        return (AppAppletPropQuery) addEquals("name", name);
    }

    public AppAppletPropQuery valueBeginsWith(String value) {
        return (AppAppletPropQuery) addBeginsWith("value", value);
    }

    public AppAppletPropQuery value(String value) {
        return (AppAppletPropQuery) addEquals("value", value);
    }
}
