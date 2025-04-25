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

package com.flowcentraltech.flowcentral.common.business;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Environment delegate holder.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EnvironmentDelegateHolder {

    private String entityLongName;

    private String entityClassName;

    private EnvironmentDelegate environmentDelegate;

    public EnvironmentDelegateHolder(String entityLongName, String entityClassName,
            EnvironmentDelegate environmentDelegate) {
        this.entityLongName = entityLongName;
        this.entityClassName = entityClassName;
        this.environmentDelegate = environmentDelegate;
    }

    public String getEntityLongName() {
        return entityLongName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public EnvironmentDelegate getEnvironmentDelegate() {
        return environmentDelegate;
    }

    public String getDataSourceName() throws UnifyException {
        return environmentDelegate.getDataSourceByEntityAlias(entityLongName);
    }

    public boolean isDirect() {
        return environmentDelegate.isDirect();
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
