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

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Notification template definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotificationTemplateDef extends BaseApplicationEntityDef {

    private NotificationType notificationType;

    private NotificationMessageFormat format;

    private String attachmentGenerator;

    private List<StringToken> subjectTokenList;

    private List<StringToken> templateTokenList;

    public NotificationTemplateDef(NotificationType notificationType, String attachmentGenerator, String subject,
            String template, NotificationMessageFormat format, String longName, String description, Long id,
            long version) throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        this.notificationType = notificationType;
        this.attachmentGenerator = attachmentGenerator;
        this.subjectTokenList = StringUtils.breakdownParameterizedString(subject);
        this.templateTokenList = StringUtils.breakdownParameterizedString(template);
        this.format = format;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getAttachmentGenerator() {
        return attachmentGenerator;
    }

    public List<StringToken> getSubjectTokenList() {
        return subjectTokenList;
    }

    public List<StringToken> getTemplateTokenList() {
        return templateTokenList;
    }

    public NotificationMessageFormat getFormat() {
        return format;
    }

    public boolean isWithAttachmentGenerator() {
        return !StringUtils.isBlank(attachmentGenerator);
    }
}
