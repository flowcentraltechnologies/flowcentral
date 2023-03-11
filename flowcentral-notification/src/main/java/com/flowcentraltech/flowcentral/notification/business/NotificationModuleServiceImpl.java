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
import com.flowcentraltech.flowcentral.common.data.Dictionary;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.notification.constants.NotificationChannelPropertyConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationHostServerConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationInboxStatus;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleErrorConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationOutboxStatus;
import com.flowcentraltech.flowcentral.notification.data.NotificationChannelDef;
import com.flowcentraltech.flowcentral.notification.data.NotificationChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotificationTemplateDef;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannel;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannelProp;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannelQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationInbox;
import com.flowcentraltech.flowcentral.notification.entities.NotificationInboxMessage;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutbox;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutboxMessage;
import com.flowcentraltech.flowcentral.notification.entities.NotificationOutboxQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationRecipient;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
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
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.security.TwoWayStringCryptograph;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;

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

    private static final List<NotificationType> NOTIFICATION_TYPE_LIST = Arrays.asList(NotificationType.EMAIL,
            NotificationType.SMS);

    @Configurable
    private AppletUtilities au;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Configurable
    private TwoWayStringCryptograph twoWayStringCryptograph;

    private Map<NotificationType, NotificationMessagingChannel> messagingChannels;

    private final FactoryMap<String, NotificationTemplateDef> templates;

    private final FactoryMap<Long, TenantChannelInfo> tenantChannelInfos;

    public NotificationModuleServiceImpl() {
        this.messagingChannels = new HashMap<NotificationType, NotificationMessagingChannel>();

        this.templates = new FactoryMap<String, NotificationTemplateDef>(true)
            {
                @Override
                protected boolean stale(String name, NotificationTemplateDef notifTemplateDef) throws Exception {
                    return (environment().value(long.class, "versionNo", new NotificationTemplateQuery()
                            .id(notifTemplateDef.getId())) > notifTemplateDef.getVersion());
                }

                @Override
                protected NotificationTemplateDef create(String longName, Object... params) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    NotificationTemplate notificationTemplate = environment().list(new NotificationTemplateQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (notificationTemplate == null) {
                        throw new UnifyException(NotificationModuleErrorConstants.CANNOT_FIND_NOTIFICATION_TEMPLATE,
                                longName);
                    }

                    return new NotificationTemplateDef(notificationTemplate.getNotificationType(),
                            notificationTemplate.getAttachmentGenerator(), notificationTemplate.getSubject(),
                            notificationTemplate.getTemplate(), notificationTemplate.getMessageFormat(), longName,
                            notificationTemplate.getDescription(), notificationTemplate.getId(),
                            notificationTemplate.getVersionNo());
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

    public void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    public void setTwoWayStringCryptograph(TwoWayStringCryptograph twoWayStringCryptograph) {
        this.twoWayStringCryptograph = twoWayStringCryptograph;
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
    public NotificationChannelMessage constructNotificationChannelMessage(Long tenantId, String notifTemplateName,
            Dictionary dictionary, List<Recipient> recipients) throws UnifyException {
        return constructNotificationChannelMessage(tenantId, notifTemplateName, dictionary,
                DataUtils.toArray(Recipient.class, recipients));
    }

    @Override
    public NotificationChannelMessage constructNotificationChannelMessage(Long tenantId, String notifTemplateName,
            Dictionary dictionary, Recipient... recipients) throws UnifyException {
        NotificationTemplateDef notificationTemplateDef = templates.get(notifTemplateName);

        NotificationChannelMessage.Builder ncmb = NotificationChannelMessage
                .newBuilder(notificationTemplateDef.getNotificationType(), null, tenantId);
        ValueStore valueStore = new MapValueStore(dictionary.getValueMap());
        ParameterizedStringGenerator sgenerator = au.getStringGenerator(valueStore,
                notificationTemplateDef.getSubjectTokenList());
        ParameterizedStringGenerator bgenerator = au.getStringGenerator(valueStore,
                notificationTemplateDef.getTemplateTokenList());
        ncmb.toRecipients(recipients).withSubject(sgenerator.generate()).withBody(bgenerator.generate());
        if (notificationTemplateDef.isWithAttachmentGenerator()) {
            ncmb.withAttachments(
                    ((NotificationAttachmentGenerator) getComponent(notificationTemplateDef.getAttachmentGenerator()))
                            .generateAttachment(dictionary));
        }

        return ncmb.build();
    }

    @Override
    public NotificationChannelMessage constructNotificationChannelMessage(Long tenantId, String notifTemplateName,
            ValueStore valueStore, List<Recipient> recipients) throws UnifyException {
        return constructNotificationChannelMessage(tenantId, notifTemplateName, valueStore,
                DataUtils.toArray(Recipient.class, recipients));
    }

    @Override
    public NotificationChannelMessage constructNotificationChannelMessage(Long tenantId, String notifTemplateName,
            ValueStore valueStore, Recipient... recipients) throws UnifyException {
        if (recipients.length == 0) {
            // TODO Throw exception
        }

        NotificationTemplateDef notificationTemplateDef = templates.get(notifTemplateName);
        NotificationChannelMessage.Builder ncmb = NotificationChannelMessage
                .newBuilder(notificationTemplateDef.getNotificationType(), null, tenantId);
        ParameterizedStringGenerator sgenerator = au.getStringGenerator(valueStore,
                notificationTemplateDef.getSubjectTokenList());
        ParameterizedStringGenerator bgenerator = au.getStringGenerator(valueStore,
                notificationTemplateDef.getTemplateTokenList());
        ncmb.toRecipients(recipients).withSubject(sgenerator.generate()).withBody(bgenerator.generate());
        if (notificationTemplateDef.isWithAttachmentGenerator()) {
            ncmb.withAttachments(
                    ((NotificationAttachmentGenerator) getComponent(notificationTemplateDef.getAttachmentGenerator()))
                            .generateAttachment(valueStore));
        }

        return ncmb.build();
    }

    @Override
    public void sendNotification(NotificationChannelMessage notifChannelMessage) throws UnifyException {
        final NotificationType type = notifChannelMessage.getNotificationType();

        if (NotificationType.SYSTEM.equals(type)) {
            dispatchSystemNotification(notifChannelMessage);
        } else {
            // Put notification in external communication system
            NotificationOutbox notification = new NotificationOutbox();
            notification.setTenantId(notifChannelMessage.getTenantId());
            notification.setType(type);
            notification.setExpiryDt(null);
            notification.setNextAttemptDt(getNow());
            notification.setSubject(notifChannelMessage.getSubject());
            notification.setStatus(NotificationOutboxStatus.NOT_SENT);
            notification.setNotificationMessage(
                    new NotificationOutboxMessage(notifChannelMessage.getFormat(), notifChannelMessage.getBody()));
            List<NotificationRecipient> notificationRecipientList = new ArrayList<NotificationRecipient>();
            for (Recipient recipient : notifChannelMessage.getRecipients()) {
                NotificationRecipient notifRecipient = new NotificationRecipient();
                notifRecipient.setType(recipient.getType());
                notifRecipient.setRecipientName(recipient.getName());
                notifRecipient.setRecipientContact(recipient.getContact());
                notificationRecipientList.add(notifRecipient);
            }

            notification.setNotificationRecipientList(notificationRecipientList);

            Long notificationId = (Long) environment().create(notification);

            if (!DataUtils.isBlank(notifChannelMessage.getAttachments())) {
                for (Attachment attachment : notifChannelMessage.getAttachments()) {
                    fileAttachmentProvider.saveFileAttachment("notification", "notification.notifOutbox",
                            notificationId, attachment);
                }
            }
        }
    }

    @Override
    public void dispatchSystemNotification(NotificationChannelMessage notifChannelMessage) throws UnifyException {
        if (!NotificationType.SYSTEM.equals(notifChannelMessage.getNotificationType())) {
            throw new UnifyException(NotificationModuleErrorConstants.CANNOT_SEND_NOTIFICATION_TYPE_THROUGH_CHANNEL,
                    notifChannelMessage.getNotificationType(), NotificationType.SYSTEM);
        }

        NotificationInbox notificationInbox = new NotificationInbox();
        notificationInbox.setTenantId(notifChannelMessage.getTenantId());
        notificationInbox.setSubject(notifChannelMessage.getSubject());
        notificationInbox.setActionLink(null);
        notificationInbox.setActionTarget(null);
        notificationInbox.setStatus(NotificationInboxStatus.NOT_READ);
        notificationInbox.setNotificationInboxMessage(new NotificationInboxMessage());
        notificationInbox.getNotificationInboxMessage().setMessage(notifChannelMessage.getBody());
        for (Recipient recipient : notifChannelMessage.getRecipients()) {
            notificationInbox.setUserLoginId(recipient.getName());
            environment().create(notificationInbox);
        }
    }

    @Periodic(PeriodicType.SLOWER)
    public void sendNotifications(TaskMonitor taskMonitor) throws UnifyException {
        if (grabClusterLock(SEND_NOTIFICATION_LOCK)) {
            try {
                if (au.system().getSysParameterValue(boolean.class,
                        NotificationModuleSysParamConstants.NOTIFICATION_ENABLED)) {
                    int maxBatchSize = au.system().getSysParameterValue(int.class,

                            NotificationModuleSysParamConstants.NOTIFICATION_MAX_BATCH_SIZE);
                    int maxAttempts = au.system().getSysParameterValue(int.class,

                            NotificationModuleSysParamConstants.NOTIFICATION_MAXIMUM_TRIES);
                    int retryMinutes = au.system().getSysParameterValue(int.class,

                            NotificationModuleSysParamConstants.NOTIFICATION_RETRY_MINUTES);

                    final Date now = environment().getNow();
                    for (NotificationType notificationType : NOTIFICATION_TYPE_LIST) {
                        for (Long tenantId : au.system().getPrimaryMappedTenantIds()) {
                            if (environment().countAll(new NotificationChannelQuery().notificationType(notificationType)
                                    .tenantId(tenantId).status(RecordStatus.ACTIVE)) > 0) {
                                TenantChannelInfo tenantChannelInfo = tenantChannelInfos.get(tenantId);
                                final NotificationChannelDef notificationChannelDef = tenantChannelInfo
                                        .getNotificationChannelDef(notificationType);
                                final int localMaxBatchSize = notificationChannelDef
                                        .isProp(NotificationChannelPropertyConstants.MAX_BATCH_SIZE)
                                                ? notificationChannelDef.getPropValue(int.class,
                                                        NotificationChannelPropertyConstants.MAX_BATCH_SIZE)
                                                : maxBatchSize;
                                final int localMaxAttempts = notificationChannelDef
                                        .isProp(NotificationChannelPropertyConstants.MAX_TRIES)
                                                ? notificationChannelDef.getPropValue(int.class,
                                                        NotificationChannelPropertyConstants.MAX_TRIES)
                                                : maxAttempts;
                                final int localRetryMinutes = notificationChannelDef
                                        .isProp(NotificationChannelPropertyConstants.RETRY_MINUTES)
                                                ? notificationChannelDef.getPropValue(int.class,
                                                        NotificationChannelPropertyConstants.RETRY_MINUTES)
                                                : retryMinutes;
                                List<NotificationOutbox> notificationList = environment()
                                        .findAllWithChildren(new NotificationOutboxQuery().type(notificationType)
                                                .due(now).status(NotificationOutboxStatus.NOT_SENT).orderById()
                                                .addSelect("type", "status", "subject", "attempts", "expiryDt",
                                                        "nextAttemptDt", "sentDt", "notificationMessage",
                                                        "notificationRecipientList")
                                                .setLimit(localMaxBatchSize));
                                logDebug("Sending [{0}] notifications via channel [{1}]...", notificationList.size(),
                                        notificationChannelDef.getDescription());
                                if (!DataUtils.isBlank(notificationList)) {
                                    NotificationMessagingChannel channel = getNotificationMessagingChannel(
                                            notificationType);
                                    NotificationChannelMessage[] messages = getNotificationChannelMessages(tenantId,
                                            notificationList);
                                    channel.sendMessages(notificationChannelDef, messages);
                                    for (int i = 0; i < messages.length; i++) {
                                        NotificationOutbox notification = notificationList.get(i);
                                        int attempts = notification.getAttempts() + 1;
                                        Update update = new Update().add("attempts", attempts);
                                        if (messages[i].isSent()) {
                                            update.add("sentDt", now);
                                            update.add("status", NotificationOutboxStatus.SENT);
                                        } else {
                                            if (attempts >= localMaxAttempts) {
                                                update.add("status", NotificationOutboxStatus.ABORTED);
                                            } else {
                                                Date nextAttemptDt = CalendarUtils.getNowWithFrequencyOffset(now,
                                                        FrequencyUnit.MINUTE, localRetryMinutes);
                                                update.add("nextAttemptDt", nextAttemptDt);
                                            }
                                        }

                                        environment().updateById(NotificationOutbox.class, notification.getId(),
                                                update);
                                    }
                                }
                                commitTransactions();
                            }

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

    private NotificationMessagingChannel getNotificationMessagingChannel(NotificationType type) throws UnifyException {
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

    private NotificationChannelMessage[] getNotificationChannelMessages(Long tenantId,
            List<NotificationOutbox> notificationList) throws UnifyException {
        int len = notificationList.size();
        NotificationChannelMessage[] messages = new NotificationChannelMessage[len];
        for (int i = 0; i < len; i++) {
            NotificationOutbox notification = notificationList.get(i);
            NotificationChannelMessage.Builder ncmb = NotificationChannelMessage.newBuilder(notification.getType(),
                    notification.getId(), tenantId);
            ncmb.withSubject(notification.getSubject()).withBody(notification.getNotificationMessage().getMessage())
                    .usingBodyFormat(notification.getNotificationMessage().getFormat());

            for (NotificationRecipient notificationRecipient : notification.getNotificationRecipientList()) {
                ncmb.toRecipient(notificationRecipient.getType(), notificationRecipient.getRecipientName(),
                        notificationRecipient.getRecipientContact());
            }

            ncmb.withAttachments(fileAttachmentProvider.retrieveAllFileAttachments("notification",
                    "notification.notifOutbox", notification.getId()));
            messages[i] = ncmb.build();
        }

        return messages;
    }

    private class TenantChannelInfo {

        private final FactoryMap<NotificationType, NotificationChannelDef> channelDefsByType;

        private final FactoryMap<String, NotificationChannelDef> channelDefsByName;

        private final Long tenantId;

        public TenantChannelInfo(Long _tenantId) {
            this.tenantId = _tenantId;
            channelDefsByType = new FactoryMap<NotificationType, NotificationChannelDef>(true)
                {

                    @Override
                    protected boolean stale(NotificationType type, NotificationChannelDef notificationChannelDef)
                            throws Exception {
                        return (environment().value(long.class, "versionNo",
                                new NotificationChannelQuery().tenantId(tenantId)
                                        .id(notificationChannelDef.getId())) > notificationChannelDef.getVersion());
                    }

                    @Override
                    protected NotificationChannelDef create(NotificationType type, Object... params) throws Exception {
                        String name = environment().value(String.class, "name",
                                new NotificationChannelQuery().notificationType(type).tenantId(tenantId));
                        return channelDefsByName.get(name);
                    }

                };

            channelDefsByName = new FactoryMap<String, NotificationChannelDef>(true)
                {

                    @Override
                    protected boolean stale(String name, NotificationChannelDef notificationChannelDef)
                            throws Exception {
                        return (environment().value(long.class, "versionNo",
                                new NotificationChannelQuery().tenantId(tenantId)
                                        .id(notificationChannelDef.getId())) > notificationChannelDef.getVersion());
                    }

                    @Override
                    protected NotificationChannelDef create(String name, Object... params) throws Exception {
                        NotificationChannel notificationChannel = environment()
                                .list(new NotificationChannelQuery().name(name).tenantId(tenantId));
                        if (notificationChannel == null) {
                            throw new UnifyException(
                                    NotificationModuleErrorConstants.NOTIFICATION_CHANNEL_WITH_NAME_UNKNOWN, name);
                        }

                        NotificationChannelDef.Builder ncdb = NotificationChannelDef.newBuilder(
                                notificationChannel.getNotificationType(), notificationChannel.getSenderName(),
                                notificationChannel.getSenderContact(), name, notificationChannel.getDescription(),
                                notificationChannel.getId(), notificationChannel.getVersionNo());
                        for (NotificationChannelProp notifChannelProp : notificationChannel
                                .getNotificationChannelPropList()) {
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

        public NotificationChannelDef getNotificationChannelDef(NotificationType type) throws UnifyException {
            return channelDefsByType.get(type);
        }
    }

}
