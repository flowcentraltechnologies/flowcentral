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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for menu widgets.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractMenuWidget extends AbstractFlowCentralPanel {

    public static final String WORK_MENUIDS = "menuIds";

    public static final String WORK_MENUITEMS = "menuItems";

    private boolean collapsedInitial;
    
    public AbstractMenuWidget() {
        this.collapsedInitial = true;
    }
    
    public boolean isCollapsedInitial() throws UnifyException {
        return collapsedInitial;
    }

    public void setCollapsedInitial(boolean collapsedInitial) {
        this.collapsedInitial = collapsedInitial;
    }
    
    public boolean isHorizontal() throws UnifyException {
        return false;
    }

    public String getMenuSectionId() throws UnifyException {
        return "";
    }

    public String getNavLeftId() throws UnifyException {
        return "";
    }

    public String getNavRightId() throws UnifyException {
        return "";
    }

    public String getSlideId() throws UnifyException {
        return "";
    }
}
