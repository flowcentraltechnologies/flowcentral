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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.tcdng.unify.core.criterion.CompoundRestriction;

/**
 * Base query object for base configuration named entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseConfigNamedEntityQuery<T extends BaseConfigNamedEntity> extends BaseNamedEntityQuery<T> {

    private static final List<ConfigType> CUSTOM_CONFIG_TYPES = Arrays.asList(ConfigType.CUSTOM, ConfigType.CUSTOMIZED);

    public BaseConfigNamedEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseConfigNamedEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseConfigNamedEntityQuery(Class<T> entityClass, CompoundRestriction restrictions,
            boolean applyAppQueryLimit) {
        super(entityClass, restrictions, applyAppQueryLimit);
    }

    public final BaseConfigNamedEntityQuery<T> configType(ConfigType configType) {
        return (BaseConfigNamedEntityQuery<T>) addEquals("configType", configType);
    }

    public final BaseConfigNamedEntityQuery<T> isCustom() {
        return (BaseConfigNamedEntityQuery<T>) addAmongst("configType", CUSTOM_CONFIG_TYPES);
    }

    public final BaseConfigNamedEntityQuery<T> isNotCustom() {
        return (BaseConfigNamedEntityQuery<T>) addNotAmongst("configType", CUSTOM_CONFIG_TYPES);
    }

}
