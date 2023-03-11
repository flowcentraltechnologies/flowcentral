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

import com.tcdng.unify.core.list.AbstractListParam;

/**
 * Studio entity form parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioEntityFieldFormParams extends AbstractListParam {

    private String entity;

    private String name;

    private String dataType;

    public StudioEntityFieldFormParams(String entity, String name, String dataType) {
        this.entity = entity;
        this.name = name;
        this.dataType = dataType;
    }

    public String getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean isPresent() {
        return entity != null && name != null && dataType != null;
    }

}
