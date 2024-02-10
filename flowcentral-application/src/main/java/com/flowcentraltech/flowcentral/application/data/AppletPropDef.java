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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Applet property definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletPropDef {

    private String name;

    private String value;

    private List<String> valueList;

    public AppletPropDef(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public <T> T getValue(Class<T> dataClazz) throws UnifyException {
        return DataUtils.convert(dataClazz, value);
    }

    public List<String> getValueList() {
        if (valueList == null) {
            synchronized (this) {
                if (valueList == null) {
                    if (!StringUtils.isBlank(value)) {
                        valueList = new ArrayList<String>();
                        String[] vals = StringUtils.commaSplit(value);
                        for (int i = 0; i < vals.length; i++) {
                            valueList.add(vals[i].trim());
                        }
                    } else {
                        valueList = Collections.emptyList();
                    }
                }
            }
        }
        
        return valueList;
    }

    @Override
    public String toString() {
        return "AppletPropDef [name=" + name + ", value=" + value + ", valueList=" + valueList + "]";
    }
}
