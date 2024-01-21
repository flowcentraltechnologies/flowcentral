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
package com.flowcentraltech.flowcentral.integration.data;

import com.flowcentraltech.flowcentral.configuration.constants.DirectionType;
import com.tcdng.unify.core.data.Listable;

/**
 * End-point path definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EndpointPathDef implements Listable {

    private DirectionType direction;

    private String name;

    private String description;

    private String path;

    public EndpointPathDef(DirectionType direction, String name, String description, String path) {
        this.direction = direction;
        this.name = name;
        this.description = description;
        this.path = path;
    }

    @Override
    public String getListKey() {
        return name;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

}
