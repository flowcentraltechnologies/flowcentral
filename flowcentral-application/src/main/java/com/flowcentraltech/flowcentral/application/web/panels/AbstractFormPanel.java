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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.EventHandler;

/**
 * Convenient abstract base class for form panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@UplBinding("web/application/upl/formpanel.upl")
public abstract class AbstractFormPanel extends AbstractApplicationPanel implements FormPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        AbstractForm form = getValue(AbstractForm.class);
        setVisible("sectorIcon", form.isWithSectorIcon());        
        setWidgetVisible("formErrors", form.isWithValidationErrors());
    }

    @Override
    public List<FormValidationErrors> validate(FormValidationContext ctx) throws UnifyException {
        return Collections.emptyList();
    }

    public EventHandler[] getSwitchOnChangeEventHandler() throws UnifyException {
        return getWidgetByShortName("switchOnChangeLabel").getEventHandlers();
    }
}
