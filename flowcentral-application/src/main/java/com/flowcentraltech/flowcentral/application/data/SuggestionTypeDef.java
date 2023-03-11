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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;

/**
 * Suggestion type definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SuggestionTypeDef extends BaseApplicationEntityDef {

    private String parent;
    
    public SuggestionTypeDef(String parent, String longName, String description, Long id,
            long versionNo) throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, versionNo);
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public boolean isWithParent() {
        return parent != null;
    }

}
