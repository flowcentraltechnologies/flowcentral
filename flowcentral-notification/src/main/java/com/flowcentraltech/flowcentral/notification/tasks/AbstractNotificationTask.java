/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.notification.tasks;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.web.data.Formats;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.task.AbstractTask;

/**
 * Convenient abstract base class for notification tasks.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotificationTask extends AbstractTask {

    private final Formats DEFAULT_REPORT_FORMATS = new Formats("###,##0.00;(###,##0.00)", "dd-MM-yyyy", "dd-MM-yyyy");

    @Configurable
    private NotificationModuleService notificationModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    public final void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    protected final NotificationModuleService notification() {
        return notificationModuleService;
    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final EnvironmentService environment() {
        return appletUtilities.environment();
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, String tableName,
            List<? extends Entity> dataList) throws UnifyException {
        return createPdfAttachmentFromDetailListing(fileName, tableName, dataList,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap(),
                DEFAULT_REPORT_FORMATS);
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, String tableName,
            List<? extends Entity> dataList, String detailsListingGenerator, Map<String, Object> properties,
            Formats formats) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, tableName, dataList, detailsListingGenerator,
                properties, formats, false);
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, String tableName,
            List<? extends Entity> dataList) throws UnifyException {
        return createExcelAttachmentFromDetailListing(fileName, tableName, dataList,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap(),
                DEFAULT_REPORT_FORMATS);
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, String tableName,
            List<? extends Entity> dataList, String detailsListingGenerator, Map<String, Object> properties,
            Formats formats) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, tableName, dataList, detailsListingGenerator,
                properties, formats, true);
    }

    private Attachment createAttachmentFromDetailListingReport(String fileName, String tableName,
            List<? extends Entity> dataList, String detailsListingGenerator, Map<String, Object> properties,
            Formats formats, boolean spreadSheet) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(tableName, dataList,
                detailsListingGenerator, properties, formats, spreadSheet);
        return Attachment.newBuilder(spreadSheet ? FileAttachmentType.EXCEL : FileAttachmentType.PDF).fileName(fileName)
                .title(fileName).name(fileName).data(report).build();
    }
}
