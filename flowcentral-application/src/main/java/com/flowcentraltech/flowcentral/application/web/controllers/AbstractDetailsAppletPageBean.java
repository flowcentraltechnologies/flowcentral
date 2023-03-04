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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractDetailsApplet;
import com.tcdng.unify.core.UnifyException;

/**
 * Abstract base class for details applet page beans.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsAppletPageBean<T extends AbstractDetailsApplet> extends AbstractAppletPageBean<T> {

    private Long parentId;

    public T getApplet() {
        return super.getApplet();
    }

    @Override
    public void setApplet(T applet) throws UnifyException {
        super.setApplet(applet);
    }

    public boolean isSupportChild() {
        return parentId != null && super.getApplet().isSupportChild();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public abstract boolean isChildAppletInlineMode();

}
