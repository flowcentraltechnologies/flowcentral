/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Document;

/**
 * Convenient abstract base class for applet action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractAppletActionPolicy extends AbstractEntityActionPolicy implements AppletActionPolicy {
    
    @SuppressWarnings("unchecked")
    protected <U> U getDocumentAttribute(Class<U> clazz, String name) throws UnifyException {
        Document document = getRequestContextUtil().getRequestDocument();
        if (document != null) {
            return (U) document.getAttribute(name);
        }
        
        return null;
    }

}
