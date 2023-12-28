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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.entities.ReportableFieldQuery;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Studio on update application entity field policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "application.appEntityField" })
@Component("studioonupdateappentityfield-policy")
public class StudioOnUpdateAppEntityFieldPolicy extends AbstractStudioAppEntityFieldPolicy {

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        final AppEntityField appEntityField = (AppEntityField) ctx.getInst();
        // Update Reportable
        Long reportableDefinitionId = getReportableDefinitionId(appEntityField);
        if (appEntityField.isReportable() && appEntityField.getDataType().isTableViewable()) {
            if (QueryUtils.isValidLongCriteria(reportableDefinitionId)) {
                ReportableField oldReportableField = getRds().findReportableField(
                        new ReportableFieldQuery().reportableId(reportableDefinitionId).name(appEntityField.getName()));
                if (oldReportableField == null) {
                    ReportableField reportableField = new ReportableField();
                    reportableField.setReportableId(reportableDefinitionId);
                    ReportEntityUtils.populateReportableField(reportableField, appEntityField,
                            FormatterOptions.DEFAULT);
                    getRds().createReportableField(reportableField);
                } else {
                    ReportEntityUtils.populateReportableField(oldReportableField, appEntityField,
                            FormatterOptions.DEFAULT);
                    getRds().updateReportableField(oldReportableField);
                }
            }
        } else {
            if (QueryUtils.isValidLongCriteria(reportableDefinitionId)) {
                getRds().deleteReportableField(
                        new ReportableFieldQuery().reportableId(reportableDefinitionId).name(appEntityField.getName()));
            }
        }

        // TODO Auditable, tables and forms
        return new EntityActionResult(ctx);
    }

}
