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

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.application.web.widgets.SingleForm;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity single form.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntitySingleForm extends AbstractForm {

    public enum UpdateType {
        SWITCH_ON_CHANGE,
        MAINTAIN_INST,
        NAV_BACK_TO_PREVIOUS,
        FORMACTION_ON_INST,
        UPDATE_INST
    }

    private SingleForm singleForm;

    private UpdateType updateType;

    public EntitySingleForm(FormContext ctx, SectorIcon sectorIcon, BreadCrumbs breadCrumbs, String panelName,
            SingleFormBean bean) {
        super(ctx, sectorIcon, breadCrumbs);
        singleForm = new SingleForm(ctx, panelName, bean);
    }

    public SingleForm getSingleForm() {
        return singleForm;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public void loadSingleFormBean() throws UnifyException {
        singleForm.getBean().load((Entity) getFormBean());
    }

    public void unloadSingleFormBean() throws UnifyException {
        singleForm.getBean().unload((Entity) getFormBean());
    }

    public boolean isSingleFormType() {
        return true;
    }
}
