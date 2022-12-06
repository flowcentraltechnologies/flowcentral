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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ExtensionType;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Convenient abstract base class for popup window text widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractPopupWindowTextField extends AbstractPopupTextField {

    @Configurable
    private AppletUtilities au;

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    @Action
    public final void popup() throws UnifyException {
        Popup popup = preparePopup();
        commandShowPopup(popup);
    }

    @Override
    public ExtensionType getExtensionType() {
        return ExtensionType.EXTENDED;
    }

    @Override
    public boolean isPopupOnEditableOnly() {
        return false;
    }

    @Override
    public boolean isUseFacade() throws UnifyException {
        return true;
    }

    @Override
    public boolean isBindEventsToFacade() throws UnifyException {
        return false;
    }

    protected AppletUtilities au() {
        return au;
    }

    protected abstract Popup preparePopup() throws UnifyException;

}
