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

import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.report.Report;

/**
 * Form listing report generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface FormListingReportGenerator extends UnifyComponent {

    /**
     * Generates form HTML report.
     * 
     * @param formBeanValueStore
     *                           the form bean value store
     * @param listingOptions
     *                           form listing options
     * @return report
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateHtmlReport(ValueStore formBeanValueStore, FormListingOptions listingOptions) throws UnifyException;
}
