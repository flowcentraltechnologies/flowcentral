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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageLoadingDetailsApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;

/**
 * Convenient abstract base class for loading details page beans.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractLoadingDetailsPageBean extends AbstractAppletPageBean<ManageLoadingDetailsApplet> {

    private Long parentId;

    public LoadingTable getResultTable() {
        return getApplet() != null ? getApplet().getResultTable() : null;
    }

    public EntityCRUD getChildEntityCrud() {
        return getApplet() != null ? getApplet().getChildEntityCrud() : null;
    }
   
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public abstract String getViewActionCaption();
    
    public abstract boolean isViewActionMode();
    
}
