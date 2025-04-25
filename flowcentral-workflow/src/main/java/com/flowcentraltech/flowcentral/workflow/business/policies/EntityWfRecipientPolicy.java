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

package com.flowcentraltech.flowcentral.workflow.business.policies;

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.AbstractWfRecipientPolicy;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Entity workflow recipient policy
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "entity-wfrecipientpolicy", description = "$m{workflow.recipientpolicy.entity}")
public class EntityWfRecipientPolicy extends AbstractWfRecipientPolicy {

    @Override
    public List<Recipient> getRecipients(ValueStoreReader wfItemReader, String recipientNameRule,
            String recipientContactRule) throws UnifyException {
        return Arrays.asList(new Recipient(wfItemReader.read(String.class, recipientNameRule),
                wfItemReader.read(String.class, recipientContactRule)));
    }

}
