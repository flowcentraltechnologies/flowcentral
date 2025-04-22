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

package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.entities.AppAPI;
import com.flowcentraltech.flowcentral.application.entities.AppAPIQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.APIType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * REST API list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("restapilist")
public class RestAPIListCommand extends AbstractApplicationListCommand<ZeroParams> {

    public RestAPIListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams zeroParams) throws UnifyException {
        List<AppAPI> appApiList = application().findAppAPIs((AppAPIQuery) new AppAPIQuery().type(APIType.REST_CRUD)
                .addSelect("applicationName", "name", "description").addOrder("description"));
        if (!DataUtils.isBlank(appApiList)) {
            List<ListData> list = new ArrayList<ListData>();
            for (AppAPI appAPI : appApiList) {
                list.add(new ListData(
                        ApplicationNameUtils.getApplicationEntityLongName(appAPI.getApplicationName(), appAPI.getName()),
                        appAPI.getDescription()));
            }

            return list;
        }

        return Collections.emptyList();
    }

}
