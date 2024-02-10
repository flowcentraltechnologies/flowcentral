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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Studio entity by name list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioentitybynamelist")
public class StudioEntityByNameListCommand extends AbstractApplicationListCommand<ZeroParams> {

    public StudioEntityByNameListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams zeroParams) throws UnifyException {
        final Long applicationId = (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        if (QueryUtils.isValidLongCriteria(applicationId)) {
            List<AppEntity> appEntityList = application()
                    .findAppEntities((AppEntityQuery) new AppEntityQuery().applicationId(applicationId)
                            .addSelect("name", "description").addOrder("description"));
            if (!DataUtils.isBlank(appEntityList)) {
                final String applicationName = (String) getSessionAttribute(
                        StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
                List<ListData> list = new ArrayList<ListData>();
                for (AppEntity appEntity : appEntityList) {
                    list.add(new ListData(
                            ApplicationNameUtils.getApplicationEntityLongName(applicationName, appEntity.getName()),
                            appEntity.getDescription()));
                }

                return list;
            }
        }

        return Collections.emptyList();
    }

}
