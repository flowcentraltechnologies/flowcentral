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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFilterConstants;
import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.Errors;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.data.FormAnnotationDef;
import com.flowcentraltech.flowcentral.application.data.FormAppendables;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFilterDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for forms.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractForm {

    public enum FormMode {
        CREATE(
                AppletPropertyConstants.CREATE_FORM),
        MAINTAIN(
                AppletPropertyConstants.MAINTAIN_FORM),
        ENTITY_CREATE(
                AppletPropertyConstants.ENTITY_FORM),
        ENTITY_MAINTAIN(
                AppletPropertyConstants.ENTITY_FORM),
        LISTING(
                AppletPropertyConstants.LISTING_FORM);

        private final String formProperty;

        private FormMode(String formProperty) {
            this.formProperty = formProperty;
        }

        public String formProperty() {
            return formProperty;
        }

        public boolean isCreate() {
            return CREATE.equals(this) || ENTITY_CREATE.equals(this);
        }

        public boolean isMaintain() {
            return MAINTAIN.equals(this) || ENTITY_MAINTAIN.equals(this);
        }

        public boolean isListing() {
            return LISTING.equals(this);
        }
    }

    private FormContext ctx;

    private SectorIcon sectorIcon;

    private BreadCrumbs breadCrumbs;

    private FormAppendables appendables;

    private FormMode formMode;

    private List<FormActionDef> formActionDefList;

    private String displayItemCounter;

    private String displayItemCounterClass;

    private String warning;

    private String submitCaption;

    private String submitNextCaption;

    private String submitStyleClass;

    private int attachmentCount;

    public AbstractForm(FormContext ctx, SectorIcon sectorIcon, BreadCrumbs breadCrumbs) {
        this.ctx = ctx;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        if (this.breadCrumbs != null) {
            this.breadCrumbs.setLastCrumbTitle(
                    ctx.isWithAltFormTitle() ? ctx.getAltFormTitle() : ctx.getEntityDef().getDescription());
        }
    }

    public FormContext getCtx() {
        return ctx;
    }

    public FormDef getFormDef() {
        return ctx.getFormDef();
    }

    public EntityDef getEntityDef() {
        return ctx.getEntityDef();
    }

    public Object getFormBean() {
        return ctx.getInst();
    }

    public void setFormBean(Object formBean) throws UnifyException {
        ctx.setInst(formBean);
    }

    public ValueStore getFormValueStore() {
        return ctx.getFormValueStore();
    }

    public ValueStoreReader getFormValueStoreReader() {
        return ctx.getFormValueStore().getReader();
    }

    public boolean matchFormBean(String formFilterName) throws UnifyException {
        if (!StringUtils.isBlank(formFilterName)) {
            if (ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME.equals(formFilterName)) {
                return true;
            }

            FormDef _formDef = ctx.getFormDef();
            FormFilterDef _filterDef = _formDef.getFilterDef(formFilterName);
            return _filterDef.getFilterDef()
                    .getObjectFilter(_formDef.getEntityDef(), getFormValueStoreReader(), ctx.au().getNow())
                    .match(ctx.getInst());
        }

        return false;
    }

    public SectorIcon getSectorIcon() {
        return sectorIcon;
    }

    public String getFormTitle() {
        return ctx.isWithAltFormTitle() ? ctx.getAltFormTitle()
                : (breadCrumbs != null ? breadCrumbs.getLastBreadCrumb().getTitle() : null);
    }

    public void setFormTitle(String formTitle) {
        if (breadCrumbs != null) {
            breadCrumbs.setLastCrumbTitle(formTitle);
        }
    }

    public String getBeanTitle() {
        return breadCrumbs != null ? breadCrumbs.getLastBreadCrumb().getSubTitle() : null;
    }

    public void setBeanTitle(String beanTitle) {
        if (breadCrumbs != null) {
            breadCrumbs.setLastCrumbSubTitle(beanTitle);
        }
    }

    public void setFormStepIndex(int stepIndex) {
        if (breadCrumbs != null) {
            breadCrumbs.setLastCrumbStepIndex(stepIndex);
        }
    }

    public int getFormStepIndex() {
        return breadCrumbs == null ? 0 : breadCrumbs.getLastBreadCrumb().getStepIndex();
    }

    public BreadCrumbs getBreadCrumbs() {
        return breadCrumbs;
    }

    public FormAppendables getAppendables() {
        return appendables;
    }

    public void setAppendables(FormAppendables appendables) {
        this.appendables = appendables;
    }

    public InputArrayEntries getEmails() {
        return appendables != null ? appendables.getEmails() : null;
    }

    public Comments getComments() {
        return appendables != null ? appendables.getComments() : null;
    }

    public Errors getErrors() {
        return appendables != null ? appendables.getErrors() : null;
    }

    public List<FormActionDef> getFormActionDefList() {
        if (formActionDefList != null) {
            return formActionDefList;
        }

        return ctx.getFormActionDefList();
    }

    public void setFormActionDefList(List<FormActionDef> formActionDefList) {
        this.formActionDefList = formActionDefList;
    }

    public FormMode getFormMode() {
        return formMode;
    }

    public void setFormMode(FormMode formMode) {
        this.formMode = formMode;
    }

    public String getDisplayItemCounter() {
        return displayItemCounter;
    }

    public void setDisplayItemCounter(String displayItemCounter) {
        this.displayItemCounter = displayItemCounter;
    }

    public boolean isWithDisplayItemCounter() {
        return displayItemCounter != null;
    }

    public String getDisplayItemCounterClass() {
        return displayItemCounterClass;
    }

    public void setDisplayItemCounterClass(String displayItemCounterClass) {
        this.displayItemCounterClass = displayItemCounterClass;
    }

    public String getSubmitCaption() {
        return submitCaption;
    }

    public void setSubmitCaption(String submitCaption) {
        this.submitCaption = submitCaption;
    }

    public String getSubmitNextCaption() {
        return submitNextCaption;
    }

    public void setSubmitNextCaption(String submitNextCaption) {
        this.submitNextCaption = submitNextCaption;
    }

    public String getSubmitStyleClass() {
        return submitStyleClass;
    }

    public void setSubmitStyleClass(String submitStyleClass) {
        this.submitStyleClass = submitStyleClass;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public void clearDisplayItem() {
        displayItemCounterClass = null;
        displayItemCounter = null;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public List<FormAnnotationDef> getFormAnnotationDef() {
        return ctx.getFormAnnotationDef();
    }

    public boolean isWithVisibleAnnotations() {
        return ctx.isWithVisibleAnnotations();
    }

    public boolean isWithValidationErrors() {
        return ctx.isWithValidationErrors();
    }

    public List<FormMessage> getValidationErrors() {
        return ctx.getValidationErrors();
    }

    public boolean isWithSectorIcon() {
        return sectorIcon != null;
    }
}
