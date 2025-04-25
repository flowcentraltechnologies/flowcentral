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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.tcdng.unify.core.task.TaskSetup;

/**
 * Entity list action result
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityListActionResult {

    private TaskSetup resultTaskSetup;

    private String resultTaskCaption;

    private Object result;

    public EntityListActionResult(EntityListActionContext ctx, TaskSetup resultTaskSetup, String resultTaskCaption) {
        this.resultTaskSetup = resultTaskSetup;
        this.resultTaskCaption = resultTaskCaption;
        this.result = ctx.getResult();
    }

    public EntityListActionResult(EntityListActionContext ctx) {
        this.result = ctx.getResult();
    }

    public TaskSetup getResultTaskSetup() {
        return resultTaskSetup;
    }

    public String getResultTaskCaption() {
        return resultTaskCaption;
    }

    public Object getResult() {
        return result;
    }

    public boolean isWithTaskResult() {
        return resultTaskSetup != null;
    }
}
