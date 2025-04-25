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

package com.flowcentraltech.flowcentral.studio.web.writers;

import com.flowcentraltech.flowcentral.studio.web.panels.ReportColumnEditorPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.panel.DetachedPanelWriter;

/**
 * Report column editor panel writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(ReportColumnEditorPanel.class)
@Component("reportcolumneditorpanel-writer")
public class ReportColumnEditorPanelWriter extends DetachedPanelWriter {

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        ReportColumnEditorPanel reportColumnEditorPanel = (ReportColumnEditorPanel) widget;
        writer.beginFunction("fuxstudio.rigReportColumnEditorPanel");
        writer.writeParam("pId", reportColumnEditorPanel.getId());
        writer.writeParam("pFieldId", reportColumnEditorPanel.getFieldId());
        writer.writeParam("pWidgetId", reportColumnEditorPanel.getWidgetId());
        writer.writeParam("pOrderId", reportColumnEditorPanel.getOrderId());
        writer.writeParam("pHorizId", reportColumnEditorPanel.getHorizId());
        writer.writeParam("pVertId", reportColumnEditorPanel.getVertId());
        writer.writeParam("pDescId", reportColumnEditorPanel.getDescId());
        writer.writeParam("pFormatterId", reportColumnEditorPanel.getFormatterId());
        writer.writeParam("pWidthId", reportColumnEditorPanel.getWidthId());
        writer.writeParam("pBoldId", reportColumnEditorPanel.getBoldId());
        writer.writeParam("pGroupId", reportColumnEditorPanel.getGroupId());
        writer.writeParam("pGroupNewId", reportColumnEditorPanel.getGroupNewId());
        writer.writeParam("pSumId", reportColumnEditorPanel.getSumId());
        writer.writeParam("pApplyId", reportColumnEditorPanel.getApplyId());
        writer.writeParam("pCancelId", reportColumnEditorPanel.getCancelId());
        writer.endFunction();
    }

}
