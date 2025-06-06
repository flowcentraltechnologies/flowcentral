/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.LongParam;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Studio work entity by name list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studioworkentitybynamelist")
public class StudioWorkEntityByNameListCommand extends AbstractApplicationListCommand<LongParam> {

    public StudioWorkEntityByNameListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam params) throws UnifyException {
        final Long applicationId = params.isPresent() ? params.getValue()
                : (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        if (QueryUtils.isValidLongCriteria(applicationId)) {
            AppEntityQuery query = new AppEntityQuery();
            query.applicationId(applicationId);
            query.isWorkEntity();
            query.addSelect("name", "description");
            List<AppEntity> appEntityList = application().findAppEntities(query);
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
