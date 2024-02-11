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

package com.flowcentraltech.flowcentral.application.validation;

import com.flowcentraltech.flowcentral.common.business.RuleListComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Validator component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface Validator extends RuleListComponent {

    /**
     * Validates supplied value.
     * 
     * @param val
     *            the value to validate
     * @return true if successfully validated otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean validate(Object val) throws UnifyException;

    /**
     * Validates supplied value using rule.
     * 
     * @param rule
     *             the optional validation rule to use
     * @param val
     *             the value to validate
     * @return true if successfully validated otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean validate(String rule, Object val) throws UnifyException;

    /**
     * Gets a failure message for this validator based on supplied parameters.
     * 
     * @param rule
     *               the validation rule
     * @param params
     *               the message parameters
     * @return the failure message
     * @throws UnifyException
     *                        if an error occurs
     */
    String getFailureMessage(String rule, Object... params) throws UnifyException;
}
