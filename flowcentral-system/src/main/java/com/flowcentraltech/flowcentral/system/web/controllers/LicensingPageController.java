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

package com.flowcentraltech.flowcentral.system.web.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.business.LicenseProvider;
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
 * @author FlowCentral Technologies Limited
 * @since 4.1
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
        Date requestDt = system().getNow();
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

        TaskSetup taskSetup = TaskSetup.newBuilder(SystemLoadLicenseTaskConstants.LOADLICENSE_TASK_NAME)
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
        pageBean.setLicenseDef(system().getInstanceLicensing());
        boolean isEnterprise = isEnterprise();
        setPageWidgetVisible("licenceRequestBtn", isEnterprise);
        setPageWidgetVisible("loadLicenceBtn", isEnterprise);
    }

}
