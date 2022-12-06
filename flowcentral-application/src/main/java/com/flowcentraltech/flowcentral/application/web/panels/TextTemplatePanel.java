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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractPanel;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Text template panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-texttemplatepanel")
@UplBinding("web/application/upl/texttemplatepanel.upl")
public class TextTemplatePanel extends AbstractPanel {

    @Action
    public void set() throws UnifyException {
        Popup popup = getCurrentPopup();
        TextTemplate textTemplate = (TextTemplate) popup.getBackingBean();
        textTemplate.set();
        removeCurrentPopup();
        setRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH, Boolean.TRUE);
        setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
    }

    @Action
    public void clear() throws UnifyException {
        TextTemplate textTemplate = getTextTemplate();
        textTemplate.clear();
    }

    @Action
    public void close() throws UnifyException {
        commandHidePopup();
    }

    private TextTemplate getTextTemplate() throws UnifyException {
        return getValue(TextTemplate.class);
    }

}
