/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.listing;

import java.util.Arrays;

import com.flowcentraltech.flowcentral.application.data.ListingReportGeneratorProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.ReportPageProperties;

/**
 * Convenient abstract base class for form listing report generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractFormListingReportGenerator extends AbstractFlowCentralComponent
        implements FormListingReportGenerator {

    protected ListingReportGeneratorProperties getReportProperties(ValueStoreReader reader,
            FormListingOptions listingOptions) throws UnifyException {
        return new ListingReportGeneratorProperties(
                ReportPageProperties.newBuilder().resourceBaseUri(getSessionContext().getUriBase()).build(),
                Arrays.asList(new ListingReportProperties(
                        listingOptions.isWithOptionsName() ? listingOptions.getOptionsName() : "default_prop")));
    }

    protected abstract void writeReportHeader(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void writeReportAddendum(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void writeReportFooter(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException;
}
