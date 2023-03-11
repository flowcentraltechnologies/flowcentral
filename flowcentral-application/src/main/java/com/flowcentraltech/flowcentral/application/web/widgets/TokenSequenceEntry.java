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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.tcdng.unify.common.util.StringTokenType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Token sequence entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TokenSequenceEntry extends FieldSequenceEntry {
    
    private StringTokenType tokenType;

    public TokenSequenceEntry(EntityDef entityDef, boolean editable) {
        super(entityDef, editable);
    }

    public StringTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(StringTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isWithTokenType() {
        return tokenType != null;
    }

    public void normalize() throws UnifyException {
        if (tokenType == null) {
            setFieldName(null);
            setParam(null);
        } else {
            switch(tokenType) {
                case FORMATTED_PARAM:
                    if (getFieldName() == null) {
                        setParam(null);;
                    }
                    break;
                case GENERATOR_PARAM:
                    setFieldName(null);
                    break;
                case NEWLINE:
                    setFieldName(null);
                    setParam(null);
                    break;
                case PARAM:
                    setParam(null);
                    break;
                case TEXT:
                    setFieldName(null);
                    break;
                default:
                    break;
                
            }
        }
        
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
