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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralStandalonePanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Entity property sequence panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitypropertysequencepanel")
@UplBinding("web/application/upl/entitypropertysequencepanel.upl")
public class EntityPropertySequencePanel extends AbstractFlowCentralStandalonePanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntityPropertySequence entityFieldSequence = getEntityPropertySequence();
        setVisible("clearBtn", entityFieldSequence.isClearButtonVisible());
        setVisible("applyBtn", entityFieldSequence.isApplyButtonVisible());
    }

    @Action
    public void clear() throws UnifyException {
        getEntityPropertySequence().clear();
    }

    @Action
    public void apply() throws UnifyException {
        getEntityPropertySequence().save();
    }

    private EntityPropertySequence getEntityPropertySequence() throws UnifyException {
        return getValue(EntityPropertySequence.class);
    }
}
