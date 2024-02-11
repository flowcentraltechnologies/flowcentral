/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.SingleFormPanel;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Single form widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-singleform")
public class SingleFormWidget extends AbstractFlowCentralMultiControl {

    @SuppressWarnings("rawtypes")
    private SingleFormPanel singleFormPanel;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public SingleForm getSingleForm() throws UnifyException {
        SingleForm singleForm = getValue(SingleForm.class);
        ValueStore newValueStore = singleForm != null ? singleForm.getBeanValueStore() : null;
        ValueStore oldValueStore = singleFormPanel != null ? singleFormPanel.getValueStore() : null;
        if (newValueStore != oldValueStore) {
            removeAllExternalChildWidgets();
            singleFormPanel = (SingleFormPanel) addExternalChildStandalonePanel(singleForm.getPanelName(),
                    getId() + "_sg");
            singleFormPanel.setValueStore(newValueStore);
            if (singleForm != null) {
                singleFormPanel.initializeBean(singleForm.getBean());
            }
        }

        return singleForm;
    }

    public FormContext getCtx() throws UnifyException {
        return getSingleForm().getCtx();
    }

    @SuppressWarnings("rawtypes")
    public SingleFormPanel getSingleFormPanel() throws UnifyException {
        getSingleForm();
        return singleFormPanel;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }

}
