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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.tcdng.unify.core.UnifyException;

/**
 * Abstract base class for entity form applet page beans.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityFormAppletPageBean<T extends AbstractEntityFormApplet>
        extends AbstractAppletPageBean<T> {

    private boolean oldPage;

    public T getApplet() {
        return super.getApplet();
    }

    @Override
    public void setApplet(T applet) throws UnifyException {
        super.setApplet(applet);
    }

    public boolean isOldPage() {
        return oldPage;
    }

    public void setOldPage(boolean oldPage) {
        this.oldPage = oldPage;
    }

    public boolean isReloadOnSwitch() throws UnifyException {
        return oldPage && ((AbstractEntityFormApplet) getApplet()).getRootAppletDef().getPropValue(boolean.class,
                AppletPropertyConstants.SEARCH_TABLE_VIEW_ITEM_SEPARATE_TAB);
    }
}
