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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Token sequence object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TokenSequence {

    private final EntityDef entityDef;

    private String preview;

    public TokenSequence(EntityDef entityDef) {
        this.entityDef = entityDef;
    }

    public TokenSequence(EntityDef entityDef, String parameterizedString) {
        this.entityDef = entityDef;
        this.preview = parameterizedString;
    }

    public void clear() throws UnifyException {
        preview = null;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    public Error validate() {
        if (preview != null) {
            try {
                for (StringToken token : StringUtils.breakdownParameterizedString(preview)) {
                    if (token.isParam() && !entityDef.isField(token.getToken())) {
                        return new Error("$m{tokensequence.unknown.parameter}", token.getToken());
                    }
                }
            } catch (Exception e) {
                return new Error("$m{tokensequence.invalid}");
            }
        }

        return null;
    }

    public static class Error {

        private String msg;

        private Object[] params;

        public Error(String msg, Object... params) {
            this.msg = msg;
            this.params = params;
        }

        public String getMsg() {
            return msg;
        }

        public Object[] getParams() {
            return params;
        }
    }
}
