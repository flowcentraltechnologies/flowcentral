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

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractFormPanel;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormWidget;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Studio header with tabs form panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-studioheaderwithtabsformpanel")
@UplBinding("web/studio/upl/studioheaderwithtabsformpanel.upl")
public class StudioHeaderWithTabsFormPanel extends AbstractFormPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        final HeaderWithTabsForm headerWithTabsForm = getValue(HeaderWithTabsForm.class);
        final FormContext formContext = headerWithTabsForm.getCtx();
        boolean evaluate = true;
        
        // Do a form update on reload on switch. Usually triggered by select popups.
        if (clearReloadOnSwitch()) {
            formContext.au().updateHeaderWithTabsForm(headerWithTabsForm, (Entity) headerWithTabsForm.getFormBean());
            evaluate = false;
        }

        if (evaluate) {
            MiniFormWidget widget = getWidgetByShortName(MiniFormWidget.class, "headerMiniForm");
            formContext.setTriggerEvaluator(widget);
            formContext.evaluateTabStates();
        }

        setWidgetVisible("formAnnotation", headerWithTabsForm.isWithVisibleAnnotations());
        setVisible("formTabSheetPanel", headerWithTabsForm.isTabSheetInStateForDisplay());
    }

}
