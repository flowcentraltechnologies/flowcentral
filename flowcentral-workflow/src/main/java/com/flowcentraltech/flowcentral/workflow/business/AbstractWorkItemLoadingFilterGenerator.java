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

package com.flowcentraltech.flowcentral.workflow.business;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Convenient abstract base class for work-item loading filter generator policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractWorkItemLoadingFilterGenerator extends AbstractFlowCentralComponent
        implements WorkItemLoadingFilterGenerator {

    private static final Restriction ID_NULL_RESTRICTION = new IsNull("id");

    private static final Restriction ID_NOT_NULL_RESTRICTION = new IsNotNull("id");
    
    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final EnvironmentService environment() {
        return appletUtilities.environment();
    }

    protected final SystemModuleService system() {
        return appletUtilities.system();
    }

    protected final Restriction noRestriction() {
        return null;
    }

    protected final Restriction allRecordsRestriction() {
        return ID_NOT_NULL_RESTRICTION;
    }

    protected final Restriction noRecordsRestriction() {
        return ID_NULL_RESTRICTION;
    }
}
