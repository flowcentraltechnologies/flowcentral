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
package com.flowcentraltech.flowcentral.application.policies;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EditEntityItem;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;

/**
 * Convenient abstract base class for application table loading providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplicationLoadingTableProvider extends AbstractUnifyComponent
    implements LoadingTableProvider {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private EnvironmentService environmentService;

    private final String sourceEntity;
    
    public AbstractApplicationLoadingTableProvider(String sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    @Override
    public int countLoadingItems(Restriction restriction) throws UnifyException {
        return 0;
    }

    @Override
    public LoadingWorkItemInfo getLoadingWorkItemInfo(WorkEntity inst) throws UnifyException {
        return new LoadingWorkItemInfo();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public EntityItem getSourceItem(Long sourceItemId) throws UnifyException {
        EntityClassDef sourceEntityClassDef = application().getEntityClassDef(sourceEntity);
        Entity entity = environment().list((Class<? extends Entity>) sourceEntityClassDef.getEntityClass(),
                sourceItemId);
        return new EditEntityItem(entity);
    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected ApplicationModuleService application() {
        return applicationModuleService;
    }

    protected String getSourceEntity() {
        return sourceEntity;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
