/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Entity table widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitytable")
public class EntityTableWidget extends AbstractTableWidget<EntityTable, Entity, Restriction> {

    public EntityTableWidget() {
        super(EntityTable.class, Entity.class);
    }

    public Long[] getSelectedItemIds() throws UnifyException {
        List<Integer> selected = getSelected();
        if (selected != null && !selected.isEmpty()) {
            Long[] ids = new Long[selected.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = (Long) getItem(selected.get(i)).getId();
            }

            return ids;
        }

        return DataUtils.ZEROLEN_LONG_ARRAY;
    }

}
