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

import com.flowcentraltech.flowcentral.application.web.widgets.EntityTextSelectWidget;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.util.WebRegexUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;
import com.tcdng.unify.web.ui.widget.control.TextField;
import com.tcdng.unify.web.ui.widget.writer.control.AbstractPopupTextFieldWriter;

/**
 * Entity text select widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(EntityTextSelectWidget.class)
@Component("entitytextselect-writer")
public class EntityTextSelectWriter extends AbstractPopupTextFieldWriter {

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        EntityTextSelectWidget entityTextSelectWidget = (EntityTextSelectWidget) widget;
        writer.beginFunction("fux.rigEntitySelect");
        writer.writeParam("pId", entityTextSelectWidget.getId());
        writer.writeParam("pFacId", entityTextSelectWidget.getFacadeId());
        writer.writeParam("pFilId", entityTextSelectWidget.getFacadeId());
        writer.writeParam("pBrdId", entityTextSelectWidget.getBorderId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pClearable", entityTextSelectWidget.isClearable());
        Listable listable = entityTextSelectWidget.getCurrentSelect();
        if (listable != null) {
            writer.writeParam("pDesc", listable.getListDescription());
        }
        String formPanelId = entityTextSelectWidget.getFormPanelId();
        if (formPanelId != null) {
            writer.writeParam("pFormId", formPanelId);
        }
        writer.writeParam("pText", true);
        writer.writeParam("pBtnId", entityTextSelectWidget.getPopupButtonId());
        writer.endFunction();
    }

    @Override
    protected void writePopupContent(ResponseWriter writer, AbstractPopupTextField popupTextField)
            throws UnifyException {

    }

    @Override
    protected void doWritePopupTextFieldBehaviour(ResponseWriter writer, AbstractPopupTextField popupTextField,
            boolean popupEnabled) throws UnifyException {

    }

    @Override
    protected String getFormatRegex(TextField textField) throws UnifyException {
        EntityTextSelectWidget entityTextSelect = (EntityTextSelectWidget) textField;
        switch (entityTextSelect.type()) {
            case ALPHANUMERIC:
                return WebRegexUtils.getAlphanumericFormatRegex(entityTextSelect.isSpecial(), entityTextSelect.isDash(),
                        entityTextSelect.isSpace());
            case FULLNAME:
                return WebRegexUtils.getFullNameFormatRegex(entityTextSelect.isSpecial());
            case INTEGER:
                WebRegexUtils.getIntegerTextFormatRegex(entityTextSelect.isAcceptPlus(),
                        entityTextSelect.isAcceptMinus());
            case TEXT:
            default:
                break;

        }

        return "";
    }

    @Override
    protected String getOnShowAction() throws UnifyException {
        return null;
    }

    @Override
    protected String getOnShowParam(AbstractPopupTextField popupTextField) throws UnifyException {
        return null;
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
