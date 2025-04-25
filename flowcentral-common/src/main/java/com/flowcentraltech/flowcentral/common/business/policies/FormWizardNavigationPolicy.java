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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.ValidationErrors;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Form wizard navigation policy
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface FormWizardNavigationPolicy extends FlowCentralComponent {

    /**
     * Gets the page attribute names.
     * 
     * @return Gets the page attributes
     */
    List<String> pageAttributeNames();
    
    /**
     * Executes on next navigation.
     * 
     * @param inst
     *                       the entity value store
     * @param errors
     *                       errors
     * @param currentPage
     *                       the current page
     * @param pageAttributes
     *                       the page attributes
     * @throws UnifyException
     *                        if an error occurs
     */
    void onNext(ValueStore inst, ValidationErrors errors, int currentPage, Map<String, Object> pageAttributes)
            throws UnifyException;

    /**
     * Executes on previous navigation.
     * 
     * @param inst
     *                       the entity value store
     * @param currentPage
     *                       the current page
     * @param pageAttributes
     *                       the page attributes
     * @throws UnifyException
     *                        if an error occurs
     */
    void onPrevious(ValueStore inst, ValidationErrors errors, int currentPage, Map<String, Object> pageAttributes)
            throws UnifyException;
}
