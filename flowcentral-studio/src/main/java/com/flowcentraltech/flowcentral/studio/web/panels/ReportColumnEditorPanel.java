/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.panel.DetachedPanel;

/**
 * Report column editor panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-reportcolumneditorpanel")
@UplBinding("web/studio/upl/reportcolumneditorpanel.upl")
public class ReportColumnEditorPanel extends DetachedPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setDisabled("editValField", true);
    }

    public String getFieldId() throws UnifyException {
        return getWidgetByShortName("editValField").getId();
    }

    public String getOrderId() throws UnifyException {
        return getWidgetByShortName("editValOrder").getId();
    }

    public String getWidgetId() throws UnifyException {
        return getWidgetByShortName("editValWidget").getId();
    }

    public String getHorizId() throws UnifyException {
        return getWidgetByShortName("editValHoriz").getId();
    }

    public String getVertId() throws UnifyException {
        return getWidgetByShortName("editValVert").getId();
    }

    public String getDescId() throws UnifyException {
        return getWidgetByShortName("editValDesc").getId();
    }

    public String getFormatterId() throws UnifyException {
        return getWidgetByShortName("editValFormatter").getId();
    }

    public String getWidthId() throws UnifyException {
        return getWidgetByShortName("editValWidth").getId();
    }

    public String getBoldId() throws UnifyException {
        return getWidgetByShortName("editValBold").getId();
    }

    public String getGroupId() throws UnifyException {
        return getWidgetByShortName("editValGroup").getId();
    }

    public String getGroupNewId() throws UnifyException {
        return getWidgetByShortName("editValGroupNew").getId();
    }

    public String getSumId() throws UnifyException {
        return getWidgetByShortName("editValSum").getId();
    }

    public String getApplyId() throws UnifyException {
        return getWidgetByShortName("editApplyBtn").getId();
    }

    public String getCancelId() throws UnifyException {
        return getWidgetByShortName("editCancelBtn").getId();
    }
}
