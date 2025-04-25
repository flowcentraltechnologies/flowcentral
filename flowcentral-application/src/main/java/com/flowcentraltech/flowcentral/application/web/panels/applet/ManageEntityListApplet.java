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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.util.AppletNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.tcdng.unify.common.constants.WfItemVersionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Manage entity list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ManageEntityListApplet extends AbstractEntityFormApplet {

    private String appletDescription;

    private static final Restriction UPDATE_DRAFT_BASE_RESTRICTION = new Equals("wfItemVersionType",
            WfItemVersionType.DRAFT);

    private static final Restriction ORIGINAL_BASE_RESTRICTION = new Equals("wfItemVersionType",
            WfItemVersionType.ORIGINAL);

    public ManageEntityListApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers);
        final AppletDef _rootAppletDef = getRootAppletDef();
        setCurrFormAppletDef(_rootAppletDef);
        final AppletNameParts parts = ApplicationNameUtils.getAppletNameParts(pathVariables.get(APPLET_NAME_INDEX));
        final boolean isUpdateDraft = ApplicationNameUtils.WORKFLOW_COPY_UPDATE_DRAFT_PATH_SUFFIX.equals(parts.getVestigial());
        entitySearch = au.constructEntitySearch(new FormContext(appletCtx()), this, null, _rootAppletDef.getDescription(),
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
                    isWorkflowCopy() ? (isUpdateDraft ? UPDATE_DRAFT_BASE_RESTRICTION : ORIGINAL_BASE_RESTRICTION)
                            : null,
                    au.getNow()), au.specialParamProvider());
        } else {
            if (isWorkflowCopy()) {
                entitySearch.setBaseFilter(
                        InputWidgetUtils.getFilterDef(au,
                                isUpdateDraft ? UPDATE_DRAFT_BASE_RESTRICTION : ORIGINAL_BASE_RESTRICTION),
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
