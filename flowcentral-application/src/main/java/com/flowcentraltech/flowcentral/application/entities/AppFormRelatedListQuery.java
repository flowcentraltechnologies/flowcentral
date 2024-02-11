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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;

/**
 * Application form related list query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppFormRelatedListQuery extends BaseConfigNamedEntityQuery<AppFormRelatedList> {

    public AppFormRelatedListQuery() {
        super(AppFormRelatedList.class);
    }

    public AppFormRelatedListQuery appFormId(Long appFormId) {
        return (AppFormRelatedListQuery) addEquals("appFormId", appFormId);
    }

    public AppFormRelatedListQuery applicationNameNot(String applicationName) {
        return (AppFormRelatedListQuery) addNotEquals("applicationName", applicationName);
    }

    public AppFormRelatedListQuery appletBeginsWith(String applet) {
        return (AppFormRelatedListQuery) addBeginsWith("applet", applet);
    }
}
