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
package com.flowcentraltech.flowcentral.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Entity utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class EntityUtils {

    private EntityUtils() {

    }

    public static List<? extends Listable> getListablesFromEntityList(List<? extends Entity> entityList, String key,
            String label) throws UnifyException {
        if (!DataUtils.isBlank(entityList)) {
            List<ListData> resultList = new ArrayList<ListData>();
            ValueStore valueStore = new BeanValueListStore(entityList);
            final int len = valueStore.size();
            for (int i = 0; i < len; i++) {
                valueStore.setDataIndex(i);
                resultList.add(
                        new ListData(valueStore.retrieve(String.class, key), valueStore.retrieve(String.class, label)));
            }
            
            DataUtils.sortAscending(resultList, ListData.class, "listDescription");
            return resultList;
        }

        return Collections.emptyList();
    }
}
