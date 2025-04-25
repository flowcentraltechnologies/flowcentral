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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.constants.AbstractUnifyStaticSettings;

/**
 * Convenient abstract base class for flowcentral static settings.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractFlowCentralStaticSettings extends AbstractUnifyStaticSettings
        implements FlowCentralStaticSettings {

    private String installerName;

    private String moduleConfigName;

    public AbstractFlowCentralStaticSettings(String installerName, String moduleConfigName, String messageBase,
            int level) {
        super(messageBase, level);
        this.installerName = installerName;
        this.moduleConfigName = moduleConfigName;
    }

    public AbstractFlowCentralStaticSettings(String messageBase, int level) {
        super(messageBase, level);
    }

    @Override
    public String installerName() {
        return installerName;
    }

    @Override
    public String moduleConfigName() {
        return moduleConfigName;
    }

}
