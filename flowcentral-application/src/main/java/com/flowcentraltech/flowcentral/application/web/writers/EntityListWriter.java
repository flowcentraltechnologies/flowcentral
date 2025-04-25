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
package com.flowcentraltech.flowcentral.application.web.writers;

import com.flowcentraltech.flowcentral.application.web.widgets.EntityListWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;
import com.tcdng.unify.web.ui.widget.writer.control.AbstractPopupTextFieldWriter;

/**
 * Entity list widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(EntityListWidget.class)
@Component("entitylist-writer")
public class EntityListWriter extends AbstractPopupTextFieldWriter {

    @Override
    protected void writePopupContent(ResponseWriter writer, AbstractPopupTextField popupTextField)
            throws UnifyException {
        EntityListWidget entityListWidget = (EntityListWidget) popupTextField;
        writer.write("<div id=\"").write(entityListWidget.getFramePanelId())
                .write("\" class=\"ssborder\" style=\"overflow-y:auto;overflow-x:hidden;\" tabindex=\"0\">");
        writer.write("<div id=\"").write(entityListWidget.getListPanelId()).write("\" class=\"sslist\">");
        WriteWork work = entityListWidget.getWriteWork();
        String blankOption = entityListWidget.getBlankOption();
        if (blankOption != null) {
            writer.write("<a id=\"").write(entityListWidget.getBlankOptionId()).write("\">");
            if (StringUtils.isBlank(blankOption)) {
                writer.writeHtmlFixedSpace();
            } else {
                writer.writeWithHtmlEscape(blankOption);

            }

            writer.write("</a>");
        }

        for (String nindex : (String[]) work.get(EntityListWidget.WORK_SELECTIDS)) {
            writer.write("<a");
            writeTagId(writer, nindex);
            writer.write("></a>");
        }

        writer.write("</div>");
        writer.write("</div>");
    }

    @Override
    protected void doWritePopupTextFieldBehaviour(ResponseWriter writer, AbstractPopupTextField popupTextField,
            boolean popupEnabled) throws UnifyException {
        EntityListWidget entityListWidget = (EntityListWidget) popupTextField;
        WriteWork work = entityListWidget.getWriteWork();
        String[] selectIds = (String[]) work.get(EntityListWidget.WORK_SELECTIDS);
        writer.beginFunction("ux.rigSingleSelect");
        writer.writeParam("pId", entityListWidget.getId());
        writer.writeParam("pFacId", entityListWidget.getFacadeId());
        writer.writeParam("pFrmId", entityListWidget.getFramePanelId());
        writer.writeParam("pLstId", entityListWidget.getListPanelId());
        writer.writeParam("pBlnkId", entityListWidget.getBlankOptionId());
        writer.writeParam("pICnt", selectIds.length);
        writer.writeParam("pSelectIds", selectIds);
        writer.writeParam("pKeys", (String[]) work.get(EntityListWidget.WORK_KEYS));
        writer.writeParam("pLabels", (String[]) work.get(EntityListWidget.WORK_LABELS));
        writer.writeParam("pIsBlankOption", entityListWidget.getBlankOption() != null);
        writer.writeParam("pNormCls", "norm");
        writer.writeParam("pSelCls", getUserColorStyleClass("sel"));
        writer.writeParam("pEnabled", popupEnabled);
        writer.writeParam("pVal", entityListWidget.getStringValue());
        writer.endFunction();
    }

    @Override
    protected String getOnShowAction() throws UnifyException {
        return "ux.ssOnShow";
    }

    @Override
    protected String getOnShowParam(AbstractPopupTextField popupTextField) throws UnifyException {
        EntityListWidget entityListWidget = (EntityListWidget) popupTextField;
        JsonWriter jw = new JsonWriter();
        jw.beginObject();
        jw.write("pFrmId", entityListWidget.getFramePanelId());
        jw.endObject();
        return jw.toString();
    }

    @Override
    protected String getOnHideAction() throws UnifyException {
        return null;
    }

    @Override
    protected String getOnHideParam(AbstractPopupTextField popupTextField) throws UnifyException {
        return null;
    }

}
