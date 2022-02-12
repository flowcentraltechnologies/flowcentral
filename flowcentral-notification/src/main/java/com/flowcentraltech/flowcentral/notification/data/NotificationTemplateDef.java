/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.notification.data;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Notification template definition.
 * 
 * @author Lateef Ojulari
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
