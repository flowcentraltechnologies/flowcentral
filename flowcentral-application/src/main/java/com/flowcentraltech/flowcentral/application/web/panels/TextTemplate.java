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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequence;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Text template object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TextTemplate {

    private final TokenSequence tokenSequence;

    private final ValueStore formValueStore;

    private final String fieldName;

    public TextTemplate(TokenSequence tokenSequence, ValueStore formValueStore, String fieldName) {
        this.tokenSequence = tokenSequence;
        this.formValueStore = formValueStore;
        this.fieldName = fieldName;
    }

    public TokenSequence getTokenSequence() {
        return tokenSequence;
    }

    public void clear() throws UnifyException {
        tokenSequence.clear();
    }

    public void set() throws UnifyException {
        formValueStore.store(fieldName, tokenSequence.getPreview());
    }
}
