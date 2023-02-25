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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.panel.DetachedPanel;

/**
 * Table column editor panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-tablecolumneditorpanel")
@UplBinding("web/studio/upl/tablecolumneditorpanel.upl")
public class TableColumnEditorPanel extends DetachedPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setDisabled("editValField", true);
    }

    public String getFieldId() throws UnifyException {
        return getWidgetByShortName("editValField").getId();
    }

    public String getLabelId() throws UnifyException {
        return getWidgetByShortName("editValLabel").getId();
    }

    public String getLinkId() throws UnifyException {
        return getWidgetByShortName("editValLink").getId();
    }

    public String getSymbolId() throws UnifyException {
        return getWidgetByShortName("editValSymbol").getId();
    }

    public String getOrderId() throws UnifyException {
        return getWidgetByShortName("editValOrder").getId();
    }

    public String getWidgetId() throws UnifyException {
        return getWidgetByShortName("editValWidget").getId();
    }

    public String getWidthId() throws UnifyException {
        return getWidgetByShortName("editValWidth").getId();
    }

    public String getSwitchId() throws UnifyException {
        return getWidgetByShortName("editValSwitch").getId();
    }

    public String getHiddenId() throws UnifyException {
        return getWidgetByShortName("editValHidden").getId();
    }

    public String getDisabledId() throws UnifyException {
        return getWidgetByShortName("editValDisabled").getId();
    }

    public String getEditableId() throws UnifyException {
        return getWidgetByShortName("editValEditable").getId();
    }

    public String getSortableId() throws UnifyException {
        return getWidgetByShortName("editValSortable").getId();
    }

    public String getSummaryId() throws UnifyException {
        return getWidgetByShortName("editValSummary").getId();
    }

    public String getApplyId() throws UnifyException {
        return getWidgetByShortName("editApplyBtn").getId();
    }

    public String getCancelId() throws UnifyException {
        return getWidgetByShortName("editCancelBtn").getId();
    }
}
