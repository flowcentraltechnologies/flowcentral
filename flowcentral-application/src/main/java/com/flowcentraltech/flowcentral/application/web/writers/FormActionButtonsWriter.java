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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.Date;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.widgets.FormActionButtons;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.policies.FormActionPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Form action button writer.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(FormActionButtons.class)
@Component("formactionbuttons-writer")
public class FormActionButtonsWriter extends AbstractControlWriter {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @SuppressWarnings("unchecked")
    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget component) throws UnifyException {
        FormActionButtons formActionButtons = (FormActionButtons) component;
        writer.write("<div ");
        writeTagAttributes(writer, formActionButtons);
        writer.write(">");

        writer.write("<div class=\"fabrow\">");

        final FormContext formContext = ((AbstractForm) formActionButtons.getValueStore().getValueObject()).getCtx();
        final ValueStore formValueStore = formContext.getFormValueStore();
        final ValueStoreReader formValueStoreReader = formValueStore.getReader();
        final Date now = formContext.au().getNow();
        final UserToken userToken = getUserToken();
        final FormMode formMode = formActionButtons.getValue(FormMode.class, "formMode");
        final Set<String> showActionSet = (Set<String>) formActionButtons.getWriteWork()
                .get(FormActionButtons.WORK_SHOWACTIONSET);
        Control actionCtrl = formActionButtons.getActionCtrl();
        for (ValueStore valueStore : formActionButtons.getValueList()) {
            FormActionDef formActionDef = (FormActionDef) valueStore.getValueObject();
            if (formActionDef.isButtonType()) {
                if (formActionDef.isWithCondition()) {
                    ObjectFilter filter = formActionDef.getOnCondition().getObjectFilter(formContext.getEntityDef(),
                            formValueStoreReader, now);
                    if (!filter.matchReader(formValueStoreReader)) {
                        continue;
                    }
                }

                if ((formMode.isListing() || (formActionDef.isShowOnCreate() && formMode.isCreate())
                        || (formActionDef.isShowOnMaintain() && formMode.isMaintain()))
                        && (!formActionDef.isWithPolicy()
                                || ((FormActionPolicy) getComponent(formActionDef.getPolicy()))
                                        .checkAppliesTo(formValueStoreReader))
                        && (!formActionDef.isWithPrivilege() || applicationPrivilegeManager
                                .isRoleWithPrivilege(userToken.getRoleCode(), formActionDef.getPrivilege()))) {
                    showActionSet.add(formActionDef.getName());
                    writer.write("<div class=\"fabcell\">");
                    actionCtrl.setValueStore(valueStore);
                    writer.writeStructureAndContent(actionCtrl);
                    writer.write("</div>");
                }
            }
        }

        writer.write("</div>");

        writer.write("</div>");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        FormActionButtons formActionButtons = (FormActionButtons) widget;
        // All behavior should be tied to action control
        EventHandler[] eventHandlers = formActionButtons.getEventHandlers();
        if (eventHandlers != null) {
            Control actionCtrl = formActionButtons.getActionCtrl();
            final Set<String> showActionSet = (Set<String>) formActionButtons.getWriteWork()
                    .get(FormActionButtons.WORK_SHOWACTIONSET);
            for (ValueStore valueStore : formActionButtons.getValueList()) {
                FormActionDef formActionDef = (FormActionDef) valueStore.getValueObject();
                if (showActionSet.contains(formActionDef.getName())) {
                    String confirmation = formActionDef.isWithPolicy()
                            ? getComponent(FormActionPolicy.class, formActionDef.getPolicy()).getConfirmation()
                            : null;
                    confirmation = resolveSessionMessage(confirmation);
                    actionCtrl.setValueStore(valueStore);
                    getRequestContext().setQuickReference(valueStore);
                    for (EventHandler eventHandler : eventHandlers) {
                        writer.setConfirm(confirmation);
                        try {
                            writer.writeBehavior(eventHandler, actionCtrl.getId(), null, null);
                        } finally {
                            writer.clearConfirm();
                        }
                    }
                }
            }
        }
    }
}
