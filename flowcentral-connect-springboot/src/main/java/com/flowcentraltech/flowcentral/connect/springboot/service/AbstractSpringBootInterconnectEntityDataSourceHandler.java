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
package com.flowcentraltech.flowcentral.connect.springboot.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.flowcentraltech.flowcentral.connect.common.constants.DataSourceOperation;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.springboot.SpringBootInterconnect;

/**
 * Convenient abstract base class for Flowcentral spring boot interconnect data source entity handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSpringBootInterconnectEntityDataSourceHandler
        implements SpringBootInterconnectEntityDataSourceHandler {

    private final SpringBootInterconnect interconnect;

    private final Set<DataSourceOperation> supportsOnly;

    public AbstractSpringBootInterconnectEntityDataSourceHandler(SpringBootInterconnect interconnect,
            DataSourceOperation... supportsOnly) {
        this.interconnect = interconnect;
        this.supportsOnly = new HashSet<DataSourceOperation>(Arrays.asList(supportsOnly));
    }

    @Override
    public final boolean supports(DataSourceRequest req) {
        return supportsOnly.isEmpty() || supportsOnly.contains(req.getOperation());
    }

    @Override
    public final Object[] process(Class<?> implClass, DataSourceRequest req) throws Exception {
        if (!supports(req)) {
            throw new Exception("Operation " + req.getOperation() + " not supported by this handler");
        }

        return this.doProcess(implClass, req);
    }

    protected abstract Object[] doProcess(Class<?> implClass, DataSourceRequest req) throws Exception;

    protected SpringBootInterconnect getInterconnect() {
        return interconnect;
    }

}
