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

package com.flowcentraltech.flowcentral.workflow.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Work-item loading filter generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface WorkItemLoadingFilterGenerator extends FlowCentralComponent {

    /**
     * Generates restriction based on supplied workflow step information.
     * 
     * @param workflow
     *                 the workflow long name
     * @param step
     *                 the workflow step name
     * @throws UnifyException
     *                        if an error occurs
     */
    Restriction generate(String workflow, String step) throws UnifyException;
}
