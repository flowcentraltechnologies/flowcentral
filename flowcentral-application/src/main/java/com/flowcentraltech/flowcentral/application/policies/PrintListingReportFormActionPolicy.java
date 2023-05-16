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

package com.flowcentraltech.flowcentral.application.policies;

import com.flowcentraltech.flowcentral.application.web.writers.FormListingGenerator;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.Report;

/**
 * Form action policy for printing listing reports.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "*" })
@Component(name = "printlistingreport-actionpolicy",
        description = "$m{application.entityactionpolicy.printlistingreport}")
public class PrintListingReportFormActionPolicy extends AbstractApplicationFormActionPolicy {

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = new EntityActionResult(ctx);
        if (ctx.isWithListingGenerator()) {
            FormListingGenerator generator = (FormListingGenerator) getComponent(ctx.getListingGenerator());
            final ValueStoreReader reader = new BeanValueStore(ctx.getInst()).getReader();
            final int optionFlags = generator.getOptionFlagsOverride(reader);
            FormListingOptions options = optionFlags == 0 ? ctx.getListingOptions()
                    : new FormListingOptions(ctx.getListingOptions().getFormActionName(), optionFlags);
            Report report = generator.generateHtmlReport(reader, options);
            ctx.setResult(report);
            result = new EntityActionResult(ctx);
            result.setDisplayListingReport(true);
        }

        return result;
    }

}
