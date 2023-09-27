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
package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Usage provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface UsageProvider extends FlowCentralComponent {

    /**
     * Finds application usages by other applications.
     * 
     * @param applicationName
     *                        the application name
     * @param usageType
     *                        optional usage type
     * @return list of usages
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Usage> findApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException;

    /**
     * Count application usages by other applications.
     * 
     * @param applicationName
     *                        the application name
     * @param usageType
     *                        optional usage type
     * @return count of usages
     * @throws UnifyException
     *                        if an error occurs
     */
    long countApplicationUsagesByOtherApplications(String applicationName, UsageType usageType) throws UnifyException;

    /**
     * Finds entity usages.
     * 
     * @param entity
     *                  the entity long name
     * @param usageType
     *                  optional usage type
     * @return list of usages
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Usage> findEntityUsages(String entity, UsageType usageType) throws UnifyException;

    /**
     * Count entity usages.
     * 
     * @param entity
     *                  the entity name
     * @param usageType
     *                  optional usage type
     * @return count of usages
     * @throws UnifyException
     *                        if an error occurs
     */
    long countEntityUsages(String entity, UsageType usageType) throws UnifyException;
}
