/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Usage object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Usage {

    private String type;

    private String usedByType;

    private String usedBy;

    private String usedFor;

    private String usage;

    public Usage(UsageType type, String usedByType, String usedBy, String usedFor,
            String usage) {
        this.type = type.toString();
        this.usedByType = usedByType;
        this.usedBy = usedBy;
        this.usedFor = usedFor;
        this.usage = usage;
    }

    public String getType() {
        return type;
    }

    public String getUsedByType() {
        return usedByType;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public String getUsedFor() {
        return usedFor;
    }

    public String getUsage() {
        return usage;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }
}
