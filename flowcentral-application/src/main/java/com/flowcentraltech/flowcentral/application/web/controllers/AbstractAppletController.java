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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.AppletPageAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.listing.DetailsFormListingGenerator;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.web.data.DetailsFormListing;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractApplet;
import com.flowcentraltech.flowcentral.common.business.SecuredLinkManager;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralResultMappingConstants;
import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.common.data.SecuredLinkContentInfo;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Convenient abstract base class for applet controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@ResultMappings({
    @ResultMapping(name = "refreshapplet",
            response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{appletPanel}" }),
    @ResultMapping(name = ApplicationResultMappingConstants.OPEN_IN_NEW_BROWSER_WINDOW,
            response = { "!openinbrowserwindowresponse" }) })
public abstract class AbstractAppletController<T extends AbstractAppletPageBean<? extends AbstractApplet>>
        extends AbstractFlowCentralPageController<T> {

    private static final String RELAY_SESSION_OBJECT = "applet.RELAY_SESSION_OBJECT";

    @Configurable
    private AppletUtilities appletUtilities;

    public AbstractAppletController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    protected final AppletUtilities au() {
        return appletUtilities;
    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final SystemModuleService system() {
        return appletUtilities.system();
    }

    protected void setPageTitle(AbstractApplet applet) throws UnifyException {
        if (applet != null) {
            setPageTitle(applet.getAppletDescription());
        }
    }

    protected String showPopup(String resultMapping, String backingBeanKey, Object backingBean) throws UnifyException {
        setSessionAttribute(backingBeanKey, backingBean);
        return resultMapping;
    }

    protected final void setReloadOnSwitch() throws UnifyException {
        appletUtilities.setReloadOnSwitch();
    }

    protected final boolean clearReloadOnSwitch() throws UnifyException {
        return appletUtilities.clearReloadOnSwitch();
    }

    protected final boolean isReloadOnSwitch() throws UnifyException {
        return appletUtilities.isReloadOnSwitch();
    }

    protected final String refreshContent() throws UnifyException {
        return ApplicationResultMappingConstants.REFRESH_CONTENT;
    }

    protected final String openApplet(String appletName) throws UnifyException {
        return openApplet(appletName, null);
    }

    protected final String openApplet(String appletName, Object relayObject) throws UnifyException {
        AppletDef appletDef = application().getAppletDef(appletName);
        String path = null;
        if (appletDef.getPropValue(boolean.class, AppletPropertyConstants.PAGE_MULTIPLE)
                && system().getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ENABLE_VIEW_ENTITY_IN_SEPARATE_TAB)) {
            path = ApplicationPageUtils.constructMultiPageAppletOpenPagePath(appletDef.getOpenPath());
        } else {
            path = appletDef.getOpenPath();
        }

        if (relayObject != null) {
            setSessionAttribute(RELAY_SESSION_OBJECT, relayObject);
        }

        return openPath(path);
    }

    @SuppressWarnings("unchecked")
    protected <U> U getRelayObject(Class<U> type) throws UnifyException {
        return (U) removeSessionAttribute(RELAY_SESSION_OBJECT);
    }

    protected String viewListingReport(ValueStoreReader reader, DetailsFormListing listing) throws UnifyException {
        DetailsFormListingGenerator generator = (DetailsFormListingGenerator) getComponent(listing.getGenerator());
        FormListingOptions options = new FormListingOptions(listing);
        Report report = listing.isSpreadSheet() ? generator.generateExcelReport(reader, options)
                : generator.generateHtmlReport(reader, options);
        return viewListingReport(report);
    }

    protected String viewListingReport(Report report) throws UnifyException {
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORT, report);
        return FlowCentralResultMappingConstants.VIEW_LISTING_REPORT;
    }

    protected final void captureSecuredLink(SecuredLinkType type) throws UnifyException {
        final String forward = getSessionContext().getExternalForward();
        if (!StringUtils.isBlank(forward)) {
            SecuredLinkManager slm = appletUtilities.getComponent(SecuredLinkManager.class);
            SecuredLinkContentInfo securedLinkContentInfo = slm.getSecuredLink(forward);
            if (type.equals(securedLinkContentInfo.getType())) {
                getSessionContext().removeExternalForward();
                getPage().setAttribute(AppletPageAttributeConstants.SECURED_LINK_ACCESSKEY,
                        securedLinkContentInfo.getAccessKey());
            }
        }
    }

    protected final void invalidateSecuredLink(SecuredLinkType type) throws UnifyException {
        final String accessKey = getPage().getAttribute(String.class,
                AppletPageAttributeConstants.SECURED_LINK_ACCESSKEY);
        if (!StringUtils.isBlank(accessKey)) {
            SecuredLinkManager slm = appletUtilities.getComponent(SecuredLinkManager.class);
            slm.invalidateSecuredLinkByAccessKey(type, accessKey);
            getPage().removeAttribute(String.class, AppletPageAttributeConstants.SECURED_LINK_ACCESSKEY);
        }
    }
}
