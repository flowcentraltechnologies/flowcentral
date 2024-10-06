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
package com.flowcentraltech.flowcentral.messaging.business;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.messaging.constants.MessagingModuleErrorConstants;
import com.flowcentraltech.flowcentral.messaging.constants.MessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.constants.MessagingModuleSysParamConstants;
import com.flowcentraltech.flowcentral.messaging.data.Message;
import com.flowcentraltech.flowcentral.messaging.data.MessageHeader;
import com.flowcentraltech.flowcentral.messaging.data.MessagingConfigDef;
import com.flowcentraltech.flowcentral.messaging.data.MessagingExecContext;
import com.flowcentraltech.flowcentral.messaging.entities.MessagingReadConfig;
import com.flowcentraltech.flowcentral.messaging.entities.MessagingReadConfigQuery;
import com.flowcentraltech.flowcentral.messaging.entities.MessagingWriteConfig;
import com.flowcentraltech.flowcentral.messaging.entities.MessagingWriteConfigQuery;
import com.flowcentraltech.flowcentral.messaging.readers.MessagingConsumer;
import com.flowcentraltech.flowcentral.messaging.utils.MessagingUtils;
import com.flowcentraltech.flowcentral.messaging.writers.MessagingProducer;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(MessagingModuleNameConstants.MESSAGING_MODULE_SERVICE)
public class MessagingModuleServiceImpl extends AbstractFlowCentralService implements MessagingModuleService {

    @Configurable
    private MessagingProvider messagingProvider;

    @Configurable
    private SystemModuleService systemModuleService;

    private final FactoryMap<String, MessagingConfigDef> readMessagingConfigDefFactoryMap;

    private final FactoryMap<String, MessagingConfigDef> writeMessagingConfigDefFactoryMap;

