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

/**
 * Quick form edit panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-quickformeditpanel")
@UplBinding("web/application/upl/quickformeditpanel.upl")
public class QuickFormEditPanel extends AbstractPanel {

    @Action
    public void saveForm() throws UnifyException {
        QuickFormEdit quickFormEdit = getValue(QuickFormEdit.class);
        if (quickFormEdit.commit()) {
            removeCurrentPopup();
            hintUser(resolveSessionMessage("$m{quickedit.hint.success}"));
            setRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH, Boolean.TRUE);
            setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
            return;
        } 
        
        hintUser(resolveSessionMessage("$m{quickedit.hint.failure}"));
    }

    @Action
    public void close() throws UnifyException {
        commandHidePopup();
    }
}
