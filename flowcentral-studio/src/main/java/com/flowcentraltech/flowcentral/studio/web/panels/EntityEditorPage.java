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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.web.widgets.EntityEditor;
import com.tcdng.unify.core.UnifyException;

/**
 * Entity editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityEditorPage extends AbstractStudioEditorPage {

    private final EntityDef entityDef;

    private final Object baseId;

    private EntityEditor entityEditor;

    public EntityEditorPage(AppletUtilities au, EntityDef entityDef, Object baseId, BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.entityDef = entityDef;
        this.baseId = baseId;
    }

    public EntityEditor getEntityEditor() {
        return entityEditor;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Object getBaseId() {
        return baseId;
    }

    public void commitDesign() throws UnifyException {
        // TODO
    }

    public void newEditor() throws UnifyException {
        EntityEditor.Builder eeb = EntityEditor.newBuilder(au(), entityDef);
        entityEditor = eeb.build();
    }
}
