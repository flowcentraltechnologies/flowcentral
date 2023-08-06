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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.entities.AppAppletAlert;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.LongParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio applet alert list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioappletalertlist")
public class StudioAppletAlertListCommand extends AbstractApplicationListCommand<LongParam> {

    public StudioAppletAlertListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam longParam) throws UnifyException {
        if (longParam.isPresent()) {
            List<AppAppletAlert> alertList = application().findAppAppletAlerts(longParam.getValue());
            if (!DataUtils.isBlank(alertList)) {
                List<ListData> list = new ArrayList<ListData>();
                for (AppAppletAlert alert : alertList) {
                    list.add(new ListData(alert.getName(), alert.getDescription()));
                }
                return list;
            }
        }

        return Collections.emptyList();
    }

}
