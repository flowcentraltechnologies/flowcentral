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

package com.flowcentraltech.flowcentral.common.business;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Environment delegate information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EnvironmentDelegateInfo {

    private String entityLongName;

    private Class<? extends Entity> entityClass;

    private EnvironmentDelegate environmentDelegate;

    public EnvironmentDelegateInfo(String entityLongName, Class<? extends Entity> entityClass,
            EnvironmentDelegate environmentDelegate) {
        this.entityLongName = entityLongName;
        this.entityClass = entityClass;
        this.environmentDelegate = environmentDelegate;
    }

    public String getEntityLongName() {
        return entityLongName;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public EnvironmentDelegate getEnvironmentDelegate() {
        return environmentDelegate;
    }
    
    public String getDataSourceName() throws UnifyException {
        return environmentDelegate.getDataSourceName(entityLongName);
    }

    public boolean isDirect() {
        return environmentDelegate.isDirect();
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
