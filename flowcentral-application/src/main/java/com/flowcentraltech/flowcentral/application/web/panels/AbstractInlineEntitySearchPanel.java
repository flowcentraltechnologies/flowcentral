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

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralStandalonePanel;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for in-line entity search panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractInlineEntitySearchPanel extends AbstractFlowCentralStandalonePanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setVisible("entitySearchPanel.reportBtn", false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntitySearch entitySearch = getValue(EntitySearch.class);
        String editActionKey = getEditActionKey();
        List<String> pushFormIds = (List<String>) getRequestAttribute(List.class,
                AppletRequestAttributeConstants.MAINFORM_PUSH_COMPONENTS);
        entitySearch.setPushFormIds(pushFormIds);
        entitySearch.setEditActionKey(editActionKey);
    }

    protected abstract String getEditActionKey();
}
