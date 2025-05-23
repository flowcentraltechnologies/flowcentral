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
package com.flowcentraltech.flowcentral.common.web.panels;

import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Basic panel for presenting and capturing report options.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-reportrunnerpanel")
@UplBinding("web/common/upl/reportrunnerpanel.upl")
public class ReportRunnerPanel extends BaseDialogPanel {

    @Action
    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        Popup popup = getCurrentPopup();
        ReportOptions reportOptions = (ReportOptions) popup.getBackingBean();
        setVisible("runnerTitlePanel", StringUtils.isNotBlank(reportOptions.getReportDescription()));
        setVisible("rptColumnOptionsPanel", !reportOptions.isUserInputOnly() && reportOptions.isWithColumnOptions());
        setVisible("rptParamsPanel", reportOptions.isWithUserInput());
        setVisible("reportOptionsPanel", !reportOptions.isLetter());
    }

}
