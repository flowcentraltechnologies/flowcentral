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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.web.panels.EntityTreeSelect;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Entity tree select generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntityTreeSelectGenerator extends UnifyComponent {

    /**
     * Generates an entity tree select.
     * 
     * @param au
     *                       the applet utility component
     * @param formValueStore
     *                       the form value store
     * @return the entity tree select
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityTreeSelect generate(AppletUtilities au, ValueStore formValueStore) throws UnifyException;
}
