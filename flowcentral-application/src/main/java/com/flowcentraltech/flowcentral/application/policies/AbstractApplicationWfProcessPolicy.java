/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractWfProcessPolicy;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for application workflow process policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractApplicationWfProcessPolicy extends AbstractWfProcessPolicy {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    protected final ApplicationModuleService application() {
        return applicationModuleService;
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

}
