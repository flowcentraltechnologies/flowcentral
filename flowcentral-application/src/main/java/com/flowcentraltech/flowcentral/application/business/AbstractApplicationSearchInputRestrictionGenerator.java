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
package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.common.business.AbstractSearchInputRestrictionGenerator;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.SearchInputRestrictionDataType;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient application search restriction generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplicationSearchInputRestrictionGenerator
        extends AbstractSearchInputRestrictionGenerator {

    @Configurable
    private AppletUtilities au;

    public AbstractApplicationSearchInputRestrictionGenerator(SearchInputRestrictionDataType inputType) {
        super(inputType);
    }

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected EnvironmentService environment() {
        return au.environment();
    }

}