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

import java.util.List;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.list.AbstractListParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Listable parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListablesParams extends AbstractListParam {

    private List<? extends Listable> listables;

    public ListablesParams(List<? extends Listable> listables) {
        this.listables = listables;
    }

    public List<? extends Listable> getListables() {
        return listables;
    }

    @Override
    public boolean isPresent() {
        return !DataUtils.isBlank(listables);
    }
}
