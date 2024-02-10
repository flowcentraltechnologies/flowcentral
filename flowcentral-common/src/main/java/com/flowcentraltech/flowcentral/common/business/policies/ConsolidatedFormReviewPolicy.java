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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Consolidated form review policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ConsolidatedFormReviewPolicy extends FlowCentralComponent {

    /**
     * Perform the review of a form's entity object.
     * 
     * @param instValueStore
     *                       the object value store
     * @param reviewType
     *                       the review type
     * @return the review result
     * @throws UnifyException
     *                        if an error occurs
     */
    List<TargetFormMessage> review(ValueStore instValueStore, FormReviewType reviewType) throws UnifyException;
}
