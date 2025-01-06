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

import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.LongParam;

/**
 * Studio entity series field list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioseriesentityfieldlist")
public class StudioEntitySeriesFieldListCommand extends AbstractApplicationListCommand<LongParam> {

    public StudioEntitySeriesFieldListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam params) throws UnifyException {
        if (params.isPresent()) {
            return application().findAppEntityFields((AppEntityFieldQuery) new AppEntityFieldQuery()
                    .appEntityId(params.getValue())
                    .dataTypeIn(EntityFieldDataType.DECIMAL, EntityFieldDataType.DOUBLE, EntityFieldDataType.FLOAT,
                            EntityFieldDataType.INTEGER, EntityFieldDataType.LONG, EntityFieldDataType.SHORT)
                    .nameNotIn("versionNo", "originalCopyId")
                    .addSelect("id", "name", "label").addOrder("name"));
        }

        return Collections.emptyList();
    }

}
