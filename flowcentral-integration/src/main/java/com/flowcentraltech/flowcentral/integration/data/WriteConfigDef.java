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

import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;

/**
 * Write configuration definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WriteConfigDef {

    private String name;

    private String writerName;

    private long version;

    private ParamValuesDef writerParamsDef;

    public WriteConfigDef(String name, String writerName, long version, ParamValuesDef writerParamsDef) {
        this.name = name;
        this.writerName = writerName;
        this.version = version;
        this.writerParamsDef = writerParamsDef;
    }

    public String getName() {
        return name;
    }

    public String getWriterName() {
        return writerName;
    }

    public long getVersion() {
        return version;
    }

    public ParamValuesDef getWriterParamsDef() {
        return writerParamsDef;
    }

}
