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
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Manage loading details applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageLoadingDetailsApplet extends AbstractApplet {

    private LoadingTable resultTable;

    private final String childAppletName;

    private final String childBaseFieldName;

    private final EntityFormEventHandlers formEventHandlers;

    private EntityCRUD childEntityCrud;

    public ManageLoadingDetailsApplet(AppletUtilities au, String pathVariable) throws UnifyException {
        super(au, pathVariable);
        this.childAppletName = null;
        this.childBaseFieldName = null;
        this.formEventHandlers = null;
    }

    public ManageLoadingDetailsApplet(AppletUtilities au, String pathVariable, String childAppletName,
            String childBaseFieldName, EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(au, pathVariable);
        this.childAppletName = childAppletName;
        this.childBaseFieldName = childBaseFieldName;
        this.formEventHandlers = formEventHandlers;
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
        EntityTable entityTable = childEntityCrud.getTable();
        Restriction restriction = new Equals(childBaseFieldName, parentId);
        Restriction baseRestriction = entityTable.getRestriction(FilterType.BASE, null, ctx.au().getNow());
        if (baseRestriction != null) {
            restriction = new And().add(restriction).add(baseRestriction);
        }

        entityTable.setSourceObjectKeepSelected(restriction);
        entityTable.setCrudActionHandlers(formEventHandlers.getCrudActionHandlers());

        if (ctx.isContextEditable()) {
            childEntityCrud.enterCreate();
        } else {
            childEntityCrud.enterMaintain(0);
        }
    }

    public EntityCRUD getChildEntityCrud() {
        return childEntityCrud;
    }

    public void clearChildEntityCrud() {
        childEntityCrud = null;
    }

    public LoadingTable getResultTable() {
        return resultTable;
    }

    public void setResultTable(LoadingTable resultTable) {
        this.resultTable = resultTable;
    }
}
