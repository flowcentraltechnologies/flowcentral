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

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.ui.widget.AbstractPanel;

/**
 * Entity multi-select panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitymultiselectpanel")
@UplBinding("web/application/upl/entitymultiselectpanel.upl")
public class EntityMultiSelectPanel extends AbstractPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        EntitySelect entitySelect = getEntitySelect();
        if (entitySelect != null) {
            setVisible("searchFilterPanel", entitySelect.isEnableFilter());
        }
    }

    @Action
    public void search() throws UnifyException {
        EntitySelect entitySelect = getEntitySelect();
        entitySelect.applyFilterToSearch();
    }

    @Action
    public void select() throws UnifyException {
        EntitySelect entitySelect = (EntitySelect) removeSessionAttribute(
                FlowCentralSessionAttributeConstants.ENTITYSELECT);
        entitySelect.applySelect();
        setRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH, Boolean.TRUE); // TODO Get from entity select flag
        setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
    }

    @Action
    public void close() throws UnifyException {
        removeSessionAttribute(FlowCentralSessionAttributeConstants.ENTITYSELECT);
        setCommandResultMapping(ResultMappingConstants.HIDE_POPUP);
    }

    private EntitySelect getEntitySelect() throws UnifyException {
        return getValue(EntitySelect.class);
    }

}
