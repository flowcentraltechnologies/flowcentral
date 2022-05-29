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

package com.flowcentraltech.flowcentral.application.web.data;

import java.util.EnumMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractApplet;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.configuration.constants.EntityChildCategoryType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Applet context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletContext extends AbstractContext {

    private final AppletUtilities au;
    
    private final AbstractApplet applet;
    
    private Map<EntityChildCategoryType, String> entityReferences;

    private boolean readOnly;
    
    private boolean inWorkflow;
    
    private boolean review;
    
    public AppletContext(AbstractApplet applet, AppletUtilities au) {
        this.applet = applet;
        this.au = au;
        this.entityReferences = new EnumMap<EntityChildCategoryType, String>(EntityChildCategoryType.class);
        for (EntityChildCategoryType type : EntityChildCategoryType.values()) {
            this.entityReferences.put(type, null);
        }
    }

    public AbstractApplet getApplet() {
        return applet;
    }

    public AppletUtilities getAu() {
        return au;
    }

    public EnvironmentService getEnvironment() {
        return au.getEnvironment();
    }

    public SpecialParamProvider getSpecialParamProvider() throws UnifyException {
        return au.getSpecialParamProvider();
    }

    public void extractReference(EntityDef entityDef, Object inst) throws UnifyException {
        if (inst instanceof Entity) {
            for (EntityChildCategoryType type : entityReferences.keySet()) {
                if (type.readField() != null && type.readEntity().equals(entityDef.getLongName())) {
                    String ref = DataUtils.getBeanProperty(String.class, inst, type.readField());
                    if (inst instanceof BaseApplicationEntity) {
                        String applicationName = ((BaseApplicationEntity) inst).getApplicationName();
                        ref = ApplicationNameUtils.ensureLongNameReference(applicationName, ref);
                    }

                    entityReferences.put(type, ref);
                }
            }
        }
    }

    public String getReference(EntityChildCategoryType type) {
        String ref = entityReferences.get(type);
        return ref != null ? ref : type.readEntity();
    }
    
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }
    
    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public boolean isInWorkflow() {
        return inWorkflow;
    }

    public void setInWorkflow(boolean inWorkflow) {
        this.inWorkflow = inWorkflow;
    }

    public boolean isContextEditable() {
        return !readOnly && (review || !inWorkflow);
    }
}
