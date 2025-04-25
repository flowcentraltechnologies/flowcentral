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
package com.flowcentraltech.flowcentral.workflow.util;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFilterConstants;
import com.flowcentraltech.flowcentral.application.data.EntityInstNameParts;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow entity utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class WorkflowEntityUtils {

    private WorkflowEntityUtils() {

    }

    public static String getReviewEntityInstName(String reviewEntityName, Long reviewId, String entityName, Long id) {
        if (id == null) {
            id = 0L;
        }

        return StringUtils.concatenateUsingSeparator(':', reviewEntityName, reviewId, entityName, id);
    }

    public static ReviewEntityInstNameParts getReviewEntityInstNameParts(String reviewEntityInstName) {
        String[] po = StringUtils.charSplit(reviewEntityInstName, ':');
        return new ReviewEntityInstNameParts(reviewEntityInstName, new EntityInstNameParts(po[0], Long.valueOf(po[1])),
                new EntityInstNameParts(po[2], Long.valueOf(po[3])));
    }

    public static boolean isWorkflowConditionMatched(AppletUtilities au, ValueStoreReader reader, WfDef wfDef,
            String conditionName) throws UnifyException {
        return conditionName != null && (ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME.equals(conditionName)
                || wfDef.getFilterDef(conditionName).getFilterDef()
                        .getObjectFilter(au.getEntityDef(wfDef.getEntity()), reader, au.getNow()).matchReader(reader));
    }

}
