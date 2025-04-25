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

package com.flowcentraltech.flowcentral.system.business.tasks;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Schedulable;
import com.tcdng.unify.core.task.TaskInput;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Print to console task.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "printtoconsole-task", description = "Print to Console Task")
@Schedulable(parameters = {
        @Parameter(name = "lineText", description = "Line Text", editor = "!ui-text", type = String.class,
                mandatory = true),
        @Parameter(name = "lineCount", description = "Line Count", editor = "!ui-integer precision:3 size:16",
                type = Integer.class, mandatory = true) })
public class PrintToConsoleTask extends AbstractSchedulableTask {

    @Override
    public void doExecute(TaskMonitor taskMonitor, TaskInput input) throws UnifyException {
        String lineText = input.getParam(String.class, "lineText");
        int lineCount = input.getParam(int.class, "lineCount");
        if (lineText != null) {
            for (int i = 0; i < lineCount; i++) {
                System.out.println(lineText);
            }
        }
    }

}
