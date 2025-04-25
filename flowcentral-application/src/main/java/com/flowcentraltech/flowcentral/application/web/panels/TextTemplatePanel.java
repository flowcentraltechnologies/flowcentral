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

import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequence;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Text template panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-texttemplatepanel")
@UplBinding("web/application/upl/texttemplatepanel.upl")
public class TextTemplatePanel extends AbstractApplicationPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        TextTemplate textTemplate = getTextTemplate();
        setWidgetVisible("errorMsg", !StringUtils.isBlank(textTemplate.getErrorMsg()));
    }

    @Action
    public void set() throws UnifyException {
        TextTemplate textTemplate = getTextTemplate();
        TokenSequence.Error error = textTemplate.set();
        if (error != null) {
            String errorMsg = resolveSessionMessage(error.getMsg(), error.getParams());
            textTemplate.setErrorMsg(errorMsg);
            setWidgetVisible("errorMsg", true);
        } else {
            removeCurrentPopup();
            setReloadOnSwitch();
            setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
            setWidgetVisible("errorMsg", false);
        }        
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
        Popup popup = getCurrentPopup();
        return popup != null ? (TextTemplate) popup.getBackingBean() : getValue(TextTemplate.class);
    }

}
