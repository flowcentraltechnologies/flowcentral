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

import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Convenient abstract base class for applet page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAppletPageBean<T extends AbstractApplet> extends AbstractPageBean {

    private T applet;

    public T getApplet() {
        return applet;
    }

    public void setApplet(T applet) throws UnifyException{
        this.applet = applet;
    }

    public String getAltCaption() throws UnifyException {
        return applet.getPageAltCaption();
    }

    public String getAltSubCaption() throws UnifyException {
        return applet.getPageAltSubCaption();
    }

}
