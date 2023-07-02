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

import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Entry table page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entrytablepagepanel")
@UplBinding("web/application/upl/entrytablepagepanel.upl")
public class EntryTablePagePanel extends AbstractFlowCentralPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        EntryTablePage entryTablePage = getValue(EntryTablePage.class);
        setVisible("sectorIcon", entryTablePage.isWithSectorIcon());
        
        entryTablePage.clearDisplayItem();
        AppletContext appCtx = entryTablePage.getCtx();
        if (appCtx.isInWorkflow() && !appCtx.isReview()) {
            entryTablePage.setDisplayItemCounterClass("fc-dispcounterorange");
            entryTablePage.setDisplayItemCounter(
                    resolveSessionMessage("$m{entrytablepage.parentinworkflow.viewonly}"));
            entryTablePage.setDisabled(true);
        } else {
            entryTablePage.setDisplayItemCounter(
                    resolveSessionMessage("$m{entrytablepage.viewing}"));
            entryTablePage.setDisabled(false);
        }
        
        setVisible("entryPanelCaption", entryTablePage.getEntryCaption() != null);
    }

    @Action
    public void saveEntry() throws UnifyException {
        EntryTablePage entryTablePage = getValue(EntryTablePage.class);
        entryTablePage.commitEntryList(true);
        if (entryTablePage.isWithValidationErrors()) {
            hintUser(MODE.ERROR, "$m{entityformapplet.entrytable.errors.hint}", entryTablePage.getSubTitle());
        } else {
            hintUser("$m{entityformapplet.entrytable.success.hint}", entryTablePage.getSubTitle());
        }
    }
}
