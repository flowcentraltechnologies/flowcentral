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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.Collections;

import com.flowcentraltech.flowcentral.application.business.AbstractMappedEntityProvider;
import com.flowcentraltech.flowcentral.organization.entities.MappedDepartment;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Default mapped department provider
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("default-mappeddepartmentprovider")
public class DefaultMappedDepartmentProvider
        extends AbstractMappedEntityProvider<MappedDepartment, DefaultMappedDepartmentProviderContext> {

    public DefaultMappedDepartmentProvider() {
        super(MappedDepartment.class, DefaultMappedDepartmentProviderContext.class, "organization.department",
                Collections.emptyMap());
    }

    @Override
    protected MappedDepartment doCreate(DefaultMappedDepartmentProviderContext context, Entity inst)
            throws UnifyException {
        MappedDepartment mappedDepartment = new MappedDepartment();
        new BeanValueStore(mappedDepartment).copy(new BeanValueStore(inst));
        return mappedDepartment;
    }

}
