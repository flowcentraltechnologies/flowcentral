/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.system.web.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.business.LicenseProvider;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.system.constants.SystemLoadLicenseTaskConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.data.DownloadFile;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Licensing page controller.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("/system/licensing")
@UplBinding("web/system/upl/licensingpage.upl")
@ResultMappings({
        @ResultMapping(name = "showlicenserequest", response = { "!showpopupresponse popup:$s{generateRequestPopup}" }),
        @ResultMapping(name = "showloadlicense", response = { "!showpopupresponse popup:$s{loadLicensePopup}" }) })
public class LicensingPageController extends AbstractSystemPageController<LicensingPageBean> {

    @Configurable
    private LicenseProvider licenseProvider;

    public LicensingPageController() {
        super(LicensingPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setLicenseProvider(LicenseProvider licenseProvider) {
        this.licenseProvider = licenseProvider;
    }

    @Action
    public String prepareGenerateLicenseRequest() throws UnifyException {
        LicensingPageBean pageBean = getPageBean();
        pageBean.setFormAccountNo(null);
        pageBean.setFormClientTitle(null);
        return "showlicenserequest";
    }

    @Action
    public String generateLicenseRequest() throws UnifyException {
        LicensingPageBean pageBean = getPageBean();
        Date requestDt = getSystemModuleService().getNow();
        byte[] license = licenseProvider.generateLicenseRequest(pageBean.getFormClientTitle(),
                pageBean.getFormAccountNo(), requestDt);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        DownloadFile downloadFile = new DownloadFile(MimeType.TEXT,
                "licenserequest_" + dateFormat.format(requestDt) + ".txt", license);
        return fileDownloadResult(downloadFile, true); // Hide popup too
    }

    @Action
    public String prepareLoadLicense() throws UnifyException {
        LicensingPageBean pageBean = getPageBean();
        pageBean.setLicenseFile(null);
        return "showloadlicense";
    }

    @Action
    public String loadLicense() throws UnifyException {
        LicensingPageBean pageBean = getPageBean();
        if (pageBean.getLicenseFile() == null) {
            hintUser(MODE.ERROR, "$m{system.loadlicense.expectedfile}");
            return noResult();
        }

        TaskSetup taskSetup = TaskSetup.newBuilder()
                .addTask(SystemLoadLicenseTaskConstants.LOADLICENSE_TASK_NAME)
                .setParam(SystemLoadLicenseTaskConstants.LOADLICENSE_UPLOAD_FILE, pageBean.getLicenseFile())
                .logMessages()
                .build();
        return launchTaskWithMonitorBox(taskSetup, resolveSessionMessage("$m{system.loadlicense}"),
                "/system/licensing/openPage", null);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        LicensingPageBean pageBean = getPageBean();
        pageBean.setLicenseDef(getSystemModuleService().getInstanceLicensing());
        boolean isEnterprise = "Enterprise".equalsIgnoreCase(getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALLATION_TYPE));
        setPageWidgetVisible("licenceRequestBtn", isEnterprise);
        setPageWidgetVisible("loadLicenceBtn", isEnterprise);
    }

}
