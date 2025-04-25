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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.APIType;
import com.tcdng.unify.core.UnifyException;

/**
 * API definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class APIDef extends BaseApplicationEntityDef {

    private APIType type;

    private String entity;

    private String applet;

    private boolean supportCreate;

    private boolean supportRead;

    private boolean supportUpdate;

    private boolean supportDelete;

    public APIDef(APIType type, String entity, String applet, boolean supportCreate, boolean supportRead,
            boolean supportUpdate, boolean supportDelete, String longName, String description, Long id, long version)
            throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        this.type = type;
        this.entity = entity;
        this.applet = applet;
        this.supportCreate = supportCreate;
        this.supportRead = supportRead;
        this.supportUpdate = supportUpdate;
        this.supportDelete = supportDelete;
    }

    public APIType getType() {
        return type;
    }

    public String getEntity() {
        return entity;
    }

    public String getApplet() {
        return applet;
    }

    public boolean isSupportCreate() {
        return supportCreate;
    }

    public boolean isSupportRead() {
        return supportRead;
    }

    public boolean isSupportUpdate() {
        return supportUpdate;
    }

    public boolean isSupportDelete() {
        return supportDelete;
    }

}
