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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.web.widgets.BeanTableWidget;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.panel.AbstractStandalonePanel;

/**
 * Inline CRUD panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-inlinecrudpanel")
@UplBinding("web/application/upl/inlinecrudpanel.upl")
public class InlineCRUDPanel extends AbstractStandalonePanel {

    public FormValidationErrors validate(EvaluationMode evaluationMode) throws UnifyException {
        return getCrud().validate(evaluationMode);
    }

    @Action
    public void addItem() throws UnifyException {
        getCrud().addEntry();
    }

    @Action
    public void deleteItem() throws UnifyException {
        int index = getRequestTarget(int.class);
        getCrud().deleteEntry(index);
    }

    @Action
    public void onRowChange() throws UnifyException {
        int index = getRequestTarget(int.class);
        String focusWidgetId = getRequestContextUtil().getTriggerWidgetId();
        BeanTableWidget entryTableWidget = getWidgetByShortName(BeanTableWidget.class, "entryTable");
        String trigger = entryTableWidget.resolveChildWidgetName(focusWidgetId);
        getCrud().fireOnRowChange(new RowChangeInfo(trigger, index));
    }

    private InlineCRUD<?> getCrud() throws UnifyException {
        return getValue(InlineCRUD.class);
    }

}
