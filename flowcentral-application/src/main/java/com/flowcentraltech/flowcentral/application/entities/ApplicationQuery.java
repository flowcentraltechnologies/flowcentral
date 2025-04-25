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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;

/**
 * Application query object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationQuery extends BaseConfigNamedEntityQuery<Application> {

    public ApplicationQuery() {
        super(Application.class);
    }

    public ApplicationQuery moduleId(Long moduleId) {
        return (ApplicationQuery) addEquals("moduleId", moduleId);
    }

    public ApplicationQuery moduleName(String moduleName) {
        return (ApplicationQuery) addEquals("moduleName", moduleName);
    }

    public ApplicationQuery isMenuAccess() {
        return (ApplicationQuery) addEquals("menuAccess", true);
    }


    public ApplicationQuery isDevelopable() {
        return (ApplicationQuery) addEquals("developable", true);
    }

}
