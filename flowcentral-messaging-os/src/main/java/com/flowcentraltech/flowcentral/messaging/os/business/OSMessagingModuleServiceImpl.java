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
import java.util.Optional;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingMode;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleSysParamConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSCredentials;
import com.flowcentraltech.flowcentral.messaging.os.data.OSInfo;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAsyncResponse;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingPeerEndpointDef;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingRequestHeaderConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.OSResponse;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsync;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingLog;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpoint;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpointQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingProcessingLog;
import com.flowcentraltech.flowcentral.messaging.os.util.OSMessagingUtils;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.business.AbstractQueuedExec;
import com.tcdng.unify.core.business.QueuedExec;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.StaleableFactoryMap;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.PostResp;
import com.tcdng.unify.core.util.RandomUtils;
import com.tcdng.unify.core.util.StringUtils;

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

    private static final int MAX_MESSAGING_THREADS = 32;

    private static final int MAX_PROCESSING_BATCH_SIZE = 512;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    @Configurable
    private OSAsyncMessagingErrorProcessor osAsyncMessagingErrorProcessor;
    
    private final FactoryMap<String, OSMessagingPeerEndpointDef> osPeerEndpointDefFactoryMap;

    private final FactoryMap<String, OSMessagingHeader> osHeaderFactoryMap;

    private final QueuedExec<Long> queuedExec;

    private OSInfo osInfo;

    public OSMessagingModuleServiceImpl() {

        this.osPeerEndpointDefFactoryMap = new StaleableFactoryMap<String, OSMessagingPeerEndpointDef>()
            {

                @Override
                protected boolean stale(String target, OSMessagingPeerEndpointDef osPeerEndpointDef) throws Exception {
                    return isStale(new OSMessagingPeerEndpointQuery(), osPeerEndpointDef);
                }

                @Override
                protected OSMessagingPeerEndpointDef create(String target, Object... arg1) throws Exception {
                    final String source = getContainerSetting(String.class,
                            FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_APPID);
                    final OSMessagingPeerEndpoint osPeerEndpoint = environment()
                            .find(new OSMessagingPeerEndpointQuery().appId(target));
                    return osPeerEndpoint == null ? OSMessagingPeerEndpointDef.BLANK
                            : new OSMessagingPeerEndpointDef(osPeerEndpoint.getId(), osPeerEndpoint.getAppId(),
                                    osPeerEndpoint.getShortName(), osPeerEndpoint.getName(), osPeerEndpoint.getDescription(),
                                    osPeerEndpoint.getEndpointUrl(), osPeerEndpoint.getPeerPassword(),
                                    osPeerEndpoint.getStatus(), osPeerEndpoint.getVersionNo(), source);
                }

            };

        this.osHeaderFactoryMap = new StaleableFactoryMap<String, OSMessagingHeader>()
            {

                @Override
                protected boolean stale(String authorization, OSMessagingHeader osHeader) throws Exception {
                    OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap
                            .get(osHeader.getSource());
                    return osPeerEndpointDef.isPresent() && osHeader.getVersionNo() != osPeerEndpointDef.getVersionNo();
                }

                @Override
                protected OSMessagingHeader create(String authorization, Object... arg1) throws Exception {
                    if (OSMessagingUtils.isBasicAuthorization(authorization)) {
                        try {
                            OSCredentials credentials = OSMessagingUtils.getOSCredentials(authorization);
                            OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap
                                    .get(credentials.getSource());
                            if (osPeerEndpointDef.isPresent()
                                    && osPeerEndpointDef.getPeerPassword().equals(credentials.getPassword())
                                    && isComponent(credentials.getProcessor())
                                    && getComponent(credentials.getProcessor()) instanceof OSMessagingProcessor) {
                                return new OSMessagingHeader(credentials.getSource(), credentials.getProcessor(),
                                        osPeerEndpointDef.getVersionNo());
                            }
                        } catch (Exception e) {
                            logError(e);
                        }
                    }

                    return OSMessagingHeader.BLANK;
                }

            };

        this.queuedExec = new AbstractQueuedExec<Long>(MAX_MESSAGING_THREADS)
            {

                @Override
                protected void doExecute(Long osMessagingAsyncId) {
                    try {
                        sendAsynchronousMessage(osMessagingAsyncId);
                    } catch (UnifyException e) {
                        logError(e);
                    }
                }

            };
    }

    @Override
    public OSInfo getOSInfo() throws UnifyException {
        return osInfo;
    }

    @Override
    public Optional<String> getPeerEndpointShortName(String appId) throws UnifyException {
        return environment().valueOptional(String.class, "shortName", new OSMessagingPeerEndpointQuery().appId(appId));
    }

    @Override
    public List<OSMessagingPeerEndpoint> findOSMessagingEndpoints(OSMessagingPeerEndpointQuery query)
            throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public OSMessagingHeader getOSMessagingHeader(String authorization) throws UnifyException {
        return osHeaderFactoryMap.get(authorization);
    }

    @Override
    public void logProcessing(OSMessagingMode mode, String correlationdId, String source, String processor,
            String summary, String responseCode, String responseMsg) throws UnifyException {
        final OSMessagingProcessingLog log = new OSMessagingProcessingLog(mode, correlationdId, source, processor,
                summary, responseCode, responseMsg, getNow());
        environment().create(log);
    }

    @Override
    public Optional<String> sendSynchronousMessageToDelegate(OSMessagingHeader header, String function,
            String correlationId, String requestJson) throws UnifyException {
        Optional<String> target = osMessagingAccessManager.resolveDelegateFunctionTarget(function);
        if (target.isPresent()) {
            return Optional.ofNullable(
                    sendMessage(target.get(), header.getProcessor(), null, null, correlationId, requestJson, true));
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> sendAsynchronousMessageToDelegate(OSMessagingHeader header, String function,
            String correlationId, String requestJson) throws UnifyException {
        Optional<String> target = osMessagingAccessManager.resolveDelegateFunctionTarget(function);
        if (target.isPresent()) {
            return Optional.ofNullable(
                    sendMessage(target.get(), header.getProcessor(), null, null, correlationId, requestJson, false));
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> sendSynchronousMessageToService(OSMessagingHeader header, String service,
            String correlationId, String requestJson) throws UnifyException {
        return Optional
                .ofNullable(sendMessage(service, header.getProcessor(), null, null, correlationId, requestJson, true));
    }

    @Override
    public Optional<String> sendAsynchronousMessageToService(OSMessagingHeader header, String service,
            String correlationId, String requestJson) throws UnifyException {
        return Optional
                .ofNullable(sendMessage(service, header.getProcessor(), null, null, correlationId, requestJson, false));
    }

    @Override
    public <T extends BaseOSMessagingResp, U extends BaseOSMessagingReq> T sendSynchronousMessage(Class<T> respClass,
            U request) throws UnifyException {
        if (StringUtils.isBlank(request.getCorrelationId())) {
            request.setCorrelationId(RandomUtils.generateUUIDInBase64());
        }

        request.setSource(osInfo.getServiceId());
        request.setSourceVersion(osInfo.getServiceVersion());
        if (request.getOriginSource() == null) {
            request.setOriginSource(osInfo.getServiceId());
        }

        final String reqJson = prettyJson(request);
        return sendMessage(respClass, request.getTarget(), request.getProcessor(), request.getFunction(),
                request.getService(), request.getCorrelationId(), reqJson, true);
    }

    @Override
    public <T extends BaseOSMessagingReq> String sendAsynchronousMessage(T request) throws UnifyException {
        return sendAsynchronousMessage(request, 0L);
    }

    @Override
    public <T extends BaseOSMessagingReq> String sendAsynchronousMessage(T request, long delayInSeconds)
            throws UnifyException {
        if (StringUtils.isBlank(request.getCorrelationId())) {
            request.setCorrelationId(RandomUtils.generateUUIDInBase64());
        }

        request.setSource(osInfo.getServiceId());
        request.setSourceVersion(osInfo.getServiceVersion());
        if (request.getOriginSource() == null) {
            request.setOriginSource(osInfo.getServiceId());
        }

        final String reqJson = prettyJson(request);
        final Date nextAttemptOn = CalendarUtils.getDateWithFrequencyOffset(getNow(), FrequencyUnit.SECOND,
                delayInSeconds <= 0 ? 0 : delayInSeconds);
        OSMessagingAsync osMessagingAsync = new OSMessagingAsync();
        osMessagingAsync.setTarget(request.getTarget());
        osMessagingAsync.setCorrelationId(request.getCorrelationId());
        osMessagingAsync.setProcessor(request.getProcessor());
        osMessagingAsync.setFunction(request.getFunction());
        osMessagingAsync.setService(request.getService());
        osMessagingAsync.setMessage(reqJson);
        osMessagingAsync.setNextAttemptOn(nextAttemptOn);
        environment().create(osMessagingAsync);

        return request.getCorrelationId();
    }

    @Override
    public OSResponse getAsynchronousAck(String correlationdId) throws UnifyException {
        OSMessagingAsync async = environment().find(
                new OSMessagingAsyncQuery().correlationId(correlationdId).addSelect("responseCode", "responseMsg"));
        if (async != null && async.getResponseCode() != null) {
            return new OSResponse(async.getResponseCode(), async.getResponseMsg());
        }

        return OSResponse.BLANK;
    }

    @Periodic(PeriodicType.FAST)
    public void processMessageAsync(TaskMonitor taskMonitor) throws UnifyException {
        if (tryGrabLock(PROCESS_MESSAGE_ASYNC)) {
            try {
                final Date now = getNow();

                // Recover dead
                environment().updateAll(new OSMessagingAsyncQuery().isDead(now), new Update()
                        .add("processing", Boolean.FALSE).add("nextAttemptOn", now).add("processBefore", null));
                commitTransactions();

                // New items for processing
                final List<Long> pendingList = environment().valueList(Long.class, "id",
                        new OSMessagingAsyncQuery().isDue(now).isResponseNull().isNotProcessing()
                                .setLimit(MAX_PROCESSING_BATCH_SIZE).addOrder("id"));
                if (!DataUtils.isBlank(pendingList)) {
                    logDebug("Processing asynchronous [{0}] messages...", pendingList.size());
                    environment().updateAll(new OSMessagingAsyncQuery().idIn(pendingList),
                            new Update().add("processing", Boolean.TRUE));
                    keepThreadAndClusterSafe("processBefore",
                            new OSMessagingAsyncQuery().isResponseNull().isProcessing().idIn(pendingList));
                    for (Long osMessagingAsyncId : pendingList) {
                        queuedExec.execute(osMessagingAsyncId);
                    }
                }
            } finally {
                releaseLock(PROCESS_MESSAGE_ASYNC);
            }
        }
    }

    public void sendAsynchronousMessage(Long osMessagingAsyncId) throws UnifyException {
        OSMessagingAsync osMessagingAsync;
        try {
            osMessagingAsync = environment().find(OSMessagingAsync.class, osMessagingAsyncId);
        } catch (UnifyException e) {
            logError(e);
            return;
        }

        try {
            OSMessagingAsyncResponse resp = sendMessage(OSMessagingAsyncResponse.class, osMessagingAsync.getTarget(),
                    osMessagingAsync.getProcessor(), osMessagingAsync.getFunction(), osMessagingAsync.getService(),
                    osMessagingAsync.getCorrelationId(), osMessagingAsync.getMessage(), false);
            environment().updateById(OSMessagingAsync.class, osMessagingAsyncId,
                    new Update().add("processing", Boolean.FALSE).add("sentOn", getNow())
                            .add("responseCode", resp.getResponseCode()).add("responseMsg", resp.getResponseMessage()));
            commitTransactions();

            if (osAsyncMessagingErrorProcessor != null && !resp.isSuccessful()) {
                osAsyncMessagingErrorProcessor.handleError(osMessagingAsync.getTarget(),
                        osMessagingAsync.getProcessor(), osMessagingAsync.getCorrelationId(), resp.getResponseCode(),
                        resp.getResponseMessage());
            }
        } catch (UnifyException e) {
            if (UnifyCoreErrorConstants.IOUTIL_STREAM_RW_ERROR.equals(e.getErrorCode())) {
                logDebug(e);
            } else {
                logError(e);
            }

            try {
                final Date nextAttemptOn = CalendarUtils.getDateWithFrequencyOffset(getNow(), FrequencyUnit.SECOND, 60);
                environment().updateById(OSMessagingAsync.class, osMessagingAsyncId,
                        new Update().add("nextAttemptOn", nextAttemptOn).add("processing", Boolean.FALSE));
            } catch (UnifyException e1) {
                logError(e);
            }
        }
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        final String serviceId = getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_APPID);
        final String vendorName = getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_VENDORNAME);
        final String vendorDomain = getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_VENDORDOMAIN);
        final String serviceVersion = getDeploymentVersion();
        osInfo = new OSInfo(serviceId, serviceVersion, vendorName, vendorDomain);
    }

    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, ModuleInstall moduleInstall)
            throws UnifyException {

    }
    
    private <T extends BaseOSMessagingResp> T sendMessage(Class<T> respClass, final String target,
            final String processor, final String function, final String service, final String correlationId,
            final String reqJson, final boolean sync) throws UnifyException {
        return fromJson(respClass, sendMessage(target, processor, function, service, correlationId, reqJson, sync));
    }

    private String sendMessage(final String target, final String processor, final String function, final String service,
            final String correlationId, String reqJson, boolean sync) throws UnifyException {
        logDebug(sync ? "Sending synchronous message [\n{0}]..." : "Sending asynchronous message [\n{0}]...", reqJson);
        final OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap.get(target);
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(OSMessagingRequestHeaderConstants.AUTHORIZATION, osPeerEndpointDef.getAuthentication(processor));
        headers.put(OSMessagingRequestHeaderConstants.CORRELATION_ID, correlationId);
        headers.put(OSMessagingRequestHeaderConstants.ROUTING_TYPE, sync ? "sync" : "async");
        if (!StringUtils.isBlank(function)) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_FUNCTION, function);
        }

        if (!StringUtils.isBlank(service)) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_SERVICE, service);
        }

        logDebug("Message headers [{0}]...", headers);
        PostResp<String> resp = IOUtils.postJsonToEndpoint(osPeerEndpointDef.getEndpointUrl(), reqJson, headers);
        if (resp.isError()) {
            throwOperationErrorException(new Exception(resp.getError()));
        }

        if (systemModuleService.getSysParameterValue(boolean.class,
                OSMessagingModuleSysParamConstants.MESSAGE_LOGGING_ENABLED)) {
            OSMessagingLog log = new OSMessagingLog();
            log.setTarget(target);
            log.setProcessor(processor);
            log.setCorrelationId(correlationId);
            log.setRequestBody(resp.getReqJson());
            log.setResponseBody(resp.getRespJson());
            log.setRuntimeInMilliSec(resp.getExecMilliSec());
            environment().create(log);
        }

        logDebug("Response message [\n{0}]", resp.getRespJson());
        return resp.getRespJson();
    }

}
