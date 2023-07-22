/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Manage entity list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageEntityListApplet extends AbstractEntityFormApplet {

    private String appletDescription;

    private static final Restriction UPDATE_DRAFT_BASE_RESTRICTION = new And()
            .add(new Equals("wfItemVersionType", WfItemVersionType.DRAFT))
            .add(new Or().add(new IsNull("inWorkflow")).add(new Equals("inWorkflow", Boolean.FALSE)));

    public ManageEntityListApplet(AppletUtilities au, String pathVariable,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        final AppletDef _rootAppletDef = getRootAppletDef();
        setCurrFormAppletDef(_rootAppletDef);
        final String vestigial = ApplicationNameUtils.getVestigialNamePart(pathVariable);
        final boolean isUpdateDraft = ApplicationNameUtils.WORKFLOW_COPY_UPDATE_DRAFT_PATH_SUFFIX.equals(vestigial);
        entitySearch = au.constructEntitySearch(new FormContext(getCtx()), this, null, _rootAppletDef.getDescription(),
                getCurrFormAppletDef(), null,
                isUpdateDraft ? EntitySearch.ENABLE_ALL & ~EntitySearch.SHOW_NEW_BUTTON : EntitySearch.ENABLE_ALL,
                false, false);
        if (isUpdateDraft) {
            entitySearch.setAltTableLabel(au.resolveSessionMessage("$m{pagetitle.updatedraft}",
                    entitySearch.getEntityTable().getTableDef().getLabel()));
            appletDescription = au.resolveSessionMessage("$m{pagetitle.updatedraft}", super.getAppletDescription());
        } else {
            appletDescription = super.getAppletDescription();
        }

        if (isRootAppletPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
            AppletFilterDef appletFilterDef = getRootAppletFilterDef(AppletPropertyConstants.BASE_RESTRICTION);
            entitySearch.setBaseFilter(InputWidgetUtils.getFilterDef(au, _rootAppletDef, appletFilterDef.getFilterDef(),
                    isUpdateDraft ? UPDATE_DRAFT_BASE_RESTRICTION : null, au.specialParamProvider(), au.getNow()),
                    au.specialParamProvider());
        } else {
            if (isUpdateDraft) {
                entitySearch.setBaseFilter(InputWidgetUtils.getFilterDef(au, UPDATE_DRAFT_BASE_RESTRICTION),
                        au.specialParamProvider());
            }
        }

        navBackToSearch();
    }

    @Override
    public String getAppletDescription() throws UnifyException {
        return appletDescription;
    }

    @Override
    public boolean navBackToSearch() throws UnifyException {
        setAltSubCaption(entitySearch.getEntityDef().getDescription());
        return super.navBackToSearch();
    }

    @Override
    public boolean navBackToPrevious() throws UnifyException {
        setAltSubCaption(entitySearch.getEntityDef().getDescription());
        return super.navBackToPrevious();
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        return null;
    }
}
