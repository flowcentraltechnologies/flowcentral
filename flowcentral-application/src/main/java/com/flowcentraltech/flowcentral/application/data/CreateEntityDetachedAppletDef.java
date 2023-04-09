/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;

/**
 * Create entity detached applet definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CreateEntityDetachedAppletDef extends AbstractDetachedAppletDef {

    public CreateEntityDetachedAppletDef(AppletDef parentAppletDef) {
        super(AppletType.CREATE_ENTITY, parentAppletDef);
    }

    @Override
    public String getOpenPath() {
        // This not an error
        return parentAppletDef.getDetachedOpenPath();
    }

    @Override
    public String getDetachedOpenPath() {
        // Neither is this
        return null;
    }

}
