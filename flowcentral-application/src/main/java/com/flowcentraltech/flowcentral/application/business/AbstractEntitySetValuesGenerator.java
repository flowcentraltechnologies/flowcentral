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

package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.util.ProcessVariableUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient abstract base class for entity set value generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractEntitySetValuesGenerator extends AbstractFlowCentralComponent
        implements EntitySetValuesGenerator {

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final AppletUtilities au() {
        return appletUtilities;
    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final EnvironmentService environment() {
        return appletUtilities.environment();
    }

    protected final SystemModuleService system() {
        return appletUtilities.system();
    }

    /**
     * Gets the child value store.
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param valueStore
     *                        the parent value store
     * @param childFieldName
     *                        the child field name
     * @return the child record value store
     * @throws UnifyException
     *                        if an error occurs
     */
    @SuppressWarnings("unchecked")
    protected ValueStore getChildValueStore(EntityDef parentEntityDef, ValueStore valueStore, String childFieldName)
            throws UnifyException {
        EntityClassDef _childEntityClassDef = appletUtilities.application().getEntityClassDef(appletUtilities
                .application().getRefDef(parentEntityDef.getFieldDef(childFieldName).getReferences()).getEntity());
        Restriction _childRestriction = appletUtilities.application().getChildRestriction(parentEntityDef,
                childFieldName, (Entity) valueStore.getValueObject());
        Entity childEntity = environment().listLean(Query
                .ofDefaultingToAnd((Class<? extends Entity>) _childEntityClassDef.getEntityClass(), _childRestriction));
        return new BeanValueStore(childEntity);
    }

    /**
     * Gets the child value store.
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param valueStore
     *                        the parent value store
     * @param childFieldName
     *                        the child field name
     * @return the child record value store
     * @throws UnifyException
     *                        if an error occurs
     */
    @SuppressWarnings("unchecked")
    protected ValueStore getChildListValueStore(EntityDef parentEntityDef, ValueStore valueStore, String childFieldName)
            throws UnifyException {
        EntityClassDef _childEntityClassDef = appletUtilities.application().getEntityClassDef(appletUtilities
                .application().getRefDef(parentEntityDef.getFieldDef(childFieldName).getReferences()).getEntity());
        Restriction _childRestriction = appletUtilities.application().getChildRestriction(parentEntityDef,
                childFieldName, (Entity) valueStore.getValueObject());
        List<? extends Entity> childEntityList = environment().listAll(Query
                .ofDefaultingToAnd((Class<? extends Entity>) _childEntityClassDef.getEntityClass(), _childRestriction));
        return new BeanValueListStore(childEntityList);
    }
    
    /**
     * Gets a new open link.
     * 
     * @param openPath
     *                        the open path
     * @param entityId
     *                        the entity ID
     * @param validityMinutes
     *                        the validity minutes
     * @return the open link (HTML)
     * @throws UnifyException
     *                        if an error occurs
     */
    protected String getNewOpenLink(String openPath, Long entityId, int validityMinutes) throws UnifyException {
        return getNewOpenLink(null, openPath, entityId, validityMinutes);
    }

    /**
     * Gets a new open link.
     * 
     * @param title
     *                        the link title (optional)
     * @param openUrl
     *                        the open URL
     * @param entityId
     *                        the entity ID
     * @param validityMinutes
     *                        the validity minutes
     * @return the open link (HTML)
     * @throws UnifyException
     *                        if an error occurs
     */
    protected String getNewOpenLink(String title, String openUrl, Long entityId, int validityMinutes)
            throws UnifyException {
        return system().getNewOpenLink(title, openUrl, entityId, validityMinutes).getHtmlLink();
    }
   
    /**
     * Sets a process variable in supplied value store.
     * 
     * @param valueStore
     *                   the value store
     * @param name
     *                   the process variable name
     * @param val
     *                   the value to set the process variable to
     * @throws UnifyException
     *                        if an error occurs
     */
    protected void setProcessVariable(ValueStore valueStore, String name, Object val) throws UnifyException {
        valueStore.setTempValue(ProcessVariableUtils.getVariable(name), val);
    }

    /**
     * Sets a property value in supplied value store.
     * 
     * @param valueStore
     *                   the value store
     * @param property
     *                   the property name
     * @param val
     *                   the value to set the property to
     * @throws UnifyException
     *                        if an error occurs
     */
    protected void setProperty(ValueStore valueStore, String property, Object val) throws UnifyException {
        valueStore.store(property, val);
    }
}
