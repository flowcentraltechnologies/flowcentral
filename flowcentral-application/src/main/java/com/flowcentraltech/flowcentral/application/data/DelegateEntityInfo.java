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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity delegate information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DelegateEntityInfo {

    private String delegate;

    private String entityName;

    private String dataSourceName;

    public DelegateEntityInfo(String delegate, String entityName, String dataSourceName) {
        this.delegate = delegate;
        this.entityName = entityName;
        this.dataSourceName = dataSourceName;
    }

    public DelegateEntityInfo(String entityName) {
        this.entityName = entityName;
    }

    public String getDelegate() {
        return delegate;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }
    
    public boolean isWithDelegate() {
        return !StringUtils.isBlank(delegate);
    }
    
    public boolean isWithDataSource() {
        return !StringUtils.isBlank(dataSourceName);
    }
    
    public static List<String> getEntityAliases(List<DelegateEntityInfo> delegateEntityInfos) {
        if (!DataUtils.isBlank(delegateEntityInfos)) {
            List<String> entityAliases = new ArrayList<String>();
            for (DelegateEntityInfo delegateEntityInfo : delegateEntityInfos) {
                entityAliases.add(delegateEntityInfo.getEntityName());
            }

            return entityAliases;
        }

        return Collections.emptyList();
    }
}
