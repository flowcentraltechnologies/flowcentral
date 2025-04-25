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

package com.flowcentraltech.flowcentral.report.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.report.entities.ReportParameter;
import com.flowcentraltech.flowcentral.report.entities.ReportParameterQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.LongParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Report parameter list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("reportparameterlist")
public class ReportParameterListCommand extends AbstractReportListCommand<LongParam> {

    public ReportParameterListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam longParams) throws UnifyException {
        if (longParams.isPresent()) {
            List<ReportParameter> list = getReportModuleService()
                    .findReportParameters((ReportParameterQuery) new ReportParameterQuery()
                            .reportConfigurationId(longParams.getValue()).addSelect("name", "description"));
            if (!DataUtils.isBlank(list)) {
                List<ListData> resultList = new ArrayList<ListData>();
                for (ReportParameter reportParameter : list) {
                    resultList.add(new ListData(reportParameter.getName(), reportParameter.getDescription()));
                }
                return resultList;
            }
        }

        return Collections.emptyList();
    }

}
