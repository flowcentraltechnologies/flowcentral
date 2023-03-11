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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Workflow item recipient policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WfRecipientPolicy extends UnifyComponent {

    /**
     * Gets a list of recipient based on workflow entity instance.
     * 
     * @param wfItemReader
     *                             the workflow item reader
     * @param recipientNameRule
     *                             recipient name rule
     * @param recipientContactRule
     *                             recipient contact rule
     * @return a list of recipients
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Recipient> getRecipients(ValueStoreReader wfItemReader, String recipientNameRule,
            String recipientContactRule) throws UnifyException;
}
