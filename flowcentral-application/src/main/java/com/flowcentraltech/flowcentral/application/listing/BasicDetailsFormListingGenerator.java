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
package com.flowcentraltech.flowcentral.application.listing;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Basic details form listing generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR)
public class BasicDetailsFormListingGenerator extends AbstractDetailsFormListingGenerator {

    @Override
    public int getOptionFlagsOverride(ValueStoreReader reader) throws UnifyException {
        return 0;
    }

    @Override
    protected void writeReportHeader(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        
    }

    @Override
    protected void writeReportAddendum(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        
    }

    @Override
    protected void writeReportFooter(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        
    }

}
