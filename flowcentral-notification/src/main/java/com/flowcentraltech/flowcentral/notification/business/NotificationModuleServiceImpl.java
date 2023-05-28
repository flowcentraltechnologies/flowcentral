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
package com.flowcentraltech.flowcentral.notification.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.notification.constants.NotificationChannelPropertyConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationHostServerConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationInboxStatus;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleErrorConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationOutboxStatus;
import com.flowcentraltech.flowcentral.notification.data.ChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifChannelDef;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextDef;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextParamDef;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateParamDef;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannel;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannelProp;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannelQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationInbox;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeText;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeTextParam;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeTextParamQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeTextQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutbox;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutboxAttachment;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutboxError;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutboxQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationRecipient;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateParam;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateParamQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifLargeTextInfo;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifTemplateInfo;
import com.flowcentraltech.flowcentral.notification.util.NotificationCodeGenUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.MapValueStore;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.security.TwoWayStringCryptograph;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Default notification business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(NotificationModuleNameConstants.NOTIFICATION_MODULE_SERVICE)
public class NotificationModuleServiceImpl extends AbstractFlowCentralService implements NotificationModuleService {

    private static final String SEND_NOTIFICATION_LOCK = "notif::sendnotification-lock";

    private static final List<NotifType> NOTIFICATION_TYPE_LIST = Arrays.asList(NotifType.EMAIL, NotifType.SMS);

    @Configurable
    private AppletUtilities au;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Configurable
    private TwoWayStringCryptograph twoWayStringCryptograph;

    private Map<NotifType, NotificationMessagingChannel> messagingChannels;

    private final FactoryMap<String, NotifTemplateDef> templates;

    private final FactoryMap<String, NotifLargeTextDef> largeTexts;

    private final FactoryMap<Long, TenantChannelInfo> tenantChannelInfos;

    public NotificationModuleServiceImpl() {
        this.messagingChannels = new HashMap<NotifType, NotificationMessagingChannel>();

        this.templates = new FactoryMap<String, NotifTemplateDef>(true)
            {
                @Override
                protected boolean stale(String name, NotifTemplateDef notifTemplateDef) throws Exception {
                    return (environment().value(long.class, "versionNo", new NotificationTemplateQuery()
                            .id(notifTemplateDef.getId())) > notifTemplateDef.getVersion());
                }

                @Override
                protected NotifTemplateDef create(String longName, Object... params) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    NotificationTemplate notificationTemplate = environment().list(new NotificationTemplateQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (notificationTemplate == null) {
                        throw new UnifyException(NotificationModuleErrorConstants.CANNOT_FIND_NOTIFICATION_TEMPLATE,
                                longName);
                    }

                    List<NotifTemplateParamDef> paramList = new ArrayList<NotifTemplateParamDef>();
                    for (NotificationTemplateParam templateParam : notificationTemplate.getParamList()) {
                        paramList.add(new NotifTemplateParamDef(templateParam.getName(), templateParam.getLabel()));
                    }

                    return new NotifTemplateDef(notificationTemplate.getNotificationType(),
                            notificationTemplate.getEntity(), notificationTemplate.getSubject(),
                            notificationTemplate.getTemplate(), notificationTemplate.getMessageFormat(), paramList,
                            longName, notificationTemplate.getDescription(), notificationTemplate.getId(),
                            notificationTemplate.getVersionNo());
                }

            };

        this.largeTexts = new FactoryMap<String, NotifLargeTextDef>(true)
            {
                @Override
                protected boolean stale(String name, NotifLargeTextDef notifLargeTextDef) throws Exception {
                    return (environment().value(long.class, "versionNo", new NotificationLargeTextQuery()
                            .id(notifLargeTextDef.getId())) > notifLargeTextDef.getVersion());
                }

                @Override
                protected NotifLargeTextDef create(String longName, Object... params) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    NotificationLargeText notificationLargeText = environment().list(new NotificationLargeTextQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (notificationLargeText == null) {
                        throw new UnifyException(NotificationModuleErrorConstants.CANNOT_FIND_NOTIFICATION_LARGETEXT,
                                longName);
                    }

                    List<NotifLargeTextParamDef> paramList = new ArrayList<NotifLargeTextParamDef>();
                    for (NotificationLargeTextParam largeTextParam : notificationLargeText.getParamList()) {
                        paramList.add(new NotifLargeTextParamDef(largeTextParam.getName(), largeTextParam.getLabel()));
                    }

                    return new NotifLargeTextDef(notificationLargeText.getEntity(), notificationLargeText.getBody(),
                            paramList, longName, notificationLargeText.getDescription(), notificationLargeText.getId(),
                            notificationLargeText.getVersionNo());
                }

            };

        this.tenantChannelInfos = new FactoryMap<Long, TenantChannelInfo>()
            {

                @Override
                protected TenantChannelInfo create(Long tenantId, Object... params) throws Exception {
                    return new TenantChannelInfo(tenantId);
                }

            };
    }

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    public final void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    public final void setTwoWayStringCryptograph(TwoWayStringCryptograph twoWayStringCryptograph) {
        this.twoWayStringCryptograph = twoWayStringCryptograph;
    }

