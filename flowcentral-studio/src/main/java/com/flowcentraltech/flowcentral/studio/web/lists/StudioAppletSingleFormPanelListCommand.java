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

package com.flowcentraltech.flowcentral.studio.web.lists;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.web.panels.SingleFormPanel;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractEntityTypeListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.LongParam;

/**
 * Studio applet single form panel list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
@Component("studiosingleformpanellist")
public class StudioAppletSingleFormPanelListCommand extends AbstractEntityTypeListCommand<SingleFormPanel, LongParam> {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    public StudioAppletSingleFormPanelListCommand() {
        super(SingleFormPanel.class, LongParam.class);
    }

    @Override
    protected String getEntityName(LongParam param) throws UnifyException {
        if (param.isPresent()) {
            return applicationModuleService.getAppAppletEntity(param.getValue());
        }
        
        return null;
    }

}
