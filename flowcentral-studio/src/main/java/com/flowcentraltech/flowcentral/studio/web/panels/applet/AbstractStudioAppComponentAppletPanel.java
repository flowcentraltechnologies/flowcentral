/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormAppletPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Abstract base class for studio application component applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@UplBinding("web/studio/upl/studioappcomponentappletpanel.upl")
public abstract class AbstractStudioAppComponentAppletPanel extends AbstractEntityFormAppletPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final AbstractStudioAppComponentApplet applet = getValue(AbstractStudioAppComponentApplet.class);
        final boolean isRootForm = applet.isRootForm();
        if (isRootForm) {
            setVisible("cancelBtn", false);
        }
        
        //setVisible("formPanel.headerLeftPanel", false);
        setVisible("formPanel.headerCenterPanel", false);
    }

}
