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

package com.flowcentraltech.flowcentral.application.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormReviewPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.FormReviewContext;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessages;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient abstract base class for consolidated for review policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplicationConsolidatedFormReviewPolicy extends AbstractFlowCentralComponent
        implements ConsolidatedFormReviewPolicy {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private EnvironmentService environmentService;

    @Override
    public final List<TargetFormMessage> review(FormReviewContext ctx, ValueStore instValueStore) throws UnifyException {
        TargetFormMessages messages = new TargetFormMessages();
        review(ctx, instValueStore, messages);
        return messages.getMessages();
    }

    protected abstract void review(FormReviewContext ctx, ValueStore instValueStore, TargetFormMessages messages)
            throws UnifyException;

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected ApplicationModuleService application() {
        return applicationModuleService;
    }

    protected void validateChildExists(TargetFormMessages messages, String fkFieldName, Long parentId,
            String entityLongName, String errMsgKey, String... targets) throws UnifyException {
        validateChildExists(messages, fkFieldName, parentId, entityLongName, false, errMsgKey, targets);
    }

    protected void validateChildExistsSkippable(TargetFormMessages messages, String fkFieldName, Long parentId,
            String entityLongName, String errMsgKey, String... targets) throws UnifyException {
        validateChildExists(messages, fkFieldName, parentId, entityLongName, true, errMsgKey, targets);
    }

    @SuppressWarnings("unchecked")
    private void validateChildExists(TargetFormMessages messages, String fkFieldName, Long parentId,
            String entityLongName, boolean skippable, String errMsgKey, String... targets) throws UnifyException {
        EntityClassDef entityClassDef = application().getEntityClassDef(entityLongName);
        int count = environment().countAll(
                Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals(fkFieldName, parentId));
        if (count == 0) {
            if (skippable) {
                messages.addSkippableError(errMsgKey, targets);
            } else {
                messages.addError(errMsgKey, targets);
            }
        }
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
