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
package com.flowcentraltech.flowcentral.notification.data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Notification template definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotifTemplateDef extends BaseApplicationEntityDef {

    private NotifType notifType;

    private NotifMessageFormat format;

    private String entity;

    private List<StringToken> subjectTokenList;

    private List<StringToken> templateTokenList;

    private Map<String, NotifTemplateParamDef> params;

    public NotifTemplateDef(NotifType notifType, String entity, String subject, String template,
            NotifMessageFormat format, List<NotifTemplateParamDef> paramList, String longName, String description,
            Long id, long version) throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        this.notifType = notifType;
        this.entity = entity;
        this.subjectTokenList = StringUtils.breakdownParameterizedString(subject);
        this.templateTokenList = StringUtils.breakdownParameterizedString(template);
        this.format = format;
        this.params = new LinkedHashMap<String, NotifTemplateParamDef>();
        for (NotifTemplateParamDef param : paramList) {
            this.params.put(param.getName(), param);
        }
    }

    public String getEntity() {
        return entity;
    }

    public List<StringToken> getSubjectTokenList() {
        return subjectTokenList;
    }

    public List<StringToken> getTemplateTokenList() {
        return templateTokenList;
    }

    public NotifMessageFormat getFormat() {
        return format;
    }

    public NotifType getNotifType() {
        return notifType;
    }

    public Set<String> getParamNames() {
        return params.keySet();
    }

    public Collection<NotifTemplateParamDef> getParams() {
        return params.values();
    }

    public boolean isParam(String name) {
        return params.containsKey(name);
    }

    public NotifTemplateParamDef getParam(String name) {
        return params.get(name);
    }
}
