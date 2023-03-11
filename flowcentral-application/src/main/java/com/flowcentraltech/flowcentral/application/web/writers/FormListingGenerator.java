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

import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Form listing generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface FormListingGenerator extends FormListingReportGenerator {

    /**
     * Generates form listing into writer.
     * 
     * @param formBeanValueStore
     *                           the form bean value store
     * @param listingProperties
     *                           the listing properties
     * @param writer
     *                           the response writer
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateListing(ValueStore formBeanValueStore, ListingProperties listingProperties, ResponseWriter writer)
            throws UnifyException;
}
