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
import com.flowcentraltech.flowcentral.application.web.data.DetailsCase;
import com.flowcentraltech.flowcentral.application.web.data.Formats;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.task.AbstractTask;
import com.tcdng.unify.core.util.IOUtils;

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

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> notifWrapperType)
            throws UnifyException {
        return notification().wrapperOfNotifLargeText(notifWrapperType);
    }

    protected Attachment createPdfAttachmentFromListing(String fileName, ValueStoreReader reader, String generator,
            FormListingOptions options) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(reader, generator, options);
        return Attachment.newBuilder(FileAttachmentType.PDF).fileName(fileName).title(fileName).name(fileName)
                .data(report).build();
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

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, List<DetailsCase> detailsCaseList,
            int columns) throws UnifyException {
        return createPdfAttachmentFromDetailListing(fileName, detailsCaseList, columns,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap());
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, List<DetailsCase> detailsCaseList,
            int columns, String detailsListingGenerator, Map<String, Object> properties) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, detailsCaseList, detailsListingGenerator, properties,
                columns, false);
    }

    protected Attachment createPdfAttachmentFromLetterListing(String fileName, String largeTextName,
            Map<String, Object> properties) throws UnifyException {
        return createPdfAttachmentFromLetterListing(fileName,
                NotificationModuleNameConstants.BASIC_LETTERFORMLISTING_GENERATOR, largeTextName, properties);
    }

    protected Attachment createPdfAttachmentFromLetterListing(String fileName, String letterGenerator,
            String largeTextName, Map<String, Object> properties) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(letterGenerator, largeTextName,
                properties);
        return Attachment.newBuilder(FileAttachmentType.PDF).fileName(fileName).title(fileName).name(fileName)
                .data(report).build();
    }

    protected Attachment createPdfAttachmentFromLetterListing(String fileName, NotifLargeTextWrapper wrapper)
            throws UnifyException {
        return createPdfAttachmentFromLetterListing(fileName,
                NotificationModuleNameConstants.BASIC_LETTERFORMLISTING_GENERATOR, wrapper);
    }

    protected Attachment createPdfAttachmentFromLetterListing(String fileName, String letterGenerator,
            NotifLargeTextWrapper wrapper) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(letterGenerator,
                wrapper.getLargeTextName(), wrapper.getProperties());
        return Attachment.newBuilder(FileAttachmentType.PDF).fileName(fileName).title(fileName).name(fileName)
                .data(report).build();
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

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, List<DetailsCase> detailsCaseList,
            int columns) throws UnifyException {
        return createExcelAttachmentFromDetailListing(fileName, detailsCaseList, columns,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap());
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, List<DetailsCase> detailsCaseList,
            int columns, String detailsListingGenerator, Map<String, Object> properties) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, detailsCaseList, detailsListingGenerator, properties,
                columns, true);
    }

    protected Attachment createAttachmentFromFile(FileAttachmentType type, String absoluteFileName)
            throws UnifyException {
        final String fileName = IOUtils.getActualFileName(absoluteFileName);
        final byte[] file = IOUtils.readFileResourceInputStream(absoluteFileName);
        return Attachment.newBuilder(type).fileName(fileName).title(fileName).name(fileName).data(file).build();
    }

    private Attachment createAttachmentFromDetailListingReport(String fileName, String tableName,
            List<? extends Entity> dataList, String detailsListingGenerator, Map<String, Object> properties,
            Formats formats, boolean spreadSheet) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(tableName, dataList,
                detailsListingGenerator, properties, formats, spreadSheet);
        return Attachment.newBuilder(spreadSheet ? FileAttachmentType.EXCEL : FileAttachmentType.PDF).fileName(fileName)
                .title(fileName).name(fileName).data(report).build();
    }

    private Attachment createAttachmentFromDetailListingReport(String fileName, List<DetailsCase> detailsCaseList,
            String detailsListingGenerator, Map<String, Object> properties, int columns, boolean asSpreadSheet)
            throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(detailsCaseList,
                detailsListingGenerator, properties, columns, asSpreadSheet);
        return Attachment.newBuilder(asSpreadSheet ? FileAttachmentType.EXCEL : FileAttachmentType.PDF)
                .fileName(fileName).title(fileName).name(fileName).data(report).build();
    }
}
