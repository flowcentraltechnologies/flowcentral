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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for form wizard navigation policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFormWizardNavigationPolicy extends AbstractFlowCentralComponent
        implements FormWizardNavigationPolicy {

    private final List<String> pageAttributesNames;

    public AbstractFormWizardNavigationPolicy(List<String> pageAttributesNames) {
        this.pageAttributesNames = pageAttributesNames;
    }

    public AbstractFormWizardNavigationPolicy() {
        this.pageAttributesNames = Collections.emptyList();
    }

    @Override
    public List<String> pageAttributeNames() {
        return pageAttributesNames;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
