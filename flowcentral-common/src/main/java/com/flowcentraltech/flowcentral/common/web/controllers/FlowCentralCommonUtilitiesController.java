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

package com.flowcentraltech.flowcentral.common.web.controllers;

import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleAuditConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.CollaborationLockedResourceInfo;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.ui.AbstractCommonUtilitiesPageController;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Flow central common utilities controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/application/commonutilities")
@UplBinding("web/common/upl/flowcentralcommonutilities.upl")
@ResultMappings({
        @ResultMapping(name = "showapplicationreportoptions",
                response = { "!showpopupresponse popup:$s{reportRunnerPopup}" }),
        @ResultMapping(name = "viewreport",
                response = { "!refreshpanelresponse panels:$l{reportRunnerPopup}", "!commonreportresponse" }),
        @ResultMapping(name = "viewlistingreport", response = { "!commonlistingreportresponse" }),
        @ResultMapping(name = "showlockedresource", response = { "!showpopupresponse popup:$s{lockedResourcePopup}" }),
        @ResultMapping(name = "grablocksuccess", response = { "!hidepopupresponse" }) })
public class FlowCentralCommonUtilitiesController
        extends AbstractCommonUtilitiesPageController<FlowCentralCommonUtilitiesPageBean> {

    @Configurable
    private CollaborationProvider collaborationProvider;
    
    public FlowCentralCommonUtilitiesController() {
        super(FlowCentralCommonUtilitiesPageBean.class);
    }

    public void setCollaborationProvider(CollaborationProvider collaborationProvider) {
        this.collaborationProvider = collaborationProvider;
    }

    @Action
    public String showLockedResource() throws UnifyException {
        return "showlockedresource";
    }

    @Action
    public String grabResourceLock() throws UnifyException {
        if (collaborationProvider != null) {
            CollaborationLockedResourceInfo info = (CollaborationLockedResourceInfo) getSessionAttribute(
                    FlowCentralSessionAttributeConstants.LOCKED_RESOURCEOPTIONS);
            collaborationProvider.grabLock(info.getType(), info.getResourceName(), getUserToken().getUserLoginId());
        }

        return "grablocksuccess";
    }

    @Action
    public String generateReport() throws UnifyException {
        Popup popup = removeCurrentPopup();
        ReportOptions reportOptions = (ReportOptions) popup.getBackingBean();
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORTOPTIONS, reportOptions);
        getEventLogger().logUserEvent(CommonModuleAuditConstants.GENERATE_REPORT, reportOptions.getTitle());
        return "viewreport";
    }

    @Action
    public String closeReport() throws UnifyException {
        return hidePopup();
    }

}
