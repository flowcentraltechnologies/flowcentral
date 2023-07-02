/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.constants.VersionType;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;

/**
 * Convenient abstract base class for application definition query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseApplicationEntityQuery<T extends BaseApplicationEntity>
        extends BaseConfigNamedEntityQuery<T> {

    public BaseApplicationEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseApplicationEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public final BaseApplicationEntityQuery<T> applicationId(Long applicationId) {
        return (BaseApplicationEntityQuery<T>) addEquals("applicationId", applicationId);
    }

    public final BaseApplicationEntityQuery<T> applicationName(String applicationName) {
        return (BaseApplicationEntityQuery<T>) addEquals("applicationName", applicationName);
    }

    public final BaseApplicationEntityQuery<T> applicationNameNot(String applicationName) {
        return (BaseApplicationEntityQuery<T>) addNotEquals("applicationName", applicationName);
    }

    public final BaseApplicationEntityQuery<T> applicationNameIn(Collection<String> applicationNames) {
        return (BaseApplicationEntityQuery<T>) addAmongst("applicationName", applicationNames);
    }

    public final BaseApplicationEntityQuery<T> moduleName(String moduleName) {
        return (BaseApplicationEntityQuery<T>) addEquals("moduleName", moduleName);
    }

    public final BaseApplicationEntityQuery<T> versionType(VersionType versionType) {
        return (BaseApplicationEntityQuery<T>) addEquals("versionType", versionType);
    }

    public final BaseApplicationEntityQuery<T> isCurrent() {
        return (BaseApplicationEntityQuery<T>) addEquals("versionType", VersionType.CURRENT);
    }

    public final BaseApplicationEntityQuery<T> rootMergeVersionNo(String rootMergeVersionNo) {
        return (BaseApplicationEntityQuery<T>) addEquals("rootMergeVersionNo", rootMergeVersionNo);
    }

    public final BaseApplicationEntityQuery<T> isNullRootMergeVersionNo() {
        return (BaseApplicationEntityQuery<T>) addIsNull("rootMergeVersionNo");
    }

}
