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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.AppletNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Create entity single form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CreateEntitySingleFormApplet extends AbstractEntitySingleFormApplet {

    public CreateEntitySingleFormApplet(Page page, AppletUtilities au, List<String> pathVariables) throws UnifyException {
        super(page, au, pathVariables);
        final AppletNameParts parts = ApplicationNameUtils.getAppletNameParts(pathVariables.get(APPLET_NAME_INDEX));
        if (parts.isWithVestigial()) {
            final Long entityInstId = Long.valueOf(parts.getVestigial());
            Entity inst = loadEntity(entityInstId);
            form = constructSingleForm(inst, FormMode.MAINTAIN);
            viewMode = ViewMode.MAINTAIN_PRIMARY_FORM_NO_SCROLL;
        } else {
            form = constructNewForm(FormMode.CREATE);
            viewMode = ViewMode.NEW_PRIMARY_FORM;
        }

        setAltSubCaption(form.getFormTitle());
    }
}
