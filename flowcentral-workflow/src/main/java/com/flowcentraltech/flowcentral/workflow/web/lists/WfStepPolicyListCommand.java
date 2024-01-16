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

package com.flowcentraltech.flowcentral.workflow.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.business.policies.WfBinaryPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.WfEnrichmentPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.WfProcessPolicy;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.TypeListFactory;

/**
 * Workflow step policy list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("wfsteppolicylist")
public class WfStepPolicyListCommand extends AbstractFlowCentralListCommand<WfStepPolicyParams> {

    @Configurable
    private TypeListFactory typeListFactory;

    public WfStepPolicyListCommand() {
        super(WfStepPolicyParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, WfStepPolicyParams params) throws UnifyException {
        if (params.isPresent()) {
            switch (params.getType()) {
                case ENRICHMENT:
                    return typeListFactory.getTypeList(locale, WfEnrichmentPolicy.class);
                case PROCEDURE:
                    return typeListFactory.getTypeList(locale, WfProcessPolicy.class);
                case POLICY_ROUTING:
                    return typeListFactory.getTypeList(locale, WfBinaryPolicy.class);
                case END:
                case ERROR:
                case RECORD_ACTION:
                case BINARY_ROUTING:
                case MULTI_ROUTING:
                case SET_VALUES:
                case START:
                case USER_ACTION:
                case NOTIFICATION:
                default:
                    break;
            }
        }

        return Collections.emptyList();
    }

}
