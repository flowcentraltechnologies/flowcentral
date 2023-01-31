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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractApplicationSwitchPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Panel;

/**
 * Convenient abstract base panel for applet panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAppletPanel extends AbstractApplicationSwitchPanel {

    protected void addPanelToPushComponents(String panelName, boolean editable) throws UnifyException {
        if (editable && getApplet().isSaveHeaderFormOnTabAction()) {
            Panel formPanel = getWidgetByShortName(Panel.class, panelName);
            getRequestContextUtil().addListItem(AppletRequestAttributeConstants.MAINFORM_PUSH_COMPONENTS,
                    formPanel.getId());
        }
    }

    protected AbstractApplet getApplet() throws UnifyException {
        return getValue(AbstractApplet.class);
    }
}
