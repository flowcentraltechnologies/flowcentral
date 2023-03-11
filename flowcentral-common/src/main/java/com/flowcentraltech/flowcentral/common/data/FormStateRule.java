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

package com.flowcentraltech.flowcentral.common.data;

import java.util.Set;

import com.tcdng.unify.core.constant.TriState;

/**
 * Form state rule interface.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface FormStateRule {

    /**
     * Gets target form element names.
     * 
     * @return the target list
     */
    Set<String> getTarget();

    /**
     * Gets the required state.
     * 
     * @return the required tri-state
     */
    TriState getRequired();

    /**
     * Gets the visible state.
     * 
     * @return the visible tri-state
     */
    TriState getVisible();

    /**
     * Gets the editable state.
     * 
     * @return the editable tri-state
     */
    TriState getEditable();

    /**
     * Gets the disabled state.
     * 
     * @return the disabled tri-state
     */
    TriState getDisabled();

    /**
     * Checks if rule is TAB element rule
     * 
     * @return the indicator
     */
    boolean isTabRule();

    /**
     * Checks if rule is SECTION element rule
     * 
     * @return the indicator
     */
    boolean isSectionRule();

    /**
     * Checks if rule is FIELD element rule
     * 
     * @return the indicator
     */
    boolean isFieldRule();
}
