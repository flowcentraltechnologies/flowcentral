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

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationImportDataTaskConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Data import applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-dataimportappletpanel")
@UplBinding("web/application/upl/dataimportappletpanel.upl")
public class DataImportAppletPanel extends AbstractAppletPanel {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    public void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    @Action
    public void executeImport() throws UnifyException {
        DataImportApplet applet = getDataImportApplet();
        if (applet.getUploadFile() == null) {
            hintUser(MODE.ERROR, "$m{dataimportappletpanel.dataimport.error}");
            return;
        }

        AppletDef appletDef = applicationModuleService.getAppletDef(applet.getAppletName());
        TaskSetup taskSetup = TaskSetup.newBuilder().addTask(ApplicationImportDataTaskConstants.IMPORTDATA_TASK_NAME)
                .setParam(ApplicationImportDataTaskConstants.IMPORTDATA_ENTITY, appletDef.getEntity())
                .setParam(ApplicationImportDataTaskConstants.IMPORTDATA_UPLOADCONFIG,
                        appletDef.getPropDef(AppletPropertyConstants.IMPORTDATA_CONFIGNAME).getValue(String.class))
                .setParam(ApplicationImportDataTaskConstants.IMPORTDATA_UPLOAD_FILE, applet.getUploadFile())
                .setParam(ApplicationImportDataTaskConstants.IMPORTDATA_WITH_HEADER_FLAG, applet.isHasHeader())
                .logMessages()
                .build();
        String routeToAppletName = appletDef.isProp(AppletPropertyConstants.IMPORTDATA_ROUTETO_APPLETNAME)
                ? appletDef.getPropDef(AppletPropertyConstants.IMPORTDATA_ROUTETO_APPLETNAME).getValue(String.class)
                : null;
        String onSuccessPath = routeToAppletName != null
                ? applicationModuleService.getAppletDef(routeToAppletName).getOpenPath()
                : null;
        launchTaskWithMonitorBox(taskSetup, appletDef.getDescription(), onSuccessPath, null);
    }

    protected DataImportApplet getDataImportApplet() throws UnifyException {
        return getValue(DataImportApplet.class);
    }
}
