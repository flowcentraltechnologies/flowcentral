/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.NotifTemplateEditor;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application notification template applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioNotifTemplateApplet extends AbstractStudioAppComponentApplet<NotifTemplateEditor> {

    private static FormTabDef formTabDef;

    public StudioNotifTemplateApplet(Page page, StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
    }

    @Override
    protected void onRootHwtFormUpdated(Entity inst) throws UnifyException {
        final NotificationTemplate template = (NotificationTemplate) getForm().getFormBean();
        final Long templateId = template != null ? template.getId() : null;
        NotifTemplateEditor notifTemplateEditor = templateId != null
                ? constructNewNotifTemplateEditor(templateId, template.getDescription())
                : constructNewNotifTemplateEditor(null,
                        au().resolveSessionMessage("$m{notiftemplateeditor.newnotiftemplate}"));
        setDesign(notifTemplateEditor);
    }

    private NotifTemplateEditor constructNewNotifTemplateEditor(Object id, String subTitle) throws UnifyException {
        BreadCrumbs breadCrumbs = getForm().getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{notiftemplateeditor.notiftemplatedesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new NotifTemplateEditor(
                id != null ? new MiniForm(MiniFormScope.MAIN_FORM, getForm().getCtx(), getFormTabDef(au())) : null,
                getHwtForm(), studio(), au(), breadCrumbs);
    }

    private static FormTabDef getFormTabDef(AppletUtilities au) throws UnifyException {
        if (formTabDef == null) {
            synchronized (StudioNotifTemplateApplet.class) {
                if (formTabDef == null) {
                    final EntityDef entityDef = au.getEntityDef("notification.notifTemplate");
                    final List<FormFieldDef> fieldDefs = new ArrayList<FormFieldDef>();
                    EntityFieldDef entityFieldDef = entityDef.getFieldDef("subject");
                    WidgetTypeDef widgetTypeDef = au.getWidgetTypeDef("studio.entitytemplatetext");
                    String renderer = InputWidgetUtils.constructEditorWithBinding(widgetTypeDef, entityFieldDef, null,
                            null);
                    fieldDefs.add(new FormFieldDef(entityFieldDef, widgetTypeDef, null, null,
                            entityFieldDef.getFieldLabel(), renderer, 0, false, false, true, true, true, false));
                    entityFieldDef = entityDef.getFieldDef("template");
                    widgetTypeDef = au.getWidgetTypeDef("studio.entitytemplaterichtexteditor");
                    renderer = InputWidgetUtils.constructEditorWithBinding(widgetTypeDef, entityFieldDef, null, null);
                    fieldDefs.add(new FormFieldDef(entityFieldDef, widgetTypeDef, null, null,
                            entityFieldDef.getFieldLabel(), renderer, 0, false, false, true, true, true, false));

                    formTabDef = new FormTabDef("template", "Template", "email",
                            Arrays.asList(new FormSectionDef(fieldDefs, "details", null, FormColumnsType.TYPE_1, null,
                                    null, true, true, false)));
                }
            }
        }

        return formTabDef;
    }
}
