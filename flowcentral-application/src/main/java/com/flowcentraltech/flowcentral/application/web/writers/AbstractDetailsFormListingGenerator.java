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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.Collections;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Convenient abstract base class for details form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsFormListingGenerator extends AbstractFormListingGenerator
        implements DetailsFormListingGenerator {

    @Override
    public final int getOptionFlagsOverride(ValueStore formBeanValueStore) throws UnifyException {
        return getOptionFlagsOverride((DetailsFormListing) formBeanValueStore.getValueObject());
    }

    @Override
    protected Set<ListingColorType> getPausePrintColors() throws UnifyException {
        return Collections.emptySet();
    }

    @Override
    protected final void doGenerate(ValueStore formBeanValueStore, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        doGenerate((DetailsFormListing) formBeanValueStore.getValueObject(), listingProperties, writer);
    }

    @Override
    protected final void generateReportHeader(ValueStore formBeanValueStore, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        generateReportHeader((DetailsFormListing) formBeanValueStore.getValueObject(), properties, writer);
    }

    @Override
    protected final void generateReportAddendum(ValueStore formBeanValueStore, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        generateReportAddendum((DetailsFormListing) formBeanValueStore.getValueObject(), properties, writer);
    }

    @Override
    protected final void generateReportFooter(ValueStore formBeanValueStore, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        generateReportFooter((DetailsFormListing) formBeanValueStore.getValueObject(), properties, writer);
    }

    protected abstract int getOptionFlagsOverride(DetailsFormListing detailsFormListing) throws UnifyException;

    protected abstract void doGenerate(DetailsFormListing detailsFormListing, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void generateReportHeader(DetailsFormListing detailsFormListing,
            ListingReportProperties properties, ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void generateReportAddendum(DetailsFormListing detailsFormListing,
            ListingReportProperties properties, ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void generateReportFooter(DetailsFormListing detailsFormListing,
            ListingReportProperties properties, ListingGeneratorWriter writer) throws UnifyException;

}
