/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import java.util.Arrays;
import java.util.Collections;

import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Quick form edit panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-quickformeditpanel")
@UplBinding("web/application/upl/quickformeditpanel.upl")
public class QuickFormEditPanel extends AbstractApplicationPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        QuickFormEdit quickFormEdit = getValue(QuickFormEdit.class);
        FormContext formContext = quickFormEdit.getFormContext();
        if (!formContext.isQuickEditMode()) {
            EventHandler[] handlers = getWidgetByShortName(Widget.class, "switchOnChangeHolder")
                    .getEventHandlers();
            formContext.setQuickEditMode(
                    handlers != null ? Collections.unmodifiableList(Arrays.asList(handlers)) : Collections.emptyList());
        }
    }

    @Action
    public void formSwitchOnChange() throws UnifyException {
        QuickFormEdit quickFormEdit = getValue(QuickFormEdit.class);
        quickFormEdit.formSwitchOnChange();
    }

    @Action
    public void saveForm() throws UnifyException {
        QuickFormEdit quickFormEdit = getValue(QuickFormEdit.class);
        if (quickFormEdit.commit()) {
            removeCurrentPopup();
            hintUser("$m{quickedit.hint.success}");
            setReloadOnSwitch();
            setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
            return;
        }

        hintUser(MODE.ERROR, "$m{quickedit.hint.failure}");
    }

    @Action
    public void close() throws UnifyException {
        commandHidePopup();
    }
}
