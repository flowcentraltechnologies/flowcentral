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

package com.flowcentraltech.flowcentral.workflow.web.lists;

import com.tcdng.unify.core.list.AbstractListParam;

/**
 * Workflow recipient policy parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfRecipientPolicyParams extends AbstractListParam {

    private String entity;

    private String recipientPolicy;

    public WfRecipientPolicyParams(String entity, String recipientPolicy) {
        this.entity = entity;
        this.recipientPolicy = recipientPolicy;
    }

    public String getRecipientPolicy() {
        return recipientPolicy;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public boolean isPresent() {
        return recipientPolicy != null && entity != null;
    }

    @Override
    public String toString() {
        return "WfRecipientPolicyParams [entity=" + entity + ", recipientPolicy=" + recipientPolicy + "]";
    }

}
