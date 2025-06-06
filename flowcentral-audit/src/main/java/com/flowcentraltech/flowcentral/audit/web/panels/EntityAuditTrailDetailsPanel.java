/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.audit.web.panels;

import com.flowcentraltech.flowcentral.application.web.panels.AbstractEntityDetailsPanel;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Entity audit trail detail panel.
 * 
 * @author Lateef Ojulari
 * @since 4.1
 */
@EntityReferences("audit.entityAuditKeys")
@Component("fc-entityaudittraildetailspanel")
@UplBinding("web/audit/upl/entityaudittraildetailspanel.upl")
public class EntityAuditTrailDetailsPanel extends AbstractEntityDetailsPanel {

    @Action
    public void reportOptionChange() throws UnifyException {

    }

    @Override
    protected void doSwitchState() throws UnifyException {

    }

}
