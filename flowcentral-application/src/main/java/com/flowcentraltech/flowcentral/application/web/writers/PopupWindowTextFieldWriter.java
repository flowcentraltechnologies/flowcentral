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
package com.flowcentraltech.flowcentral.application.web.writers;

import com.flowcentraltech.flowcentral.application.web.widgets.AbstractPopupWindowTextField;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;
import com.tcdng.unify.web.ui.widget.writer.control.AbstractPopupTextFieldWriter;

/**
 * Popup window text field writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(AbstractPopupWindowTextField.class)
@Component("popupwindowtextfield-writer")
public class PopupWindowTextFieldWriter extends AbstractPopupTextFieldWriter {

    @Override
    protected final void writeOpenPopupJS(ResponseWriter writer, Widget widget, String event, String pageName,
            String cmdTag, String frameId, String popupId, long stayOpenForMillSec, String onShowAction,
            String onShowParamObject, String onHideAction, String onHideParamObject) throws UnifyException {
        AbstractPopupWindowTextField popupWindowTextField = (AbstractPopupWindowTextField) widget;
        writer.beginFunction("fux.rigPopupWinText");
        writer.writeParam("pId", popupWindowTextField.getId());
        writer.writeParam("pTrgId", pageName);
        writer.writeParam("pEvt", event.substring(2));
        writer.writeCommandURLParam("pCmdURL");
        writer.endFunction();
    }

    @Override
    protected void writePopupContent(ResponseWriter writer, AbstractPopupTextField popupTextField)
            throws UnifyException {

    }

    @Override
    protected void writeBaseAddOn(ResponseWriter writer, Widget widget) throws UnifyException {

    }

    @Override
    protected void doWritePopupTextFieldBehaviour(ResponseWriter writer, AbstractPopupTextField popupTextField,
            boolean popupEnabled) throws UnifyException {

    }

    @Override
    protected String getOnHideAction() throws UnifyException {
        return null;
    }

    @Override
    protected String getOnHideParam(AbstractPopupTextField popupTextField) throws UnifyException {
        return null;
    }

    @Override
    protected String getOnShowAction() throws UnifyException {
        return null;
    }

    @Override
    protected String getOnShowParam(AbstractPopupTextField popupTextField) throws UnifyException {
        return null;
    }

}
