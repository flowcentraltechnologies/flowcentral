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

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.LongFormDef;

/**
 * Long form.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LongForm {

    private LongFormDef longFormDef;
    
    private List<LongFormElement> elements;

    public LongForm(LongFormDef longFormDef, List<LongFormElement> elements) {
        this.longFormDef = longFormDef;
        this.elements = elements;
    }

    public LongFormDef getLongFormDef() {
        return longFormDef;
    }

    public List<LongFormElement> getElements() {
        return elements;
    }

}
