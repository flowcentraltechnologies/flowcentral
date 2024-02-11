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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.StringParam;

/**
 * Studio reportable large text list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioreportablelargetextlist")
public class StudioReportableLargeTextListCommand extends AbstractFlowCentralListCommand<StringParam> {

    @Configurable
    private NotificationModuleService notificationModuleService;

    @Configurable
    private ReportModuleService reportModuleService;
    
    public StudioReportableLargeTextListCommand() {
        super(StringParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam params) throws UnifyException {
        if (params.isPresent()) {
            final String entity = reportModuleService.getReportableEntity(params.getValue());
            return notificationModuleService.getEntityNotifLargeTexts(entity);
        }

        return Collections.emptyList();
    }

}
