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

package com.flowcentraltech.flowcentral.application.validation;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.policies.FormReviewContext;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.tcdng.unify.core.UnifyException;

/**
 * Form context evaluator component
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface FormContextEvaluator extends FlowCentralComponent {

    /**
     * Evaluates a form context.
     * 
     * @param ctx
     *             the form context
     * @param vCtx
     *             the validation context
     * @throws UnifyException
     *                        if an error occurs
     */
    void evaluateFormContext(FormContext ctx, FormValidationContext vCtx) throws UnifyException;

    /**
     * Evaluates a form context.
     * 
     * @param ctx
     *             the form context
     * @param vCtx
     *             the validation context
     * @param rCtx
     *             the review context
     * @return the review result
     * @throws UnifyException
     *                        if an error occurs
     */
    ReviewResult reviewFormContext(FormContext ctx, FormValidationContext vCtx, FormReviewContext rCtx)
            throws UnifyException;
}
