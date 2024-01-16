/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractEntryTablePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.FixedRowActionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.web.ui.widget.Panel;

/**
 * Convenient abstract base class for inline CRUD table policies
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractInlineCRUDTablePolicy<T extends InlineCRUDEntry> extends AbstractEntryTablePolicy
        implements InlineCRUDTablePolicy<T> {

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    public FixedRowActionType resolveFixedIndex(ValueStoreReader parentReader, ValueStore valueStore, int index,
            int size) throws UnifyException {
        return FixedRowActionType.FIXED;
    }

    @Override
    public final int resolveActionIndex(ValueStoreReader parentReader, ValueStore valueStore, int index, int size)
            throws UnifyException {
        return 0;
    }

    protected void commandShowPopup(String panelName) throws UnifyException {
        appletUtilities.commandShowPopup(panelName);
    }

    protected void commandShowPopup(Panel panel) throws UnifyException {
        appletUtilities.commandShowPopup(panel);
    }

    protected void commandHidePopup() throws UnifyException {
        appletUtilities.commandHidePopup();
    }

    protected void commandRefreshPanels(String... panelLongName) throws UnifyException {
        appletUtilities.commandRefreshPanels(panelLongName);
    }

    protected void commandRefreshPanels(Panel... panels) throws UnifyException {
        appletUtilities.commandRefreshPanels(panels);
    }

    protected void commandRefreshPanelsAndHidePopup(String... panelLongName) throws UnifyException {
        appletUtilities.commandRefreshPanelsAndHidePopup(panelLongName);
    }

    protected void commandRefreshPanelsAndHidePopup(Panel... panels) throws UnifyException {
        appletUtilities.commandRefreshPanelsAndHidePopup(panels);
    }

    protected AppletUtilities au() {
        return appletUtilities;
    }

    protected ApplicationModuleService application() {
        return appletUtilities.application();
    }

}
