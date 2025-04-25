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

package com.flowcentraltech.flowcentral.delegate.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AbstractEnvironmentDelegate;
import com.flowcentraltech.flowcentral.application.business.EntitySchemaManager;
import com.flowcentraltech.flowcentral.application.data.EntityFieldSchema;
import com.flowcentraltech.flowcentral.application.data.EntitySchema;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.common.business.SynchronizableEnvironmentDelegate;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.DelegateEntityListingDTO;
import com.tcdng.unify.common.data.EntityDTO;
import com.tcdng.unify.common.data.EntityFieldDTO;
import com.tcdng.unify.common.data.EntityListingDTO;
import com.tcdng.unify.common.data.RedirectErrorDTO;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for environment delegates.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSynchronizableEnvironmentDelegate extends AbstractEnvironmentDelegate
        implements SynchronizableEnvironmentDelegate {

    @Configurable
    private EntitySchemaManager entitySchemaManager;

    @Override
    public void delegateCreateSynchronization(TaskMonitor taskMonitor) throws UnifyException {
        logInfo(taskMonitor, "Performing create synchronization of entities for delegate [0]...", getName());
        DelegateEntityListingDTO delegateEntityListingDTO = getDelegatedEntityList();
        if (delegateEntityListingDTO != null) {
            if (!DataUtils.isBlank(delegateEntityListingDTO.getRedirectErrors())) {
                for (RedirectErrorDTO redirectErrorDTO : delegateEntityListingDTO.getRedirectErrors()) {
                    logWarn(taskMonitor, "Unable to synchronize with redirect [{0}]. Error [{1}].",
                            redirectErrorDTO.getRedirect(), redirectErrorDTO.getErrorMsg());
                }
            }

            if (!DataUtils.isBlank(delegateEntityListingDTO.getListings())) {
                final String delegate = getName();
                logInfo(taskMonitor, "[{0}] entities detected...", delegateEntityListingDTO.getListings().size());
                final Set<String> existing = new HashSet<String>(
                        au.getApplicationEntitiesLongNames(new AppEntityQuery().ignoreEmptyCriteria(true)));
                for (EntityListingDTO entityListingDTO : delegateEntityListingDTO.getListings()) {
                    final String entity = entityListingDTO.getEntity();
                    if (!existing.contains(entity)) {
                        logInfo(taskMonitor, "Fetching schema information for [{0}]...", entity);
                        EntityDTO entityDTO = getDelegatedEntitySchema(entity);
                        if (entityDTO != null) {
                            if (!entityDTO.isIgnoreOnSync()) {
                                logInfo(taskMonitor, "Creating entity schema...");
                                List<EntityFieldSchema> fields = new ArrayList<EntityFieldSchema>();
                                for (EntityFieldDTO entityFieldDTO : entityDTO.getFields()) {
                                    EntityFieldDataType dataType = EntityFieldDataType
                                            .fromInterconnect(entityFieldDTO.getType());
                                    fields.add(new EntityFieldSchema(dataType, entityFieldDTO.getName(),
                                            entityFieldDTO.getDescription(), entityFieldDTO.getColumn(),
                                            entityFieldDTO.getReferences(), entityFieldDTO.getScale(),
                                            entityFieldDTO.getPrecision(), entityFieldDTO.getLength(),
                                            entityFieldDTO.isNullable()));
                                }

                                EntityBaseType baseType = EntityBaseType.fromInterconnect(entityDTO.getBaseType());
                                EntitySchema entitySchema = new EntitySchema(baseType, delegate,
                                        entityDTO.getDataSourceAlias(), entityDTO.getImplClass(), entity,
                                        entityDTO.getName(), entityDTO.getDescription(), entityDTO.getTableName(),
                                        entityDTO.isDynamic(), entityDTO.isActionPolicy(), fields);
                                entitySchemaManager.createEntitySchema(entitySchema);
                                logInfo(taskMonitor, "Entity schema create for [{0}] completed.", entity);
                            } else {
                                logInfo(taskMonitor, "Entity schema creation for [{0}] ignored.", entity);
                            }
                        } else {
                            logWarn(taskMonitor, "Could no retreive schema information for entity [{0}].", entity);
                        }
                    } else {
                        logInfo(taskMonitor, "Skipping entity schema create for [{0}]. Already exists.", entity);
                    }
                }
            } else {
                logInfo(taskMonitor, "No entity listings found for this delegate.");
            }

            logInfo(taskMonitor, "Delegate synchronization completed.");
        } else {
            logInfo(taskMonitor, "Could not retrieve synchronization information.");
        }
    }

    @Override
    public void delegateUpdateSynchronization(TaskMonitor taskMonitor) throws UnifyException {
        logInfo(taskMonitor, "Performing update synchronization of entities for delegate [0]...", getName());
        DelegateEntityListingDTO delegateEntityListingDTO = getDelegatedEntityList();
        if (delegateEntityListingDTO != null) {
            if (!DataUtils.isBlank(delegateEntityListingDTO.getRedirectErrors())) {
                for (RedirectErrorDTO redirectErrorDTO : delegateEntityListingDTO.getRedirectErrors()) {
                    logWarn(taskMonitor, "Unable to synchronize with redirect [{0}]. Error [{1}].",
                            redirectErrorDTO.getRedirect(), redirectErrorDTO.getErrorMsg());
                }
            }

            if (!DataUtils.isBlank(delegateEntityListingDTO.getListings())) {
                final String delegate = getName();
                logInfo(taskMonitor, "[{0}] entities detected...", delegateEntityListingDTO.getListings().size());
                final Set<String> existing = new HashSet<String>(
                        au.getApplicationEntitiesLongNames(new AppEntityQuery().ignoreEmptyCriteria(true)));
                for (EntityListingDTO entityListingDTO : delegateEntityListingDTO.getListings()) {
                    final String entity = entityListingDTO.getEntity();
                    if (existing.contains(entity)) {
                        logInfo(taskMonitor, "Fetching schema information for [{0}]...", entity);
                        EntityDTO entityDTO = getDelegatedEntitySchema(entity);
                        if (entityDTO != null) {
                            if (!entityDTO.isIgnoreOnSync()) {
                                logInfo(taskMonitor, "Updating entity schema...");
                                List<EntityFieldSchema> fields = new ArrayList<EntityFieldSchema>();
                                for (EntityFieldDTO entityFieldDTO : entityDTO.getFields()) {
                                    EntityFieldDataType dataType = EntityFieldDataType
                                            .fromInterconnect(entityFieldDTO.getType());
                                    fields.add(new EntityFieldSchema(dataType, entityFieldDTO.getName(),
                                            entityFieldDTO.getDescription(), entityFieldDTO.getColumn(),
                                            entityFieldDTO.getReferences(), entityFieldDTO.getScale(),
                                            entityFieldDTO.getPrecision(), entityFieldDTO.getLength(),
                                            entityFieldDTO.isNullable()));
                                }

                                EntityBaseType baseType = EntityBaseType.fromInterconnect(entityDTO.getBaseType());
                                EntitySchema entitySchema = new EntitySchema(baseType, delegate,
                                        entityDTO.getDataSourceAlias(), entityDTO.getImplClass(), entity,
                                        entityDTO.getName(), entityDTO.getDescription(), entityDTO.getTableName(),
                                        entityDTO.isDynamic(), entityDTO.isActionPolicy(), fields);
                                entitySchemaManager.updateEntitySchema(entitySchema);
                                logInfo(taskMonitor, "Entity schema update for [{0}] completed.", entity);
                            } else {
                                logInfo(taskMonitor, "Entity schema update for [{0}] ignored.", entity);
                            }
                        } else {
                            logWarn(taskMonitor, "Could no retreive schema information for entity [{0}].", entity);
                        }
                    } else {
                        logInfo(taskMonitor, "Skipping entity schema update for [{0}]. Does not exist.", entity);
                    }
                }
            } else {
                logInfo(taskMonitor, "No entity listings found for this delegate.");
            }

            logInfo(taskMonitor, "Delegate synchronization completed.");
        } else {
            logInfo(taskMonitor, "Could not retrieve synchronization information.");
        }
    }

}