    private static final Class<?>[] WRAPPER_PARAMS_0 = { NotifTemplateDef.class };

    @Override
    public <T extends NotifTemplateWrapper> T wrapperOfNotifTemplate(Class<T> wrapperType) throws UnifyException {
        final String templateName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                NotificationCodeGenUtils.TEMPLATE_NAME);
        final NotifTemplateDef notifTemplateDef = getNotifTemplateDef(templateName);
        return ReflectUtils.newInstance(wrapperType, WRAPPER_PARAMS_0, notifTemplateDef);
    }

    @Override
    public <T extends NotifLargeTextWrapper> T wrapperOfNotifLargeText(Class<T> wrapperType) throws UnifyException {
        final String largeTextName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                NotificationCodeGenUtils.LARGETEXT_NAME);
        final NotifLargeTextDef notifLargeTextDef = getNotifLargeTextDef(largeTextName);
        return ReflectUtils.newInstance(wrapperType, WRAPPER_PARAMS_0, notifLargeTextDef);
    }

    @Override
    public int countNotifTemplatesByModule(String moduleName) throws UnifyException {
        return environment().countAll(new NotificationTemplateQuery().moduleName(moduleName));
    }

    @Override
    public List<DynamicNotifTemplateInfo> generateNotifTemplateInfos(String basePackage, String moduleName)
            throws UnifyException {
        List<NotificationTemplate> templates = environment().listAll(new NotificationTemplateQuery()
                .moduleName(moduleName).addSelect("applicationName", "name"));
        if (!DataUtils.isBlank(templates)) {
            List<DynamicNotifTemplateInfo> resultList = new ArrayList<DynamicNotifTemplateInfo>();
            for (NotificationTemplate template : templates) {
                final String templateClassName = NotificationCodeGenUtils
                        .generateUtilitiesTemplateWrapperClassName(basePackage, moduleName, template.getName());
                final List<String> paramNames = environment().valueList(String.class, "name",
                        new NotificationTemplateParamQuery().notificationTemplateId(template.getId()));
                resultList.add(new DynamicNotifTemplateInfo(ApplicationNameUtils.getApplicationEntityLongName(
                        template.getApplicationName(), template.getName()), templateClassName, paramNames));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<DynamicNotifLargeTextInfo> generateNotifLargeTextInfos(String basePackage, String moduleName)
            throws UnifyException {
        List<NotificationLargeText> largeTexts = environment().listAll(new NotificationLargeTextQuery()
                .moduleName(moduleName).addSelect("applicationName", "name"));
        if (!DataUtils.isBlank(largeTexts)) {
            List<DynamicNotifLargeTextInfo> resultList = new ArrayList<DynamicNotifLargeTextInfo>();
            for (NotificationLargeText largeText : largeTexts) {
                final String largeTextClassName = NotificationCodeGenUtils
                        .generateUtilitiesLargeTextWrapperClassName(basePackage, moduleName, largeText.getName());
                final List<String> paramNames = environment().valueList(String.class, "name",
                        new NotificationLargeTextParamQuery().notifLargeTextId(largeText.getId()));
                resultList.add(new DynamicNotifLargeTextInfo(ApplicationNameUtils.getApplicationEntityLongName(
                        largeText.getApplicationName(), largeText.getName()), largeTextClassName, paramNames));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<NotificationTemplate> findNotificationTemplates(NotificationTemplateQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public NotificationTemplate findNotificationTemplate(Long notifTemplateId) throws UnifyException {
        return environment().find(NotificationTemplate.class, notifTemplateId);
    }

    @Override
    public List<Long> findNotificationTemplateIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id",
                new NotificationTemplateQuery().applicationName(applicationName));
    }

    @Override
    public NotificationLargeText findNotificationLargeText(Long notifLargeTextId) throws UnifyException {
        return environment().find(NotificationLargeText.class, notifLargeTextId);
    }

    @Override
    public List<Long> findNotificationLargeTextIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id",
                new NotificationLargeTextQuery().applicationName(applicationName));
    }

    @Override
    public NotifTemplateDef getNotifTemplateDef(String templateName) throws UnifyException {
        return templates.get(templateName);
    }

    @Override
    public NotifLargeTextDef getNotifLargeTextDef(String largeTextName) throws UnifyException {
        return largeTexts.get(largeTextName);
    }

    @Override
    public void sendNotification(NotifMessage notifMessage) throws UnifyException {
        final NotifTemplateDef notifTemplateDef = getNotifTemplateDef(notifMessage.getTemplate());
        if (NotifType.SYSTEM.equals(notifTemplateDef.getNotifType())) {
            dispatchSystemNotification(notifMessage);
        } else {
            MessageParts messageParts = getMessageParts(notifMessage);
            NotificationOutbox notification = new NotificationOutbox();
            notification.setTenantId(getUserTenantId());
            notification.setImportance(notifMessage.getImportance());
            notification.setFrom(notifMessage.getFrom());
            notification.setType(notifTemplateDef.getNotifType());
            notification.setExpiryDt(null);
            notification.setNextAttemptDt(getNow());
            notification.setSubject(messageParts.getSubject());
            notification.setStatus(NotificationOutboxStatus.NOT_SENT);
            notification.setFormat(notifTemplateDef.getFormat());
            notification.setMessage(messageParts.getBody());

            // Recipients
            List<NotificationRecipient> recipientList = new ArrayList<NotificationRecipient>();
            for (Recipient recipient : notifMessage.getRecipients()) {
                NotificationRecipient notifRecipient = new NotificationRecipient();
                notifRecipient.setType(recipient.getType());
                notifRecipient.setRecipientName(recipient.getName());
                notifRecipient.setRecipientContact(recipient.getContact());
                recipientList.add(notifRecipient);
            }

            notification.setRecipientList(recipientList);

            // Attachments
            List<NotificationOutboxAttachment> attachmentList = new ArrayList<NotificationOutboxAttachment>();
            for (Attachment attachment : notifMessage.getAttachments()) {
                NotificationOutboxAttachment notifAttachment = new NotificationOutboxAttachment();
                notifAttachment.setName(attachment.getName());
                notifAttachment.setTitle(attachment.getTitle());
                notifAttachment.setType(attachment.getType());
                notifAttachment.setData(attachment.getData());
                attachmentList.add(notifAttachment);
            }

            notification.setAttachmentList(attachmentList);

            // Create outgoing notification
            environment().create(notification);
        }
    }

    @Periodic(PeriodicType.SLOW)
    public void sendNotifications(TaskMonitor taskMonitor) throws UnifyException {
        logDebug("Grabbing lock required to send notifications...");
        if (au.system().getSysParameterValue(boolean.class, NotificationModuleSysParamConstants.NOTIFICATION_ENABLED)
                && grabClusterLock(SEND_NOTIFICATION_LOCK)) {
            try {
                final int maxBatchSize = au.system().getSysParameterValue(int.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_MAX_BATCH_SIZE);
                final int maxAttempts = au.system().getSysParameterValue(int.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_MAXIMUM_TRIES);
                final int retryMinutes = au.system().getSysParameterValue(int.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_RETRY_MINUTES);

                final Date now = environment().getNow();
                for (NotifType notifType : NOTIFICATION_TYPE_LIST) {
                    for (Long tenantId : au.system().getPrimaryMappedTenantIds()) {
                        logDebug("Checking [{0}] notifications for tenant with id [{1}]...", notifType, tenantId);
                        if (environment().countAll(new NotificationChannelQuery().notifType(notifType)
                                .tenantId(tenantId).status(RecordStatus.ACTIVE)) > 0) {
                            logDebug("Sending [{0}] notifications for tenant with id [{1}]...", notifType, tenantId);
                            TenantChannelInfo tenantChannelInfo = tenantChannelInfos.get(tenantId);
                            final NotifChannelDef notifChannelDef = tenantChannelInfo
                                    .getNotificationChannelDef(notifType);
                            final int localMaxBatchSize = notifChannelDef
                                    .isProp(NotificationChannelPropertyConstants.MAX_BATCH_SIZE)
                                            ? notifChannelDef.getPropValue(int.class,
                                                    NotificationChannelPropertyConstants.MAX_BATCH_SIZE)
                                            : maxBatchSize;
                            final int localMaxAttempts = notifChannelDef
                                    .isProp(NotificationChannelPropertyConstants.MAX_TRIES)
                                            ? notifChannelDef.getPropValue(int.class,
                                                    NotificationChannelPropertyConstants.MAX_TRIES)
                                            : maxAttempts;
                            final int localRetryMinutes = notifChannelDef
                                    .isProp(NotificationChannelPropertyConstants.RETRY_MINUTES)
                                            ? notifChannelDef.getPropValue(int.class,
                                                    NotificationChannelPropertyConstants.RETRY_MINUTES)
                                            : retryMinutes;
                            logDebug(
                                    "Parameters for sending notifications extracted. MaxBatchSize = [{0}], MaxAttempts = [{1}], RetryMinutes = [{2}]",
                                    localMaxBatchSize, localMaxAttempts, localRetryMinutes);
                            List<Long> pendingNotificationIdList = environment().valueList(Long.class, "id",
                                    new NotificationOutboxQuery().type(notifType).due(now)
                                            .status(NotificationOutboxStatus.NOT_SENT).setLimit(localMaxBatchSize));
                            logDebug("Sending [{0}] notifications via channel [{1}]...",
                                    pendingNotificationIdList.size(), notifChannelDef.getDescription());
                            if (!DataUtils.isBlank(pendingNotificationIdList)) {
                                NotificationMessagingChannel channel = getNotificationMessagingChannel(notifType);
                                ChannelMessage[] messages = getChannelMessages(tenantId, pendingNotificationIdList);
                                channel.sendMessages(notifChannelDef, messages);
                                for (ChannelMessage message : messages) {
                                    final int attempts = message.getAttempts() + 1;
                                    Update update = new Update().add("attempts", attempts);
                                    if (message.isSent()) {
                                        update.add("sentDt", now);
                                        update.add("status", NotificationOutboxStatus.SENT);
                                    } else {
                                        if (attempts >= localMaxAttempts || !message.isRetry()) {
                                            update.add("status", NotificationOutboxStatus.ABORTED);
                                        } else {
                                            Date nextAttemptDt = CalendarUtils.getNowWithFrequencyOffset(now,
                                                    FrequencyUnit.MINUTE, localRetryMinutes);
                                            update.add("nextAttemptDt", nextAttemptDt);
                                        }
                                    }

                                    environment().updateById(NotificationOutbox.class, message.getOriginId(), update);
                                    if (message.isWithError()) {
                                        NotificationOutboxError error = new NotificationOutboxError();
                                        error.setNotificationId(message.getOriginId());
                                        error.setError(message.getError());
                                        environment().create(error);
                                    }
                                }
                            }
                            commitTransactions();
                        }

                    }
                }
            } finally {
                releaseClusterLock(SEND_NOTIFICATION_LOCK);
            }
        }
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private NotificationMessagingChannel getNotificationMessagingChannel(NotifType type) throws UnifyException {
        NotificationMessagingChannel channel = messagingChannels.get(type);
        if (channel == null) {
            switch (type) {
                case EMAIL:
                    channel = (NotificationMessagingChannel) getComponent(
                            NotificationModuleNameConstants.EMAILMESSAGINGCHANNEL);
                    break;
                case SMS:
                    channel = (NotificationMessagingChannel) getComponent(
                            NotificationModuleNameConstants.SMSMESSAGINGCHANNEL);
                    break;
                case SYSTEM:
                default:
                    break;
            }

            if (channel == null) {
                throw new UnifyException(NotificationModuleErrorConstants.NOTIFICATION_CHANNEL_NOT_AVAILABLE, type);
            }

            messagingChannels.put(type, channel);
        }

        return channel;
    }

    private ChannelMessage[] getChannelMessages(Long tenantId, List<Long> pendingNotificationIdList)
            throws UnifyException {
        List<ChannelMessage> messages = new ArrayList<ChannelMessage>();
        for (Long notificationId : pendingNotificationIdList) {
            NotificationOutbox notification = environment().find(NotificationOutbox.class, notificationId);
            ChannelMessage.Builder cmb = ChannelMessage.newBuilder(notification.getFormat(), notification.getId())
                    .subject(notification.getSubject()).message(notification.getMessage())
                    .attempts(notification.getAttempts());

            // Recipients
            for (NotificationRecipient notificationRecipient : notification.getRecipientList()) {
                cmb.addRecipient(notificationRecipient.getType(), notificationRecipient.getRecipientName(),
                        notificationRecipient.getRecipientContact());
            }

            // Attachments
            for (NotificationOutboxAttachment attachment : notification.getAttachmentList()) {
                cmb.addAttachment(attachment.getType(), attachment.getName(), attachment.getTitle(),
                        attachment.getData());
            }

            messages.add(cmb.build());
        }

        return DataUtils.toArray(ChannelMessage.class, messages);
    }

    private void dispatchSystemNotification(NotifMessage notifMessage) throws UnifyException {
        final NotifTemplateDef notifTemplateDef = getNotifTemplateDef(notifMessage.getTemplate());
        if (!NotifType.SYSTEM.equals(notifTemplateDef.getNotifType())) {
            throw new UnifyException(NotificationModuleErrorConstants.CANNOT_SEND_NOTIFICATION_TYPE_THROUGH_CHANNEL,
                    notifTemplateDef.getNotifType(), NotifType.SYSTEM);
        }

        MessageParts messageParts = getMessageParts(notifMessage);
        NotificationInbox notificationInbox = new NotificationInbox();
        notificationInbox.setSubject(messageParts.getSubject());
        notificationInbox.setActionLink(null);
        notificationInbox.setActionTarget(null);
        notificationInbox.setStatus(NotificationInboxStatus.NOT_READ);
        notificationInbox.setMessage(messageParts.getBody());
        for (Recipient recipient : notifMessage.getRecipients()) {
            notificationInbox.setUserLoginId(recipient.getName());
            environment().create(notificationInbox);
        }
    }

    private MessageParts getMessageParts(NotifMessage notifMessage) throws UnifyException {
        NotifTemplateDef notifTemplateDef = templates.get(notifMessage.getTemplate());
        ValueStoreReader valueStoreReader = new MapValueStore(notifMessage.getParams()).getReader();
        ParameterizedStringGenerator sgenerator = au.getStringGenerator(valueStoreReader,
                notifTemplateDef.getSubjectTokenList());
        ParameterizedStringGenerator bgenerator = au.getStringGenerator(valueStoreReader,
                notifTemplateDef.getTemplateTokenList());
        String subject = sgenerator.generate();
        String body = bgenerator.generate();
        return new MessageParts(subject, body);
    }

    private class MessageParts {

        private final String subject;

        private final String body;

        public MessageParts(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

    }

    private class TenantChannelInfo {

        private final FactoryMap<NotifType, NotifChannelDef> channelDefsByType;

        private final FactoryMap<String, NotifChannelDef> channelDefsByName;

        private final Long tenantId;

        public TenantChannelInfo(Long _tenantId) {
            this.tenantId = _tenantId;
            channelDefsByType = new FactoryMap<NotifType, NotifChannelDef>(true)
                {

                    @Override
                    protected boolean stale(NotifType type, NotifChannelDef notifChannelDef) throws Exception {
                        return (environment().value(long.class, "versionNo", new NotificationChannelQuery()
                                .tenantId(tenantId).id(notifChannelDef.getId())) > notifChannelDef.getVersion());
                    }

                    @Override
                    protected NotifChannelDef create(NotifType type, Object... params) throws Exception {
                        String name = environment().value(String.class, "name",
                                new NotificationChannelQuery().notifType(type).tenantId(tenantId));
                        return channelDefsByName.get(name);
                    }

                };

            channelDefsByName = new FactoryMap<String, NotifChannelDef>(true)
                {

                    @Override
                    protected boolean stale(String name, NotifChannelDef notifChannelDef) throws Exception {
                        return (environment().value(long.class, "versionNo", new NotificationChannelQuery()
                                .tenantId(tenantId).id(notifChannelDef.getId())) > notifChannelDef.getVersion());
                    }

                    @Override
                    protected NotifChannelDef create(String name, Object... params) throws Exception {
                        NotificationChannel notificationChannel = environment()
                                .list(new NotificationChannelQuery().name(name).tenantId(tenantId));
                        if (notificationChannel == null) {
                            throw new UnifyException(
                                    NotificationModuleErrorConstants.NOTIFICATION_CHANNEL_WITH_NAME_UNKNOWN, name);
                        }

                        NotifChannelDef.Builder ncdb = NotifChannelDef.newBuilder(
                                notificationChannel.getNotificationType(), notificationChannel.getSenderName(),
                                notificationChannel.getSenderContact(), name, notificationChannel.getDescription(),
                                notificationChannel.getId(), notificationChannel.getVersionNo());
                        for (NotificationChannelProp notifChannelProp : notificationChannel.getChannelPropList()) {
                            String val = notifChannelProp.getValue();
                            if (NotificationHostServerConstants.PASSWORD_PROPERTY.equals(notifChannelProp.getName())) {
                                val = twoWayStringCryptograph.decrypt(val);
                            }

                            ncdb.addPropDef(notifChannelProp.getName(), val);
                        }

                        return ncdb.build();
                    }

                };
        }

        public NotifChannelDef getNotificationChannelDef(NotifType type) throws UnifyException {
            return channelDefsByType.get(type);
        }
    }

}
