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
import com.flowcentraltech.flowcentral.application.web.widgets.AssignmentPage;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Assignment page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-assignmentpagepanel")
@UplBinding("web/application/upl/assignmentpagepanel.upl")
public class AssignmentPagePanel extends AbstractFlowCentralPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        AssignmentPage assignmentPage = getValue(AssignmentPage.class);
        setVisible("sectorIcon", assignmentPage.isWithSectorIcon());
        
        assignmentPage.clearDisplayItem();
        AppletContext appCtx = assignmentPage.getCtx();
        if (appCtx.isInWorkflow() && !appCtx.isReview()) {
            assignmentPage.setDisplayItemCounterClass("fc-dispcounterorange");
            assignmentPage.setDisplayItemCounter(
                    resolveSessionMessage("$m{assignmentpage.parentinworkflow.viewonly}"));
            assignmentPage.setDisabled(true);
        } else {
            assignmentPage.setDisplayItemCounter(
                    resolveSessionMessage("$m{assignmentpage.viewing}"));
            assignmentPage.setDisabled(false);
       }
    }

    @Action
    public void saveAssignment() throws UnifyException {
        AssignmentPage assignmentPage = getValue(AssignmentPage.class);
        assignmentPage.commitAssignedList(true);
        hintUser("$m{entityformapplet.assignment.success.hint}", assignmentPage.getSubTitle());
    }
}
