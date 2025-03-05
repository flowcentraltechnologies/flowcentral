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
package com.flowcentraltech.flowcentral.notification.senders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.web.data.DetailsCase;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.common.data.GenerateListingReportOptions;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Convenient abstract base class for notification alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotificationAlertSender extends AbstractFlowCentralComponent
        implements NotificationAlertSender {

    private final Formats DEFAULT_REPORT_FORMATS = new Formats("###,##0;(###,##0)", "###,##0.00;(###,##0.00)",
            "dd-MM-yyyy", "dd-MM-yyyy");

    @Configurable
    private NotificationModuleService notificationModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final NotificationModuleService notification() {
        return notificationModuleService;
    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final <T extends NotifTemplateWrapper> T getTemplateWrapper(Class<T> wrapperType) throws UnifyException {
        return notification().wrapperOfNotifTemplate(wrapperType);
    }

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> wrapperType)
            throws UnifyException {
        return notification().wrapperOfNotifLargeText(wrapperType);
    }

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> wrapperType,
            Map<String, Object> parameters) throws UnifyException {
        return notification().wrapperOfNotifLargeText(wrapperType, parameters);
    }
    
    /**
     * Generates notification attachments using reader.
     * 
     * @param reader
     *               the backing value store reader
     * @return the list of generated attachments
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract List<Attachment> generateAttachments(ValueStoreReader reader) throws UnifyException;

    protected Attachment createPdfAttachmentFromListing(String fileName, ValueStoreReader reader, String generator,
            FormListingOptions options) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(reader, generator, options);
        return Attachment.newBuilder(FileAttachmentType.PDF, false).fileName(fileName).title(fileName).name(fileName)
                .data(report).build();
    }

    protected Attachment createPdfAttachmentFromListing(String fileName, ValueStoreReader reader,
            List<GenerateListingReportOptions> options) throws UnifyException {
        final byte[] report = appletUtilities.generateViewListingReportAsByteArray(reader, options);
        return Attachment.newBuilder(FileAttachmentType.PDF, false).fileName(fileName).title(fileName).name(fileName)
                .data(report).build();
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList) throws UnifyException {
        return createPdfAttachmentFromDetailListing(fileName, reader, tableName, dataList,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap(),
                DEFAULT_REPORT_FORMATS);
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList, String detailsListingGenerator,
            Map<String, Object> properties, Formats formats) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, reader, tableName, dataList, detailsListingGenerator,
                properties, formats, false);
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            List<DetailsCase> detailsCaseList, int columns) throws UnifyException {
        return createPdfAttachmentFromDetailListing(fileName, reader, detailsCaseList, columns,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap());
    }

    protected Attachment createPdfAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            List<DetailsCase> detailsCaseList, int columns, String detailsListingGenerator,
            Map<String, Object> properties) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, reader, detailsCaseList, detailsListingGenerator,
                properties, columns, false);
    }

    protected Attachment createPdfAttachmentFromLetterListing(String fileName, ValueStoreReader reader,
            String letterGenerator) throws UnifyException {
        final byte[] report = appletUtilities.generateLetterListingReportAsByteArray(reader, letterGenerator);
        return Attachment.newBuilder(FileAttachmentType.PDF, false).fileName(fileName).title(fileName).name(fileName)
                .data(report).build();
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList) throws UnifyException {
        return createExcelAttachmentFromDetailListing(fileName, reader, tableName, dataList,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap(),
                DEFAULT_REPORT_FORMATS);
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList, String detailsListingGenerator,
            Map<String, Object> properties, Formats formats) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, reader, tableName, dataList, detailsListingGenerator,
                properties, formats, true);
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            List<DetailsCase> detailsCaseList, int columns) throws UnifyException {
        return createExcelAttachmentFromDetailListing(fileName, reader, detailsCaseList, columns,
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap());
    }

    protected Attachment createExcelAttachmentFromDetailListing(String fileName, ValueStoreReader reader,
            List<DetailsCase> detailsCaseList, int columns, String detailsListingGenerator,
            Map<String, Object> properties) throws UnifyException {
        return createAttachmentFromDetailListingReport(fileName, reader, detailsCaseList, detailsListingGenerator,
                properties, columns, true);
    }

    protected Attachment createAttachmentFromFile(FileAttachmentType type, String absoluteFileName)
            throws UnifyException {
        final String fileName = IOUtils.getActualFileName(absoluteFileName);
        final byte[] file = IOUtils.readFileResourceInputStream(absoluteFileName);
        return Attachment.newBuilder(type, false).fileName(fileName).title(fileName).name(fileName).data(file).build();
    }

    private Attachment createAttachmentFromDetailListingReport(String fileName, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList, String detailsListingGenerator,
            Map<String, Object> properties, Formats formats, boolean spreadSheet) throws UnifyException {
        final byte[] report = appletUtilities.generateDetailListingReportAsByteArray(reader, tableName, dataList,
                detailsListingGenerator, properties, formats, spreadSheet);
        return Attachment.newBuilder(spreadSheet ? FileAttachmentType.EXCEL : FileAttachmentType.PDF, false).fileName(fileName)
                .title(fileName).name(fileName).data(report).build();
    }

    private Attachment createAttachmentFromDetailListingReport(String fileName, ValueStoreReader reader,
            List<DetailsCase> detailsCaseList, String detailsListingGenerator, Map<String, Object> properties,
            int columns, boolean asSpreadSheet) throws UnifyException {
        final byte[] report = appletUtilities.generateDetailListingReportAsByteArray(reader, detailsCaseList,
                detailsListingGenerator, properties, columns, asSpreadSheet);
        return Attachment.newBuilder(asSpreadSheet ? FileAttachmentType.EXCEL : FileAttachmentType.PDF, false)
                .fileName(fileName).title(fileName).name(fileName).data(report).build();
    }
}
