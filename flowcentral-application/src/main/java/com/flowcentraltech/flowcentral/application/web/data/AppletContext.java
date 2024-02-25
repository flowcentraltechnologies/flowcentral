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

package com.flowcentraltech.flowcentral.application.web.data;

import java.util.EnumMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractApplet;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityChildCategoryType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Applet context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletContext extends AbstractContext {

    private final AppletUtilities au;

    private final AbstractApplet applet;

    private Map<EntityChildCategoryType, String> entityReferences;

    private EntityActionResult originalEntityActionResult;

    private int tabReadOnlyCounter;

    private boolean auditingEnabled;

    private final boolean parentStateAuditingEnabled;

    private boolean readOnly;

    private boolean inWorkflowPromptViewMode;

    private boolean inWorkflow;

    private boolean review;

    private boolean capture;

    private boolean recovery;

    private boolean emails;

    private boolean comments;

    private boolean attachments;

    private boolean rootFormUpdateDraft;

    public AppletContext(AbstractApplet applet, AppletUtilities au) throws UnifyException {
        this.applet = applet;
        this.au = au;
        this.entityReferences = new EnumMap<EntityChildCategoryType, String>(EntityChildCategoryType.class);
        for (EntityChildCategoryType type : EntityChildCategoryType.values()) {
            this.entityReferences.put(type, null);
        }

        this.auditingEnabled = au.audit() != null && applet != null && applet.getRootAppletDef() != null
                && au.audit().supportsAuditLog(AuditSourceType.APPLET, applet.getRootAppletDef().getEntity())
                && au.system().getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ENABLE_APPLET_SOURCE_AUDITING);
        this.parentStateAuditingEnabled = auditingEnabled && au.system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_PARENT_STATE_AUDITING);
    }

    public AbstractApplet applet() {
        return applet;
    }

    public AppletUtilities au() {
        return au;
    }

    public EnvironmentService environment() {
        return au.environment();
    }

    public String getRootAppletName() {
        return applet.getAppletName();
    }

    public AppletDef getRootAppletDef() throws UnifyException {
        return applet.getRootAppletDef();
    }

    public SpecialParamProvider specialParamProvider() throws UnifyException {
        return au.specialParamProvider();
    }

    public void extractReference(EntityDef entityDef, Object inst) throws UnifyException {
        if (inst instanceof Entity) {
            for (EntityChildCategoryType type : entityReferences.keySet()) {
                if (type.readField() != null && type.readEntity().equals(entityDef.getLongName())) {
                    String ref = DataUtils.getBeanProperty(String.class, inst, type.readField());
                    if (inst instanceof BaseApplicationEntity) {
                        String applicationName = ((BaseApplicationEntity) inst).getApplicationName();
                        ref = ApplicationNameUtils.ensureLongNameReference(applicationName, ref);
                    }

                    entityReferences.put(type, ref);
                }
            }
        }
    }

    public String getReference(EntityChildCategoryType type) {
        String ref = entityReferences.get(type);
        return ref != null ? ref : type.readEntity();
    }

    public boolean isStudioComponent() throws UnifyException {
        return applet.getRootAppletDef().isStudioComponent();
    }

    public void incTabReadOnlyCounter() {
        tabReadOnlyCounter++;
    }

    public void decTabReadOnlyCounter() {
        if (tabReadOnlyCounter > 0) {
            tabReadOnlyCounter--;
        }
    }

    public EntityActionResult getOriginalEntityActionResult() {
        return originalEntityActionResult;
    }

    public void setOriginalEntityActionResult(EntityActionResult originalEntityActionResult) {
        this.originalEntityActionResult = originalEntityActionResult;
    }

    public boolean isAuditingEnabled() {
        return auditingEnabled;
    }

    public boolean isParentStateAuditingEnabled() {
        return parentStateAuditingEnabled;
    }

    public void setAuditingEnabled(boolean auditingEnabled) {
        this.auditingEnabled = auditingEnabled;
    }

    public boolean isRootFormUpdateDraft() {
        return rootFormUpdateDraft;
    }

    public void setRootFormUpdateDraft(boolean rootFormUpdateDraft) {
        this.rootFormUpdateDraft = rootFormUpdateDraft;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isInWorkflowPromptViewMode() {
        return inWorkflowPromptViewMode;
    }

    public void setInWorkflowPromptViewMode(boolean inWorkflowPromptViewMode) {
        this.inWorkflowPromptViewMode = inWorkflowPromptViewMode;
    }

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public boolean isRecovery() {
        return recovery;
    }

    public void setRecovery(boolean recovery) {
        this.recovery = recovery;
    }

    public boolean isEmails() {
        return emails;
    }

    public void setEmails(boolean emails) {
        this.emails = emails;
    }

    public boolean isComments() {
        return comments;
    }

    public void setComments(boolean comments) {
        this.comments = comments;
    }

    public boolean isAttachments() {
        return attachments;
    }

    public void setAttachments(boolean attachments) {
        this.attachments = attachments;
    }

    public boolean isInWorkflow() {
        return inWorkflow;
    }

    public void setInWorkflow(boolean inWorkflow) {
        this.inWorkflow = inWorkflow;
    }

    public boolean isContextEditable() {
        return !readOnly && (review || !inWorkflow) && tabReadOnlyCounter == 0;
    }
}
