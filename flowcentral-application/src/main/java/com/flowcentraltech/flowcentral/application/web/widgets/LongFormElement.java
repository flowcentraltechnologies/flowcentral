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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.constants.LongFormElementType;

/**
 * Long form element.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class LongFormElement {

    private final LongFormElementType type;

    private final String appletName;

    private Object valObject;

    public LongFormElement(LongFormElementType type, String appletName, Object valObject) {
        this.type = type;
        this.appletName = appletName;
        this.valObject = valObject;
    }

    public Object getValObject() {
        return valObject;
    }

    public void setValObject(Object valObject) {
        this.valObject = valObject;
    }

    public LongFormElementType getType() {
        return type;
    }

    public String getAppletName() {
        return appletName;
    }
        
}
