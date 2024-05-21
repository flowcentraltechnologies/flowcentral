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

import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Edit property list panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-editpropertylistpanel")
@UplBinding("web/application/upl/editpropertylistpanel.upl")
public class EditPropertyListPanel extends AbstractFlowCentralPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        EditPropertyList editPropertyList = getValue(EditPropertyList.class);
        setVisible("sectorIcon", editPropertyList.isWithSectorIcon());
        
        editPropertyList.clearDisplayItem();
        AppletContext appCtx = editPropertyList.getCtx();
        if (appCtx.isInWorkflow() && !appCtx.isReview()) {
            editPropertyList.setDisplayItemCounterClass("fc-dispcounterorange");
            editPropertyList.setDisplayItemCounter(
                    resolveSessionMessage("$m{editpropertylist.parentinworkflow.viewonly}"));
        } else {
            editPropertyList.setDisplayItemCounter(
                    resolveSessionMessage("$m{assignmentpage.viewing}"));
        }
    }

    @Action
    public void saveProperties() throws UnifyException {
        EditPropertyList editPropertyList = getValue(EditPropertyList.class);
        editPropertyList.commitPropertyList();
        hintUser("$m{entityformapplet.editpropertylist.success.hint}");
    }
}
