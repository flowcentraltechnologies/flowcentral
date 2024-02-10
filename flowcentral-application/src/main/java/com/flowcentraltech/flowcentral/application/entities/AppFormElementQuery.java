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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;

/**
 * Application form element query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppFormElementQuery extends BaseConfigEntityQuery<AppFormElement> {

    public AppFormElementQuery() {
        super(AppFormElement.class);
    }

    public AppFormElementQuery appFormId(Long appFormId) {
        return (AppFormElementQuery) addEquals("appFormId", appFormId);
    }

    public AppFormElementQuery type(FormElementType type) {
        return (AppFormElementQuery) addEquals("type", type);
    }

    public AppFormElementQuery applicationNameNot(String applicationName) {
        return (AppFormElementQuery) addNotEquals("applicationName", applicationName);
    }

    public AppFormElementQuery tabAppletBeginsWith(String tabApplet) {
        return (AppFormElementQuery) addBeginsWith("tabApplet", tabApplet);
    }

    public AppFormElementQuery tabReferenceBeginsWith(String tabReference) {
        return (AppFormElementQuery) addBeginsWith("tabReference", tabReference);
    }

    public AppFormElementQuery tabMappedFormBeginsWith(String tabMappedForm) {
        return (AppFormElementQuery) addBeginsWith("tabMappedForm", tabMappedForm);
    }

    public AppFormElementQuery inputReferenceBeginsWith(String inputReference) {
        return (AppFormElementQuery) addBeginsWith("inputReference", inputReference);
    }

    public AppFormElementQuery inputWidgetBeginsWith(String inputWidget) {
        return (AppFormElementQuery) addBeginsWith("inputWidget", inputWidget);
    }
}
