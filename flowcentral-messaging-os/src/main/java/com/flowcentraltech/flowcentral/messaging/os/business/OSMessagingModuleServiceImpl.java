/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.messaging.os.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleSysParamConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAsyncResponse;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingSourceDef;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingTargetDef;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsync;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingLog;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingSource;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingSourceQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingTarget;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingTargetQuery;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.business.AbstractQueuedExec;
import com.tcdng.unify.core.business.QueuedExec;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.StaleableFactoryMap;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.EncodingUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.PostResp;
import com.tcdng.unify.web.http.HttpRequestHeaderConstants;

/**
 * Implementation of OS messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(OSMessagingModuleNameConstants.OSMESSAGING_MODULE_SERVICE)
public class OSMessagingModuleServiceImpl extends AbstractFlowCentralService implements OSMessagingModuleService {

    private static final String PROCESS_MESSAGE_ASYNC = "os::processmessageasync";

    private static final String BASIC_AUTH_PREFIX = "Basic ";

    private static final int MAX_MESSAGING_THREADS = 32;

    private static final int MAX_PROCESSING_BATCH_SIZE = 512;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    private FactoryMap<String, OSMessagingTargetDef> osTargetDefFactoryMap;

    private FactoryMap<String, OSMessagingSourceDef> osSourceDefFactoryMap;

    private FactoryMap<String, OSMessagingHeader> osHeaderFactoryMap;

    private final QueuedExec<Long> queuedExec;

    public OSMessagingModuleServiceImpl() {

        this.osTargetDefFactoryMap = new StaleableFactoryMap<String, OSMessagingTargetDef>()
            {

                @Override
                protected boolean stale(String target, OSMessagingTargetDef osTargetDef) throws Exception {
                    return isStale(new OSMessagingTargetQuery(), osTargetDef);
                }

                @Override
                protected OSMessagingTargetDef create(String target, Object... arg1) throws Exception {
                    final String source = getContainerSetting(String.class,
                            FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_APPID);
                    final OSMessagingTarget osTarget = environment().find(new OSMessagingTargetQuery().name(target));
                    return new OSMessagingTargetDef(osTarget.getId(), osTarget.getName(), osTarget.getDescription(),
                            osTarget.getTargetUrl(), osTarget.getPassword(), osTarget.getStatus(),
                            osTarget.getVersionNo(), source);
                }

            };

        this.osSourceDefFactoryMap = new StaleableFactoryMap<String, OSMessagingSourceDef>()
            {

                @Override
                protected boolean stale(String source, OSMessagingSourceDef osSourceDef) throws Exception {
                    return isStale(new OSMessagingSourceQuery(), osSourceDef);
                }

                @Override
                protected OSMessagingSourceDef create(String source, Object... arg1) throws Exception {
                    final OSMessagingSource osSource = environment().find(new OSMessagingSourceQuery().name(source));
                    if (osSource == null) {
                        throw new IllegalArgumentException("Message source [" + source + "] is unknown.");
                    }
                    
                    return new OSMessagingSourceDef(osSource.getId(), osSource.getName(), osSource.getDescription(),
                            osSource.getPassword(), osSource.getStatus(), osSource.getVersionNo());
                }

            };

        this.osHeaderFactoryMap = new StaleableFactoryMap<String, OSMessagingHeader>()
            {

                @Override
                protected boolean stale(String authorization, OSMessagingHeader osHeader) throws Exception {
                    OSMessagingSourceDef osSourceDef = osSourceDefFactoryMap.get(osHeader.getSource());
                    return osHeader.getVersionNo() != osSourceDef.getVersionNo();
                }

                @Override
                protected OSMessagingHeader create(String authorization, Object... arg1) throws Exception {
                    if (authorization.startsWith(BASIC_AUTH_PREFIX)) {
                        try {
                            final String credentials = EncodingUtils
                                    .decodeBase64String(authorization.substring(BASIC_AUTH_PREFIX.length()));
                            String[] parts = credentials.split(":", 2);
                            String[] nparts = parts[0].split("\\.", 2);

                            final String source = nparts[0];
                            final String processor = nparts[1];
                            final String password = parts[1];

                            OSMessagingSourceDef osSourceDef = osSourceDefFactoryMap.get(source);
                            if (osSourceDef.getPassword().equals(password) && isComponent(processor)
                                    && getComponent(processor) instanceof OSMessagingProcessor) {
                                return new OSMessagingHeader(source, processor, osSourceDef.getVersionNo());
                            }
                        } catch (Exception e) {
                            logError(e);
                        }
                    }

                    throw new IllegalArgumentException("Invalid authorization.");
                }

            };

        this.queuedExec = new AbstractQueuedExec<Long>(MAX_MESSAGING_THREADS)
            {

                @Override
                protected void doExecute(Long osMessagingAsyncId) {
                    OSMessagingAsync osMessagingAsync;
                    try {
                        osMessagingAsync = environment().find(OSMessagingAsync.class, osMessagingAsyncId);
                    } catch (UnifyException e) {
                        logError(e);
                        return;
                    }

                    try {
                        OSMessagingAsyncResponse resp = sendMessage(OSMessagingAsyncResponse.class,
                                osMessagingAsync.getTarget(), osMessagingAsync.getProcessor(),
                                osMessagingAsync.getMessage());
                        environment().updateById(OSMessagingAsync.class, osMessagingAsyncId,
                                new Update().add("processing", Boolean.FALSE).add("sentOn", getNow())
                                        .add("responseCode", resp.getResponseCode())
                                        .add("responseMsg", resp.getResponseMessage()));
                    } catch (UnifyException e) {
                        logError(e);
                        try {
                            final Date nextAttemptOn = CalendarUtils.getDateWithFrequencyOffset(getNow(),
                                    FrequencyUnit.SECOND, 60);
                            environment().updateById(OSMessagingAsync.class, osMessagingAsyncId,
                                    new Update().add("nextAttemptOn", nextAttemptOn).add("processing", Boolean.FALSE));
                        } catch (UnifyException e1) {
                            logError(e);
                        }
                    }
                }

            };
    }

    @Override
    public OSMessagingHeader getOSMessagingHeader(String authorization) throws UnifyException {
        return osHeaderFactoryMap.get(authorization);
    }

    @Override
    public <T extends BaseOSMessagingResp, U extends BaseOSMessagingReq> T sendSynchronousMessage(Class<T> respClass,
            U request, String target, String processor) throws UnifyException {
        return sendMessage(respClass, target, processor, request);
    }

    @Override
    public <T extends BaseOSMessagingReq> void sendAsynchronousMessage(T request, String target, String processor)
            throws UnifyException {
        sendAsynchronousMessage(request, target, processor, 0L);
    }

    @Override
    public <T extends BaseOSMessagingReq> void sendAsynchronousMessage(T request, String target, String processor,
            long delayInSeconds) throws UnifyException {
        final Date nextAttemptOn = CalendarUtils.getDateWithFrequencyOffset(getNow(), FrequencyUnit.SECOND,
                delayInSeconds <= 0 ? 0 : delayInSeconds);
        OSMessagingAsync osMessagingAsync = new OSMessagingAsync();
        osMessagingAsync.setTarget(target);
        osMessagingAsync.setProcessor(processor);
        osMessagingAsync.setMessage(DataUtils.asJsonString(request, PrintFormat.NONE));
        osMessagingAsync.setNextAttemptOn(nextAttemptOn);
        environment().create(osMessagingAsync);
    }

    @Synchronized(PROCESS_MESSAGE_ASYNC)
    @Periodic(PeriodicType.FASTER)
    public void processMessageAsync(TaskMonitor taskMonitor) throws UnifyException {
        final List<Long> pendingList = environment().valueList(Long.class, "id", new OSMessagingAsyncQuery()
                .isDue(getNow()).isNotProcessing().setLimit(MAX_PROCESSING_BATCH_SIZE).addOrder("id"));
        if (!DataUtils.isBlank(pendingList)) {
            environment().updateAll(new OSMessagingAsyncQuery().idIn(pendingList),
                    new Update().add("processing", Boolean.TRUE));
            keepThreadAndClusterSafe("processBefore", new OSMessagingAsyncQuery().isProcessing().idIn(pendingList));
            for (Long osMessagingAsyncId : pendingList) {
                queuedExec.execute(osMessagingAsyncId);
            }
        }
    }

    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, ModuleInstall moduleInstall)
            throws UnifyException {

    }

    protected <T extends BaseOSMessagingResp> T sendMessage(Class<T> respClass, String target, String processor,
            Object message) throws UnifyException {
        final OSMessagingTargetDef osTargetDef = osTargetDefFactoryMap.get(target);
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpRequestHeaderConstants.AUTHORIZATION, osTargetDef.getAuthentication(processor));
        PostResp<T> resp = IOUtils.postObjectToEndpointUsingJson(respClass, osTargetDef.getTargetUrl(), message,
                headers);
        T result = extractResult(resp);
        if (systemModuleService.getSysParameterValue(boolean.class,
                OSMessagingModuleSysParamConstants.MESSAGE_LOGGING_ENABLED)) {
            OSMessagingLog log = new OSMessagingLog();
            log.setTarget(target);
            log.setProcessor(processor);
            log.setRequestBody(resp.getReqJson());
            log.setResponseBody(resp.getRespJson());
            log.setResponseCode(result.getResponseCode());
            log.setResponseMsg(result.getResponseMessage());
            log.setRuntimeInMilliSec(resp.getExecMilliSec());
            environment().create(log);
        }

        return result;
    }

}
