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

package com.flowcentraltech.flowcentral.common.entities;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Base configuration named entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Policy("baseconfignamed-entitypolicy")
public abstract class BaseConfigNamedEntity extends BaseNamedEntity implements ConfigEntity {

    @ForeignKey
    private ConfigType configType;

    @ListOnly(key = "configType", property = "description")
    private String configTypeDesc;

    public BaseConfigNamedEntity() {
        this.configType = ConfigType.CUSTOM;
    }

    @Override
    public final ConfigType getConfigType() {
        return configType;
    }

    @Override
    public final void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public final String getConfigTypeDesc() {
        return configTypeDesc;
    }

    public final void setConfigTypeDesc(String configTypeDesc) {
        this.configTypeDesc = configTypeDesc;
    }

}
