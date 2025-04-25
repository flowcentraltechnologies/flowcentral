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

package com.flowcentraltech.flowcentral.studio.web.lists;

import com.flowcentraltech.flowcentral.common.annotation.FormSectionLoading;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractEntityTypeListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.StringParam;
import com.tcdng.unify.web.ui.widget.Panel;

/**
 * Studio form section panel list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioformsectionpanellist")
public class StudioFormSectionPanelListCommand extends AbstractEntityTypeListCommand<Panel, StringParam> {

    public StudioFormSectionPanelListCommand() {
        super(Panel.class, StringParam.class);
    }

    @Override
    protected boolean accept(Class<Panel> type) {
        return type.isAnnotationPresent(FormSectionLoading.class);
    }

    protected boolean acceptNonReferenced() {
        return false;
    }

    @Override
    protected String getEntityName(StringParam param) throws UnifyException {
        if (param.isPresent()) {
            return param.getValue();
        }

        return null;
    }

}