    public MessagingModuleServiceImpl() {
        this.readMessagingConfigDefFactoryMap = new FactoryMap<String, MessagingConfigDef>(true)
            {

                @Override
                protected boolean stale(String key, MessagingConfigDef messagingConfigDef) throws Exception {
                    if (isStale(new MessagingReadConfigQuery(), messagingConfigDef)) {
                        shutdownExecutorPool(messagingConfigDef.getCtx().getPool());
                        return true;
                    }

                    return false;
                }

                @Override
                protected MessagingConfigDef create(String configName, Object... params) throws Exception {
                    MessagingReadConfig messagingReadConfig = environment()
                            .find(new MessagingReadConfigQuery().name(configName));
                    return new MessagingConfigDef(messagingReadConfig.getId(), messagingReadConfig.getVersionNo(),
                            messagingReadConfig.getName(), messagingReadConfig.getDescription(),
                            messagingReadConfig.getEndpointConfig(), messagingReadConfig.getSource(),
                            messagingReadConfig.getConsumer(), messagingReadConfig.getMaxConcurrent(),
                            messagingReadConfig.getStatus());
                }
            };

        this.writeMessagingConfigDefFactoryMap = new FactoryMap<String, MessagingConfigDef>(true)
            {

                @Override
                protected boolean stale(String key, MessagingConfigDef messagingConfigDef) throws Exception {
                    if (isStale(new MessagingWriteConfigQuery(), messagingConfigDef)) {
                        shutdownExecutorPool(messagingConfigDef.getCtx().getPool());
                        return true;
                    }

                    return false;
                }

                @Override
                protected MessagingConfigDef create(String configName, Object... params) throws Exception {
                    MessagingWriteConfig messagingWriteConfig = environment()
                            .find(new MessagingWriteConfigQuery().name(configName));
                    return new MessagingConfigDef(messagingWriteConfig.getId(), messagingWriteConfig.getVersionNo(),
                            messagingWriteConfig.getName(), messagingWriteConfig.getDescription(),
                            messagingWriteConfig.getEndpointConfig(), messagingWriteConfig.getDestination(),
                            messagingWriteConfig.getProducer(), 1, messagingWriteConfig.getStatus());
                }
            };

    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        readMessagingConfigDefFactoryMap.clear();
        writeMessagingConfigDefFactoryMap.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public void sendMessage(Message message) throws UnifyException {
        MessageHeader header = message.getHeader();
        String text = MessagingUtils.marshal(message);
        provider().sendMessage(header.getConfig(), header.getDestination(), text);
    }

    @Override
    public Message receiveMessage(String config, String source) throws UnifyException {
        String text = provider().receiveMessage(config, source);
        if (!StringUtils.isBlank(text)) {
            return MessagingUtils.unmarshal(config, source, text);
        }

        return null;
    }

    @Periodic(PeriodicType.FASTER)
    public void triggerMessagingForExecution(TaskMonitor taskMonitor) throws UnifyException {
        logDebug("Triggering messaging for execution...");

        // Reads
        if (systemModuleService.getSysParameterValue(boolean.class,
                MessagingModuleSysParamConstants.MESSAGING_READ_ENABLED)) {
            List<String> readConfigList = environment().valueList(String.class, "name",
                    new MessagingReadConfigQuery().status(RecordStatus.ACTIVE));
            logDebug("[{0}] active read configurations detected...", readConfigList.size());
            for (String readConfigName : readConfigList) {
                MessagingConfigDef messagingConfigDef = readMessagingConfigDefFactoryMap.get(readConfigName);
                MessagingExecContext ctx = messagingConfigDef.getCtx();
                int len = ctx.getLoadingAvailable();
                logDebug("Loading [{0}] threads for [{1}]...", len, readConfigName);
                for (int i = 0; i < len; i++) {
                    ctx.getPool().execute(new ReadExec(ctx));
                }
            }
        } else {
            logDebug("Read messaging is currently disabled.");
        }

        // Writes
        if (systemModuleService.getSysParameterValue(boolean.class,
                MessagingModuleSysParamConstants.MESSAGING_WRITE_ENABLED)) {
            List<String> writeConfigList = environment().valueList(String.class, "name",
                    new MessagingWriteConfigQuery().status(RecordStatus.ACTIVE));
            logDebug("[{0}] active write configurations detected...", writeConfigList.size());
            for (String writeConfigName : writeConfigList) {
                MessagingConfigDef messagingConfigDef = writeMessagingConfigDefFactoryMap.get(writeConfigName);
                MessagingExecContext ctx = messagingConfigDef.getCtx();
                int len = ctx.getLoadingAvailable();
                logDebug("Loading [{0}] threads for [{1}]...", len, writeConfigName);
                for (int i = 0; i < len; i++) {
                    ctx.getPool().execute(new WriteExec(ctx));
                }
            }
        } else {
            logDebug("Write messaging is currently disabled.");
        }
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private void logProcessFailure(Message message) throws UnifyException {
        // TODO
    }

    private MessagingProvider provider() throws UnifyException {
        if (messagingProvider == null) {
            throw new UnifyException(MessagingModuleErrorConstants.NO_MESSAGING_PROVIDER_FOUND);
        }

        return messagingProvider;
    }

    private void shutdownExecutorPool(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                pool.awaitTermination(60, TimeUnit.SECONDS);
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private class ReadExec extends AbstractExec {

        public ReadExec(MessagingExecContext ctx) {
            super(ctx);
        }

        @Override
        protected void doRun() throws Exception {
            Message message = receiveMessage(ctx.getConfig(), ctx.getTarget());
            if (message != null) {
                try {
                    logDebug("Messaging read execution [{0}]...", execId);
                    MessagingConsumer consumer = getComponent(MessagingConsumer.class, ctx.getComponent());
                    consumer.consume(message);
                } catch (Exception e) {
                    logProcessFailure(message);
                    throw e;
                }
            }
        }

    }

    private class WriteExec extends AbstractExec {

        public WriteExec(MessagingExecContext ctx) {
            super(ctx);
        }

        @Override
        protected void doRun() throws Exception {
            MessagingProducer producer = getComponent(MessagingProducer.class, ctx.getComponent());
            List<Message> messages = producer.produce(ctx.getConfig(), ctx.getTarget());
            if (!DataUtils.isBlank(messages)) {
                logDebug("Messaging write execution [{0}]...", execId);
                for (Message message : messages) {
                    try {
                        sendMessage(message);
                    } catch (UnifyException e) {
                        logProcessFailure(message);
                        logError(e);
                    }
                }
            }
        }

    }

    private abstract class AbstractExec implements Runnable {

        protected final MessagingExecContext ctx;

        protected final String execId;

        public AbstractExec(MessagingExecContext ctx) {
            this.ctx = ctx;
            this.execId = generateRandomUUID();
        }

        @Override
        public void run() {
            if (ctx.incLoading()) {
                try {
                    doRun();
                } catch (Exception e) {
                    logError(e);
                } finally {
                    ctx.decLoading();
                }
            }
        }

        protected abstract void doRun() throws Exception;

    }

}
