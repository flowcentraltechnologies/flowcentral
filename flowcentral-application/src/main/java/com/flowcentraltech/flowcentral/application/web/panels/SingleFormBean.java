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

package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Single form bean for single for panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SingleFormBean {

    /**
     * Initialize the single form bean.
     * 
     * @param au handle to applet utilities component.
     * 
     * @throws UnifyException if an error occurs
     */
    void init(AppletUtilities au) throws UnifyException;
    
    /**
     * Loads single form bean using supplied entity instance properties.
     * 
     * @param inst
     *             the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void load(Entity inst) throws UnifyException;

    /**
     * Unloads single form bean to supplied entity instance properties.
     * 
     * @param inst
     *             the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void unload(Entity inst) throws UnifyException;
}
