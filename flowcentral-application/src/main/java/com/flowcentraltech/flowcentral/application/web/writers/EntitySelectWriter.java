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
package com.flowcentraltech.flowcentral.application.web.writers;

import com.flowcentraltech.flowcentral.application.web.widgets.EntitySelectWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;
import com.tcdng.unify.web.ui.widget.writer.control.AbstractPopupTextFieldWriter;

/**
 * Entity select widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(EntitySelectWidget.class)
@Component("entityselect-writer")
public class EntitySelectWriter extends AbstractPopupTextFieldWriter {

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        EntitySelectWidget entitySelectWidget = (EntitySelectWidget) widget;
        writer.beginFunction("fux.rigEntitySelect");
        writer.writeParam("pId", entitySelectWidget.getId());
        writer.writeParam("pFacId", entitySelectWidget.getFacadeId());
        writer.writeParam("pFilId", entitySelectWidget.getFacadeId());
        writer.writeParam("pBrdId", entitySelectWidget.getBorderId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pClearable", entitySelectWidget.isClearable());
        Listable listable = entitySelectWidget.getCurrentSelect();
        if (listable != null) {
            writer.writeParam("pDesc", listable.getListDescription());
        }
        String formPanelId = entitySelectWidget.getFormPanelId();
        if (formPanelId != null) {
            writer.writeParam("pFormId", formPanelId);
        }
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
