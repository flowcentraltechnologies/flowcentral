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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Create entity single form applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-createentitysingleformappletpanel")
@UplBinding("web/application/upl/createentitysingleformappletpanel.upl")
public class CreateEntitySingleFormAppletPanel extends AbstractEntitySingleFormAppletPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        CreateEntitySingleFormApplet applet = getCreateEntityApplet();
        applet.ensureRootAppletStruct();

        final AbstractEntitySingleFormApplet.ViewMode viewMode = applet.getMode();

        switch (viewMode) {
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
            case MAINTAIN_FORM:
            case NEW_FORM:
            case NEW_PRIMARY_FORM:
                break;
            case SEARCH:
            default:
                break;
        }
    }

    protected CreateEntitySingleFormApplet getCreateEntityApplet() throws UnifyException {
        return getValue(CreateEntitySingleFormApplet.class);
    }
}
