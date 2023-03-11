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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;

/**
 * Application applet query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppAppletQuery extends BaseApplicationEntityQuery<AppApplet> {

    public AppAppletQuery() {
        super(AppApplet.class);
    }

    public AppAppletQuery applicationNameNot(String applicationName) {
        return (AppAppletQuery) addNotEquals("applicationName", applicationName);
    }

    public AppAppletQuery routeToApplet(String routeToApplet) {
        return (AppAppletQuery) addEquals("routeToApplet", routeToApplet);
    }

    public AppAppletQuery entity(String entity) {
        return (AppAppletQuery) addEquals("entity", entity);
    }

    public AppAppletQuery entityBeginsWith(String entity) {
        return (AppAppletQuery) addBeginsWith("entity", entity);
    }

    public AppAppletQuery entityIn(Collection<String> entity) {
        return (AppAppletQuery) addAmongst("entity", entity);
    }

    public AppAppletQuery menuAccess(boolean menuAccess) {
        return (AppAppletQuery) addEquals("menuAccess", menuAccess);
    }

    public AppAppletQuery type(AppletType type) {
        return (AppAppletQuery) addEquals("type", type);
    }

    public AppAppletQuery typeNotIn(Collection<AppletType> type) {
        return (AppAppletQuery) addNotAmongst("type", type);
    }

    public AppAppletQuery typeIn(Collection<AppletType> type) {
        return (AppAppletQuery) addAmongst("type", type);
    }

    public AppAppletQuery unreserved() {
        return (AppAppletQuery) addAmongst("type", AppletType.UNRESERVED_LIST);
    }

}
