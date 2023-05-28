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
package com.flowcentraltech.flowcentral.notification.listing;

import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.flowcentraltech.flowcentral.application.listing.ListingGeneratorWriter;
import com.flowcentraltech.flowcentral.application.web.data.LetterFormListing;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Basic notification letter form listing generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(NotificationModuleNameConstants.BASIC_LETTERFORMLISTING_GENERATOR)
public class BasicNotificationLetterFormListingGenerator extends AbstractNotificationLetterFormListingGenerator {

    @Override
    protected void writeLetterHeader(LetterFormListing letterFormListing, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
 
    }

    @Override
    protected void writeLetterAddendum(LetterFormListing letterFormListing, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
 
    }

    @Override
    protected void writeLetterFooter(LetterFormListing letterFormListing, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
 
    }

}
