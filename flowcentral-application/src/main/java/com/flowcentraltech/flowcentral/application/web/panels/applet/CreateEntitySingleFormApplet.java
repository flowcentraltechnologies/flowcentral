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
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Create entity single form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CreateEntitySingleFormApplet extends AbstractEntitySingleFormApplet {

    public CreateEntitySingleFormApplet(AppletUtilities au, List<String> pathVariables) throws UnifyException {
        super(au, pathVariables);
        final String vestigial = ApplicationNameUtils.getVestigialNamePart(pathVariables.get(APPLET_NAME_INDEX));
        if (vestigial == null) {
            form = constructNewForm(FormMode.CREATE);
            viewMode = ViewMode.NEW_PRIMARY_FORM;
        } else {
            final Long entityInstId = Long.valueOf(vestigial);
            Entity inst = loadEntity(entityInstId);
            form = constructSingleForm(inst, FormMode.MAINTAIN);
            viewMode = ViewMode.MAINTAIN_PRIMARY_FORM_NO_SCROLL;
        }

        setAltSubCaption(form.getFormTitle());
    }
}
