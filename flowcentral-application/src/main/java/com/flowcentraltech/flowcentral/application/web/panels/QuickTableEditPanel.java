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

import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractPanel;

/**
 * Quick table edit panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-quicktableeditpanel")
@UplBinding("web/application/upl/quicktableeditpanel.upl")
public class QuickTableEditPanel extends AbstractPanel {

    @Action
    public void saveEntries() throws UnifyException {
        QuickTableEdit quickTableEdit = getValue(QuickTableEdit.class);
        if (quickTableEdit.commitEntryList()); {
            removeCurrentPopup();
            setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
        }
    }

    @Action
    public void close() throws UnifyException {
        commandHidePopup();
    }
}
