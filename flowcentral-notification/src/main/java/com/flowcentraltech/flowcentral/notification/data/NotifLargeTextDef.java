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
package com.flowcentraltech.flowcentral.notification.data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;

/**
 * Notification large text definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotifLargeTextDef extends BaseApplicationEntityDef {

    private String entity;

    private int fontSizeInPixels;

    private List<List<StringToken>> bodyTokenList;

    private Map<String, NotifLargeTextParamDef> params;

    public NotifLargeTextDef(String entity, int fontSizeInPixels, List<List<StringToken>> bodyTokenList,
            List<NotifLargeTextParamDef> paramList, String longName, String description, Long id, long version)
            throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        this.entity = entity;
        this.fontSizeInPixels = fontSizeInPixels;
        this.bodyTokenList = bodyTokenList;
        this.params = new LinkedHashMap<String, NotifLargeTextParamDef>();
        for (NotifLargeTextParamDef param : paramList) {
            this.params.put(param.getName(), param);
        }
    }

    public String getEntity() {
        return entity;
    }

    public int getFontSizeInPixels() {
        return fontSizeInPixels;
    }

    public List<StringToken> getBodyTokenList(int pageIndex) {
        return bodyTokenList.get(pageIndex);
    }

    public int getNumberOfPages() {
        return bodyTokenList.size();
    }

    public Set<String> getParamNames() {
        return params.keySet();
    }

    public Collection<NotifLargeTextParamDef> getParams() {
        return params.values();
    }

    public boolean isParam(String name) {
        return params.containsKey(name);
    }

    public NotifLargeTextParamDef getParam(String name) {
        return params.get(name);
    }
}
