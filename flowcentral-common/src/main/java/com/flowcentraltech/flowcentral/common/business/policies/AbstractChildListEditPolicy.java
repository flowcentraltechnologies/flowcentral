/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.data.FormMessages;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Convenient abstract base class for child list edit policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractChildListEditPolicy extends AbstractEntryTablePolicy implements ChildListEditPolicy {

    @Override
    public final FormMessages validateEntries(Class<? extends Entity> entityClass, String baseFieldName, Object baseId,
            List<?> instList) throws UnifyException {
        FormMessages messages = new FormMessages();
        validateEntries(messages, entityClass, baseFieldName, baseId, instList);
        return messages;
    }

    protected abstract void validateEntries(FormMessages messages, Class<? extends Entity> entityClass,
            String baseFieldName, Object baseId, List<?> instList) throws UnifyException;

}
