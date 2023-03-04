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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Database;

/**
 * Abstract details applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsApplet extends AbstractApplet implements SweepingCommitPolicy {

    protected final EntityFormEventHandlers formEventHandlers;

    protected final String childAppletName;

    protected final String childBaseFieldName;

    protected final boolean collaboration;

    protected EntityCRUD childEntityCrud;

    public AbstractDetailsApplet(AppletUtilities au, String pathVariable, boolean collaboration, String childAppletName,
            String baseFieldName, EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(au, pathVariable);
        this.childAppletName = childAppletName;
        this.childBaseFieldName = baseFieldName;
        this.formEventHandlers = formEventHandlers;
        this.collaboration = collaboration;
    }

    public boolean isCollaboration() {
        return collaboration;
    }

    public boolean isSupportChild() {
        return childAppletName != null && childBaseFieldName != null;
    }

    public void childCrudSwitchOnChange() throws UnifyException {
        ctx.au().onMiniformSwitchOnChange(childEntityCrud.getForm());
    }

    public void childCrudSelectItem(int index) throws UnifyException {
        childEntityCrud.enterMaintain(index);
    }

    public void createNewChildCrud(Long parentId) throws UnifyException {
        childEntityCrud = ctx.au().constructEntityCRUD(ctx, childAppletName, formEventHandlers, childBaseFieldName,
                parentId);
    }

    public EntityCRUD getChildEntityCrud() {
        return childEntityCrud;
    }

    public void clearChildEntityCrud() {
        childEntityCrud = null;
    }

    @Override
    public void bumpAllParentVersions(Database db, RecordActionType type) throws UnifyException {

    }
}
