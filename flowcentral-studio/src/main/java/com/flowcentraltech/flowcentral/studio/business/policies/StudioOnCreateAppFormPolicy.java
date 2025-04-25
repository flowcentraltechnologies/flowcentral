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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio on create application form policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiooncreateappform-policy")
public class StudioOnCreateAppFormPolicy extends StudioOnCreateComponentPolicy {

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        final AppForm appForm = (AppForm) ctx.getInst();
        if (DataUtils.isBlank(appForm.getElementList())) {
            List<AppFormElement> elementList = new ArrayList<AppFormElement>();
            // Add basic tab with empty section
            AppFormElement appFormElement = new AppFormElement();
            appFormElement.setType(FormElementType.TAB);
            appFormElement.setElementName("basicDetails");
            appFormElement.setTabContentType(TabContentType.MINIFORM);
            appFormElement.setLabel(resolveApplicationMessage("$m{studio.apptable.form.basicdetails}"));
            appFormElement.setVisible(true);
            appFormElement.setEditable(true);
            appFormElement.setDisabled(false);
            appFormElement.setConfigType(ConfigType.CUSTOM);
            elementList.add(appFormElement);

            appFormElement = new AppFormElement();
            appFormElement.setType(FormElementType.SECTION);
            appFormElement.setElementName("basicDetails");
            appFormElement.setSectionColumns(FormColumnsType.TYPE_2);
            appFormElement.setLabel(null);
            appFormElement.setVisible(true);
            appFormElement.setEditable(true);
            appFormElement.setDisabled(false);
            appFormElement.setConfigType(ConfigType.CUSTOM);
            elementList.add(appFormElement);

            appForm.setElementList(elementList);
        }
        
        return null;
    }

}
