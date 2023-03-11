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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.util.RefDecodedValue;
import com.flowcentraltech.flowcentral.application.util.RefEncodingUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for entity description generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityDescriptionGenerator extends AbstractFieldSetValueGenerator {

    private String refLongName;

    private String srcFieldName;

    public AbstractEntityDescriptionGenerator(String refLongName, String srcFieldName) {
        this.refLongName = refLongName;
        this.srcFieldName = srcFieldName;
    }

    public AbstractEntityDescriptionGenerator(String srcFieldName) {
        this.srcFieldName = srcFieldName;
    }

    @Override
    public final Object generate(EntityDef entityDef, ValueStore valueStore, String rule) throws UnifyException {
        logDebug("Generating description for refLongName [{0}] and srcFieldName [{1}]...", refLongName, srcFieldName);
        String _refLongName = null;
        Long _instId = null;
        if (!StringUtils.isBlank(refLongName)) {
            // Resolved target
            _refLongName = refLongName;
            _instId = valueStore.retrieve(Long.class, srcFieldName);
        } else {
            // Unresolved target
            String encRef = valueStore.retrieve(String.class, srcFieldName);
            RefDecodedValue decRef = RefEncodingUtils.decodeRefValue(encRef);
            if (decRef != null) {
                _refLongName = decRef.getRefLongName();
                if (!StringUtils.isBlank((String) decRef.getValue())) {
                    _instId = Long.valueOf((String) decRef.getValue());
                }
            }
        }

        if (_refLongName != null && _instId != null) {
            return application().getEntityDescriptionByRef(_refLongName, _instId);
        }

        return null;
    }

}
