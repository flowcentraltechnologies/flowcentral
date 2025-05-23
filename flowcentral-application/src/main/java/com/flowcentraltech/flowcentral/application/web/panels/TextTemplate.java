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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequence;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Text template object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TextTemplate {

    private final TokenSequence tokenSequence;

    private final ValueStore formValueStore;

    private final String fieldName;

    private String errorMsg;

    public TextTemplate(TokenSequence tokenSequence, ValueStore formValueStore, String fieldName) {
        this.tokenSequence = tokenSequence;
        this.formValueStore = formValueStore;
        this.fieldName = fieldName;
    }

    public TokenSequence getTokenSequence() {
        return tokenSequence;
    }

    public void clear() throws UnifyException {
        errorMsg = null;
        tokenSequence.clear();
    }

    public TokenSequence.Error set() throws UnifyException {
        errorMsg = null;
        TokenSequence.Error error = tokenSequence.validate();
        if (error == null ) {
            formValueStore.store(fieldName, tokenSequence.getPreview());
        }
        
        return error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
