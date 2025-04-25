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

package com.flowcentraltech.flowcentral.common.data;

import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for context object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractContext {

    private Map<String, Object> attributes;

    public void setAll(AbstractContext ctx) {
        if (ctx.attributes != null) {
            if (attributes == null) {
                attributes = new HashMap<String, Object>();
            }

            attributes.putAll(ctx.attributes);
        }
    }

    public void setAttribute(String name, Object val) {
        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }

        attributes.put(name, val);
    }

    public <T> T getAttribute(Class<T> clazz, String name) throws UnifyException {
        if (attributes != null) {
            return DataUtils.convert(clazz, attributes.get(name));
        }

        return null;
    }
}
