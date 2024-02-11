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

package com.flowcentraltech.flowcentral.application.util;

import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;

/**
 * Application collaboration utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationCollaborationUtils {

    private static Map<Class<? extends BaseApplicationEntity>, CollaborationType> collaborationEntityTypeMapping;

    private static Map<CollaborationType, Class<? extends BaseApplicationEntity>> entityTypeCollaborationMapping;

    static {
        collaborationEntityTypeMapping = new HashMap<Class<? extends BaseApplicationEntity>, CollaborationType>();
        collaborationEntityTypeMapping.put(AppWidgetType.class, CollaborationType.WIDGET);
        collaborationEntityTypeMapping.put(AppEntity.class, CollaborationType.ENTITY);
        collaborationEntityTypeMapping.put(AppRef.class, CollaborationType.REFERENCE);
        collaborationEntityTypeMapping.put(AppApplet.class, CollaborationType.APPLET);
        collaborationEntityTypeMapping.put(AppForm.class, CollaborationType.FORM);
        collaborationEntityTypeMapping.put(AppTable.class, CollaborationType.TABLE);
        
        
        entityTypeCollaborationMapping = new HashMap<CollaborationType, Class<? extends BaseApplicationEntity>>();
        entityTypeCollaborationMapping.put(CollaborationType.WIDGET, AppWidgetType.class);
        entityTypeCollaborationMapping.put(CollaborationType.ENTITY, AppEntity.class);
        entityTypeCollaborationMapping.put(CollaborationType.REFERENCE, AppRef.class);
        entityTypeCollaborationMapping.put(CollaborationType.APPLET, AppApplet.class);
        entityTypeCollaborationMapping.put(CollaborationType.FORM, AppForm.class);
        entityTypeCollaborationMapping.put(CollaborationType.TABLE, AppTable.class);        
    }

    private ApplicationCollaborationUtils() {

    }

    public static CollaborationType getCollaborationType(
            Class<? extends BaseApplicationEntity> applicationEntityClass) {
        return collaborationEntityTypeMapping.get(applicationEntityClass);
    }

    public static Class<? extends BaseApplicationEntity> getEntityClass(CollaborationType collaborationType) {
        return entityTypeCollaborationMapping.get(collaborationType);
    }

    public static void registerCollaborationType(Class<? extends BaseApplicationEntity> appEntityType,
            CollaborationType type) {
        collaborationEntityTypeMapping.put(appEntityType, type);
        entityTypeCollaborationMapping.put(type, appEntityType);
    }
}
