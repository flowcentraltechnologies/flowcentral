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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.ParamValueDef;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Parameter value entries object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ParamValueEntries {

    private List<ParamValueEntry> entryList;

    public ParamValueEntries(ParamValuesDef paramValuesDef) throws UnifyException {
        entryList = Collections.emptyList();
        if (paramValuesDef != null) {
            entryList = new ArrayList<ParamValueEntry>();
            for (ParamValueDef paramValueDef : paramValuesDef.getParamValueList()) {
                ParamValueEntry pvo = new ParamValueEntry(paramValueDef.getParamConfig());
                pvo.normalize();

                AbstractInput<?> in = pvo.getParamInput();
                if (in != null) {
                    in.setStringValue(paramValueDef.getParamVal());
                }
                entryList.add(pvo);
            }

            entryList = DataUtils.unmodifiableList(entryList);
        }
    }

    public ParamValueEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<ParamValueEntry> getEntryList() {
        return entryList;
    }

    public int size() {
        return entryList.size();
    }

    public ParamValuesDef getParamValuesDef() throws UnifyException {
        ParamValuesDef.Builder pvdb = ParamValuesDef.newBuilder();
        for (ParamValueEntry pvo : entryList) {
            String param = pvo.isWithParamInput() ? pvo.getParamInput().getStringValue() : null;
            pvdb.addParamValueDef(pvo.getParamConfig(), param);
        }

        return pvdb.build();
    }

}
