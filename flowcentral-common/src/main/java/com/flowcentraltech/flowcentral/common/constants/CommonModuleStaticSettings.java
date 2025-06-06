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
package com.flowcentraltech.flowcentral.common.constants;

import com.flowcentraltech.flowcentral.configuration.constants.AbstractFlowCentralStaticSettings;
import com.flowcentraltech.flowcentral.configuration.constants.ModuleInstallLevelConstants;
import com.tcdng.unify.common.annotation.AutoDetect;

/**
 * Common module static settings.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@AutoDetect
public class CommonModuleStaticSettings extends AbstractFlowCentralStaticSettings {

    public CommonModuleStaticSettings() {
        super("com.flowcentraltech.flowcentral.resources.common-messages", ModuleInstallLevelConstants.COMMON_MODULE_LEVEL);
    }

}
