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
package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.data.EntitySchema;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Entity schema manager component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntitySchemaManager extends FlowCentralComponent {

    /**
     * Create entity schema.
     * 
     * @param entitySchema
     *                     the entity schema
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean createEntitySchema(EntitySchema entitySchema) throws UnifyException;

    /**
     * Update entity schema.
     * 
     * @param entitySchema
     *                     the entity schema
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean updateEntitySchema(EntitySchema entitySchema) throws UnifyException;

    /**
     * Creates default entity components.
     * 
     * @param entity
     *                  the entity name
     * @param appEntity
     *                  the entity
     * @throws UnifyException
     *                        if an error occurs
     */
    void createDefaultComponents(String entity, AppEntity appEntity) throws UnifyException;

    /**
     * Updates default entity components.
     * 
     * @param entity
     *                  the entity name
     * @param appEntity
     *                  the entity
     * @throws UnifyException
     *                        if an error occurs
     */
    void updateDefaultComponents(String entity, AppEntity appEntity) throws UnifyException;
    
    /**
     * Creates an applet component.
     * 
     * @param applicationName
     *                        the application name
     * @param appApplet
     *                        the applet
     * @param child
     *                        child applet indication
     * @return the applet name
     * @throws UnifyException
     *                        if an error occurs
     */
    String createDefaultAppletComponents(String applicationName, AppApplet appApplet, boolean child)
            throws UnifyException;
}
