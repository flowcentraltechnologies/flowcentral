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
package com.flowcentraltech.flowcentral.application.web.data;

import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Letter form listing.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LetterFormListing extends AbstractFormListing {

    private Map<String, Object> parameters;

    private List<String> letterNames;

    private String generator;

    public LetterFormListing(List<String> letterNames, String generator, Map<String, Object> parameters) {
        this.letterNames = letterNames;
        this.generator = generator;
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public <T> T getParameter(Class<T> dataType, String name) throws UnifyException {
        return parameters != null ? DataUtils.convert(dataType, parameters.get(name))
                : DataUtils.convert(dataType, null);
    }

    public String getGenerator() {
        return generator;
    }

    public String getLetterName(int index) {
        return letterNames.get(index);
    }

    public int getNumberOfParts() {
        return letterNames.size();
    }

    public boolean isEmptyParameters() {
        return DataUtils.isBlank(parameters);
    }
}
