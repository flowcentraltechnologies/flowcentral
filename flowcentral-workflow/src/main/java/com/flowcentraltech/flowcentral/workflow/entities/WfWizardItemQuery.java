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

package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseEntityQuery;

/**
 * Workflow wizard item query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfWizardItemQuery extends BaseEntityQuery<WfWizardItem> {

    public WfWizardItemQuery() {
        super(WfWizardItem.class);
    }

    public WfWizardItemQuery wizard(String wizard) {
        return (WfWizardItemQuery) addEquals("wizard", wizard);
    }

    public WfWizardItemQuery ownerId(String ownerId) {
        return (WfWizardItemQuery) addEquals("ownerId", ownerId);
    }

    public WfWizardItemQuery primaryEntityId(Long primaryEntityId) {
        return (WfWizardItemQuery) addEquals("primaryEntityId", primaryEntityId);
    }
}
