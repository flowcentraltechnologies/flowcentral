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
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for applet objects.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplet {

    protected enum PreferredFormType {
        INPUT_ONLY,
        LISTING_ONLY,
        ALL;

        public boolean supports(FormDef formDef) {
            return (this.equals(INPUT_ONLY) && formDef.isInputForm())
                    || (this.equals(LISTING_ONLY) && formDef.isListingForm()) || this.equals(ALL);
        }
    }
    
    protected final AppletUtilities au;

    protected final AppletContext ctx;

    private final String appletName;

    private AppletDef rootAppletDef;

    private String altSubCaption;;

    public AbstractApplet(AppletUtilities au, String appletName) throws UnifyException {
        this.appletName = ApplicationNameUtils.removeVestigialNamePart(appletName);
        this.au = au;
        this.ctx = new AppletContext(this, au);
    }

    public String getAppletName() {
        return appletName;
    }

    public final AppletContext getCtx() {
        return ctx;
    }

    public String getAppletLabel() throws UnifyException {
        return getRootAppletDef().getLabel();
    }

    public String getAppletDescription() throws UnifyException {
        return getRootAppletDef().getDescription();
    }

    public String getPageAltCaption() throws UnifyException {
        return au.resolveSessionMessage(
                getRootAppletDef().getPropValue(String.class, AppletPropertyConstants.PAGE_ALTERNATE_CAPTION));
    }

    public String getPageAltSubCaption() throws UnifyException {
        return !StringUtils.isBlank(altSubCaption) ? altSubCaption
                : au.resolveSessionMessage(getRootAppletDef().getPropValue(String.class,
                        AppletPropertyConstants.PAGE_ALTERNATE_SUBCAPTION));
    }

    public AppletDef getRootAppletDef() throws UnifyException {
        if (rootAppletDef == null) {
            rootAppletDef = au.getAppletDef(appletName);
        }

        return rootAppletDef;
    }

    protected void setAltSubCaption(String altSubCaption) {
        this.altSubCaption = altSubCaption;
    }

    public final AppletUtilities au() {
        return au;
    }

    public void ensureRootAppletStruct() throws UnifyException {
        if (rootAppletDef != null) {
            AppletDef _nAppletDef = au.getAppletDef(rootAppletDef.getLongName());
            if (rootAppletDef.getVersion() != _nAppletDef.getVersion()) {
                rootAppletDef = _nAppletDef;
            }
        }
    }

    protected void setFormProperties(AppletDef _appletDef, AbstractForm form) throws UnifyException {
        String submitCaption = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_CAPTION);
        String submitNextCaption = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_NEXT_CAPTION);
        boolean submitButtonHighlight = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_BUTTON_HIGHLIGHT);
        form.setSubmitCaption(submitCaption);
        form.setSubmitNextCaption(submitNextCaption);
        if (submitButtonHighlight) {
            form.setSubmitStyleClass("fc-greenbutton");
        }
    }

    protected FormDef getPreferredForm(PreferredFormType type, AppletDef appletDef, Entity inst, String formProperty)
            throws UnifyException {
        if (appletDef.isWithPreferredFormFilters()) {
            EntityDef _entityDef = getEntityClassDef(appletDef.getEntity()).getEntityDef();
            for (FilterDef filterDef : appletDef.getPreferredFormFilterList()) {
                if (filterDef.getObjectFilter(_entityDef, au.getSpecialParamProvider(), au.getNow()).match(inst)) {
                    String formName = filterDef.getPreferredForm();
                    FormDef formDef = au.getFormDef(formName);
                    if (type.supports(formDef)) {
                        return formDef;
                    }
                }
            }
        }

        String formName = appletDef.getPropValue(String.class, formProperty);
        return !StringUtils.isBlank(formName) ? au.getFormDef(formName) : null;
    }

    protected boolean isRootAppletProp(String propName) throws UnifyException {
        return getRootAppletDef().isProp(propName);
    }

    protected boolean isRootAppletPropWithValue(String propName) throws UnifyException {
        AppletDef rootAppletDef = getRootAppletDef();
        return rootAppletDef.isProp(propName)
                && !StringUtils.isBlank(rootAppletDef.getPropValue(String.class, propName));
    }

    protected <T> T getRootAppletProp(Class<T> dataClazz, String propName) throws UnifyException {
        return getRootAppletDef().getPropValue(dataClazz, propName);
    }

    protected <T> T getRootAppletProp(Class<T> dataClazz, String propName, T defVal) throws UnifyException {
        return getRootAppletDef().getPropValue(dataClazz, propName, defVal);
    }

    protected EntityClassDef getEntityClassDef(String entityName) throws UnifyException {
        return au.getEntityClassDef(entityName);
    }

    protected EntityDef getEntityDef(String entityName) throws UnifyException {
        return au.getEntityDef(entityName);
    }

    protected FormDef getFormDef(String formName) throws UnifyException {
        return au.getFormDef(formName);
    }

    protected TableDef getRootAppletTableDef(String tblPropName) throws UnifyException {
        return au.getTableDef(getRootAppletDef().getPropValue(String.class, tblPropName));
    }

    protected FilterDef getRootAppletFilterDef(String frmPropName) throws UnifyException {
        return getRootAppletDef().getFilterDef(getRootAppletDef().getPropValue(String.class, frmPropName));
    }

    protected FormDef getRootAppletFormDef(String frmPropName) throws UnifyException {
        return getFormDef(getRootAppletDef(), frmPropName);
    }

    protected FormDef getFormDef(AppletDef appletDef, String frmPropName) throws UnifyException {
        String _formName = appletDef.getPropValue(String.class, frmPropName);
        if (_formName != null) {
            return au.getFormDef(_formName);
        }

        return null;
    }

    protected AssignmentPageDef getAssignmentPageDef(AppletDef appletDef, String frmPropName) throws UnifyException {
        return au.getAssignmentPageDef(appletDef.getPropValue(String.class, frmPropName));
    }

    protected PropertyRuleDef getPropertyRuleDef(AppletDef appletDef, String frmPropName) throws UnifyException {
        return au.getPropertyRuleDef(appletDef.getPropValue(String.class, frmPropName));
    }
}
