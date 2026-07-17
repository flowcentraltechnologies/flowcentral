/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.InactiveTargetResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSCredentials;
import com.flowcentraltech.flowcentral.messaging.os.data.OSInfo;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAsyncResponse;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingPeerEndpointDef;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingPeerInfo;
import com.flowcentraltech.flowcentral.messaging.os.data.UnknownTargetResp;
import com.flowcentraltech.flowcentral.messaging.os.data.constants.OSMessagingFunction;
import com.flowcentraltech.flowcentral.messaging.os.data.constants.OSMessagingRequestHeaderConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.local.OSDownloadLocalController;
import com.flowcentraltech.flowcentral.messaging.os.data.local.OSMessagingLocalController;
import com.flowcentraltech.flowcentral.messaging.os.data.local.OSUploadLocalController;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncIn;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncInQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncOut;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncOutQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpoint;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpointQuery;
import com.flowcentraltech.flowcentral.messaging.os.util.OSMessagingUtils;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.business.AbstractQueuedExec;
import com.tcdng.unify.core.business.QueuedExec;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.StaleableFactoryMap;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.PostResp;
import com.tcdng.unify.core.util.RandomUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.util.ContentDisposition;
import com.tcdng.unify.web.util.HttpUtils;

/**
 * Implementation of OS messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(OSMessagingModuleNameConstants.OSMESSAGING_MODULE_SERVICE)
public class OSMessagingModuleServiceImpl extends AbstractFlowCentralService implements OSMessagingModuleService {

    private static final String PROCESS_OUTGOING_MESSAGE_ASYNC = "os::processoutmessageasync";

    private static final String PROCESS_INCOMING_MESSAGE_ASYNC = "os::processinmessageasync";

    private static final long BASE_OUTGOING_TIMEOUT = 5 * 1000L; // 5 seconds

    private static final long MESSAGE_OUTGOING_OVERHEAD = 250L; // .25 seconds

    private static final long BASE_INCOMING_TIMEOUT = 5 * 1000L; // 5 seconds

    private static final long MESSAGE_INCOMING_OVERHEAD = 10 * 1000L; // 10 seconds

    private static final int MAX_MESSAGING_THREADS = 32;

    private static final int MAX_PROCESSING_BATCH_SIZE = 128;

    @Configurable
    private OSUploadLocalController osUploadLocalController;

    @Configurable
    private OSDownloadLocalController osDownloadLocalController;

    @Configurable
    private OSMessagingLocalController osMessagingLocalController;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private OSMessagingAccessManager osMessagingAccessManager;

    @Configurable
    private OSAsyncMessagingErrorProcessor osAsyncMessagingErrorProcessor;

    @Configurable
    private JsonObjectStreamer jsonObjectStreamer;

    private final FactoryMap<String, OSMessagingPeerEndpointDef> osPeerEndpointDefFactoryMap;

    private final FactoryMap<String, OSMessagingHeader> osHeaderFactoryMap;

    private final QueuedExec<List<Long>> outgoingQueuedExec;

    private final QueuedExec<List<Long>> incomingQueuedExec;

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
                                    osPeerEndpoint.getShortName(), osPeerEndpoint.getName(),
                                    osPeerEndpoint.getDescription(), osPeerEndpoint.getEndpointUrl(),
                                    osPeerEndpoint.getPeerPassword(), osPeerEndpoint.getStatus(),
                                    osPeerEndpoint.isLocalTarget(), osPeerEndpoint.getVersionNo(), source);
                }

            };

        this.osHeaderFactoryMap = new StaleableFactoryMap<String, OSMessagingHeader>()
            {

                @Override
                protected boolean stale(String authorization, OSMessagingHeader osHeader) throws Exception {
                    if (!StringUtils.isBlank(osHeader.getSource())) {
                        OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap
                                .get(osHeader.getSource());
                        return osPeerEndpointDef.isPresent()
                                && osHeader.getVersionNo() != osPeerEndpointDef.getVersionNo();
                    }

                    return true;
                }

                @Override
                protected OSMessagingHeader create(String authorization, Object... arg1) throws Exception {
                    if (OSMessagingUtils.isBasicAuthorization(authorization)) {
                        try {
                            OSCredentials credentials = OSMessagingUtils.getOSCredentials(authorization);
                            OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap
                                    .get(credentials.getSource());
                            if (osPeerEndpointDef.isPresent()
                                    && osPeerEndpointDef.getPeerPassword().equals(credentials.getPassword())) {
                                return new OSMessagingHeader(credentials.getSource(), credentials.getProcessor(),
                                        isComponent(credentials.getProcessor()) && (getComponent(
                                                credentials.getProcessor()) instanceof OSMessagingProcessor
                                                || getComponent(credentials.getProcessor()) instanceof OSUploadProcessor
                                                || getComponent(
                                                        credentials.getProcessor()) instanceof OSDownloadProcessor),
                                        osPeerEndpointDef.getVersionNo());
                            }
                        } catch (Exception e) {
                            logError(e);
                        }
                    }

                    return OSMessagingHeader.BLANK;
                }

            };

        this.outgoingQueuedExec = new AbstractQueuedExec<List<Long>>(MAX_MESSAGING_THREADS)
            {
                @Override
                protected void doExecute(List<Long> osMessagingAsyncIds) {
                    try {
                        for (Long osMessagingAsyncId : osMessagingAsyncIds) {
                            if (!sendAsynchronousMessage(osMessagingAsyncId)) {
                                break; // Stop sending to target
                            }
                        }
                    } catch (UnifyException e) {
                        logError(e);
                    }
                }

            };

        this.incomingQueuedExec = new AbstractQueuedExec<List<Long>>(MAX_MESSAGING_THREADS)
            {
                @Override
                protected void doExecute(List<Long> osMessagingAsyncIds) {
                    for (Long osMessagingAsyncId : osMessagingAsyncIds) {
                        try {
                            processAsynchronousMessage(osMessagingAsyncId);
                        } catch (UnifyException e) {
                            logError(e);
                        }
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
    public Optional<String> getPeerEndpointURL(String appId) throws UnifyException {
        return environment().valueOptional(String.class, "endpointUrl",
                new OSMessagingPeerEndpointQuery().appId(appId));
    }

    @Override
    public void updateOSMessagingEndpoints(List<OSMessagingPeerInfo> messagingPeerInfoList) throws UnifyException {
        final Set<String> names = new HashSet<String>();
        for (OSMessagingPeerInfo osMessagingPeerInfo : messagingPeerInfoList) {
            OSMessagingPeerEndpoint osMessagingPeerEndpoint = environment()
                    .find(new OSMessagingPeerEndpointQuery().name(osMessagingPeerInfo.getName()));
            if (osMessagingPeerEndpoint == null) {
                osMessagingPeerEndpoint = new OSMessagingPeerEndpoint();
                osMessagingPeerEndpoint.setAppId(osMessagingPeerInfo.getAppId());
                osMessagingPeerEndpoint.setName(osMessagingPeerInfo.getName());
                osMessagingPeerEndpoint.setDescription(osMessagingPeerInfo.getDescription());
                osMessagingPeerEndpoint.setEndpointUrl(osMessagingPeerInfo.getEndpointUrl());
                osMessagingPeerEndpoint.setPeerPassword(osMessagingPeerInfo.getPeerPassword());
                osMessagingPeerEndpoint.setShortName(osMessagingPeerInfo.getShortName());
                osMessagingPeerEndpoint.setLocalTarget(osMessagingPeerInfo.isLocalTarget());
                osMessagingPeerEndpoint.setStatus(RecordStatus.ACTIVE);
                environment().create(osMessagingPeerEndpoint);
            } else {
                osMessagingPeerEndpoint.setAppId(osMessagingPeerInfo.getAppId());
                osMessagingPeerEndpoint.setName(osMessagingPeerInfo.getName());
                osMessagingPeerEndpoint.setDescription(osMessagingPeerInfo.getDescription());
                osMessagingPeerEndpoint.setEndpointUrl(osMessagingPeerInfo.getEndpointUrl());
                osMessagingPeerEndpoint.setPeerPassword(osMessagingPeerInfo.getPeerPassword());
                osMessagingPeerEndpoint.setShortName(osMessagingPeerInfo.getShortName());
                osMessagingPeerEndpoint.setLocalTarget(osMessagingPeerInfo.isLocalTarget());
                osMessagingPeerEndpoint.setStatus(RecordStatus.ACTIVE);
                environment().updateByIdVersion(osMessagingPeerEndpoint);
            }

            names.add(osMessagingPeerInfo.getName());
        }

        environment().deleteAll(new OSMessagingPeerEndpointQuery().nameNotIn(names));
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
    public Optional<String> sendSynchronousMessageToDelegate(OSMessagingHeader header, OSMessagingFunction function,
            String correlationId, String userLoginId, String requestJson) throws UnifyException {
        Optional<String> target = osMessagingAccessManager.resolveDelegateFunctionTarget(function);
        if (target.isPresent()) {
            return Optional.ofNullable(sendMessage(target.get(), header.getProcessor(), null, null, correlationId,
                    userLoginId, requestJson, true));
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> sendSynchronousMessageToService(OSMessagingHeader header, String service,
            String correlationId, String userLoginId, String requestJson) throws UnifyException {
        return Optional.ofNullable(
                sendMessage(service, header.getProcessor(), null, null, correlationId, userLoginId, requestJson, true));
    }

    @Override
    public Optional<String> sendAsynchronousMessageToDelegate(OSMessagingHeader header, OSMessagingFunction function,
            String correlationId, String userLoginId, String requestJson) throws UnifyException {
        Optional<String> target = osMessagingAccessManager.resolveDelegateFunctionTarget(function);
        if (target.isPresent()) {
            return Optional.ofNullable(sendMessage(target.get(), header.getProcessor(), null, null, correlationId,
                    userLoginId, requestJson, false));
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> sendAsynchronousMessageToService(OSMessagingHeader header, String service,
            String correlationId, String userLoginId, String requestJson) throws UnifyException {
        return Optional.ofNullable(sendMessage(service, header.getProcessor(), null, null, correlationId, userLoginId,
                requestJson, false));
    }

    @Override
    public Optional<String> sendUploadMessageToDelegate(OSMessagingHeader header, OSMessagingFunction function,
            String correlationId, String userLoginId, String fileSignature, String fileChecksum,
            ContentDisposition disposition, InputStream in) throws UnifyException {
        Optional<String> target = osMessagingAccessManager.resolveDelegateFunctionTarget(function);
        if (target.isPresent()) {
            return Optional.ofNullable(sendUploadMessage(target.get(), header.getProcessor(), null, null, correlationId,
                    userLoginId, fileSignature, fileChecksum, disposition, in));
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> sendUploadMessageToService(OSMessagingHeader header, String service, String correlationId,
            String userLoginId, String fileSignature, String fileChecksum, ContentDisposition disposition,
            InputStream in) throws UnifyException {
        return Optional.ofNullable(sendUploadMessage(service, header.getProcessor(), null, null, correlationId,
                userLoginId, fileSignature, fileChecksum, disposition, in));
    }

    @Override
    public Optional<String> sendDownloadMessageToDelegate(OSMessagingHeader header, OSMessagingFunction function,
            String correlationId, String userLoginId, String fileSignature, OutputStream out) throws UnifyException {
        Optional<String> target = osMessagingAccessManager.resolveDelegateFunctionTarget(function);
        if (target.isPresent()) {
            return Optional.ofNullable(sendDownloadMessage(target.get(), header.getProcessor(), null, null,
                    correlationId, userLoginId, fileSignature, out));
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> sendDownloadMessageToService(OSMessagingHeader header, String service, String correlationId,
            String userLoginId, String fileSignature, OutputStream out) throws UnifyException {
        return Optional.ofNullable(sendDownloadMessage(service, header.getProcessor(), null, null, userLoginId,
                correlationId, fileSignature, out));
    }

    @Override
    public <T extends BaseOSMessagingResp, U extends BaseOSMessagingReq> T sendSynchronousMessage(Class<T> respClass,
            U request) throws UnifyException {
        final String correlationId = RandomUtils.generateUUIDInBase64();

        if (StringUtils.isBlank(request.getUserId())) {
            request.setUserId(getUserLoginId());
        }

        request.setSource(osInfo.getServiceId());
        request.setSourceVersion(osInfo.getServiceVersion());
        if (request.getOriginSource() == null) {
            request.setOriginSource(osInfo.getServiceId());
        }

        final String reqJson = prettyJson(request);
        return sendMessage(respClass, request.getTarget(), request.getProcessor(), request.getFunction(),
                request.getService(), correlationId, request.getUserId(), reqJson, true);
    }

    @Override
    public <T extends BaseOSMessagingReq> String sendAsynchronousMessage(T request) throws UnifyException {
        final String correlationId = RandomUtils.generateUUIDInBase64();

        if (StringUtils.isBlank(request.getUserId())) {
            request.setUserId(getUserLoginId());
        }

        request.setSource(osInfo.getServiceId());
        request.setSourceVersion(osInfo.getServiceVersion());
        if (request.getOriginSource() == null) {
            request.setOriginSource(osInfo.getServiceId());
        }

        final String reqJson = prettyJson(request);
        OSMessagingAsyncOut osMessagingAsync = new OSMessagingAsyncOut();
        osMessagingAsync.setTarget(request.getTarget());
        osMessagingAsync.setCorrelationId(correlationId);
        osMessagingAsync.setUserLoginId(request.getUserId());
        osMessagingAsync.setProcessor(request.getProcessor());
        osMessagingAsync.setFunction(request.getFunction() != null ? request.getFunction().code() : null);
        osMessagingAsync.setService(request.getService());
        osMessagingAsync.setMessage(reqJson);
        environment().create(osMessagingAsync);

        return correlationId;
    }

    @Override
    public void saveIncomingAsynchronousMessage(String processor, String correlationId, String requestJson)
            throws UnifyException {
        Optional<Long> optional = environment().valueOptional(Long.class, "id",
                new OSMessagingAsyncInQuery().correlationId(correlationId));
        if (!optional.isPresent()) {
            OSMessagingAsyncIn async = new OSMessagingAsyncIn();
            async.setCorrelationId(correlationId);
            async.setProcessor(processor);
            async.setMessage(requestJson);
            environment().create(async);
        }
    }

    @Periodic(PeriodicType.FASTER)
    public void processOutgoingAsyncMessages(TaskMonitor taskMonitor) throws UnifyException {
        if (tryGrabLock(PROCESS_OUTGOING_MESSAGE_ASYNC)) {
            try {
                // Get targets
                Set<String> targets = environment().valueSet(String.class, "appId",
                        new OSMessagingPeerEndpointQuery().ignoreEmptyCriteria(true));

                // Concurrent send by target
                long messageCount = 0;
                for (String target : targets) {
                    final List<Long> osMessagingAsyncIdList = environment().valueList(Long.class, "id",
                            new OSMessagingAsyncOutQuery().target(target).isNotSent()
                                    .setLimit(MAX_PROCESSING_BATCH_SIZE).addOrder("id"));
                    if (!DataUtils.isBlank(osMessagingAsyncIdList)) {
                        outgoingQueuedExec.execute(osMessagingAsyncIdList);
                        messageCount += osMessagingAsyncIdList.size();
                    }
                }

                // Wait till messages sent
                if (messageCount > 0) {
                    try {
                        outgoingQueuedExec
                                .waitTillCompleted(BASE_OUTGOING_TIMEOUT + messageCount * MESSAGE_OUTGOING_OVERHEAD);
                    } catch (TimeoutException e) {
                        logError(e);
                    }
                }

            } finally {
                releaseLock(PROCESS_OUTGOING_MESSAGE_ASYNC);
            }
        }
    }

    @Periodic(PeriodicType.FASTER)
    public void processIncomingAsyncMessages(TaskMonitor taskMonitor) throws UnifyException {
        if (tryGrabLock(PROCESS_INCOMING_MESSAGE_ASYNC)) {
            try {
                // Get processors
                Set<String> processors = environment().valueSet(String.class, "processor",
                        new OSMessagingAsyncInQuery().isNotProcessed());

                // Concurrent process by processors
                long messageCount = 0;
                for (String processor : processors) {
                    final List<Long> osMessagingInAsyncIdList = environment().valueList(Long.class, "id",
                            new OSMessagingAsyncInQuery().processor(processor).isNotProcessed()
                                    .setLimit(MAX_PROCESSING_BATCH_SIZE).addOrder("id"));
                    if (!DataUtils.isBlank(osMessagingInAsyncIdList)) {
                        incomingQueuedExec.execute(osMessagingInAsyncIdList);
                        messageCount += osMessagingInAsyncIdList.size();
                    }
                }

                // Wait till messages sent
                if (messageCount > 0) {
                    try {
                        incomingQueuedExec
                                .waitTillCompleted(BASE_INCOMING_TIMEOUT + messageCount * MESSAGE_INCOMING_OVERHEAD);
                    } catch (TimeoutException e) {
                        logError(e);
                    }
                }

            } finally {
                releaseLock(PROCESS_INCOMING_MESSAGE_ASYNC);
            }
        }
    }

    public boolean sendAsynchronousMessage(Long osMessagingAsyncId) throws UnifyException {
        try {
            final Date now = getNow();
            final OSMessagingAsyncOut osMessagingAsyncOut = environment().find(OSMessagingAsyncOut.class,
                    osMessagingAsyncId);
            final OSMessagingAsyncResponse resp = sendMessage(OSMessagingAsyncResponse.class,
                    osMessagingAsyncOut.getTarget(), osMessagingAsyncOut.getProcessor(),
                    OSMessagingFunction.fromCode(osMessagingAsyncOut.getFunction()), osMessagingAsyncOut.getService(),
                    osMessagingAsyncOut.getCorrelationId(), osMessagingAsyncOut.getUserLoginId(),
                    osMessagingAsyncOut.getMessage(), false);
            final Update update = new Update().add("sentOn", now);
            if (!resp.isSuccessful()) {
                update.add("errorCode", resp.getResponseCode()).add("errorMessage", resp.getResponseMessage());
            }

            environment().updateById(OSMessagingAsyncOut.class, osMessagingAsyncId, update);
            return true;
        } catch (Exception e) {
            logError(e);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public void processAsynchronousMessage(Long osMessagingAsyncId) throws UnifyException {
        String errorCode = null;
        String errorMessage = null;
        try {
            final OSMessagingAsyncIn osMessagingAsyncIn = environment().find(OSMessagingAsyncIn.class,
                    osMessagingAsyncId);
            final OSMessagingProcessor<BaseOSMessagingResp, BaseOSMessagingReq> _processor = getComponent(
                    OSMessagingProcessor.class, osMessagingAsyncIn.getProcessor());
            final BaseOSMessagingReq request = jsonObjectStreamer.unmarshal(_processor.getRequestClass(),
                    osMessagingAsyncIn.getMessage());
            final BaseOSMessagingResp resp = _processor.process(request);
            if (!resp.isSuccessful()) {
                errorCode = resp.getResponseCode();
                errorMessage = resp.getResponseMessage();
            }
        } catch (Exception e) {
            errorCode = "X01";
            errorMessage = e.getMessage();
        }

        environment().updateById(OSMessagingAsyncIn.class, osMessagingAsyncId, new Update().add("processedOn", getNow())
                .add("errorCode", errorCode).add("errorMessage", errorMessage));
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
        final boolean debugging = getContainerSetting(boolean.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_DEBUGGING);
        final String hubServiceId = getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_INTEGRATION_SERVICE_ID);
        final String serviceVersion = getDeploymentVersion();
        osInfo = new OSInfo(serviceId, serviceVersion, vendorName, vendorDomain, hubServiceId, debugging);
    }

    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, ModuleInstall moduleInstall)
            throws UnifyException {

    }

    private <T extends BaseOSMessagingResp> T sendMessage(Class<T> respClass, final String target,
            final String processor, final OSMessagingFunction function, final String service,
            final String correlationId, final String userLoginId, final String reqJson, final boolean sync)
            throws UnifyException {
        return fromJson(respClass,
                sendMessage(target, processor, function, service, correlationId, userLoginId, reqJson, sync));
    }

    private String sendMessage(final String target, final String processor, final OSMessagingFunction function,
            final String service, final String correlationId, final String userLoginId, String reqJson, boolean sync)
            throws UnifyException {
        if (osInfo.isDebugging()) {
            logDebug(sync ? "Sending synchronous message [\n{0}]..." : "Sending asynchronous message [\n{0}]...",
                    reqJson);
        }

        final OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap.get(target);
        if (!osPeerEndpointDef.isPresent()) {
            return prettyJson(new UnknownTargetResp(target));
        }

        if (!osPeerEndpointDef.isActive()) {
            return prettyJson(InactiveTargetResp.MESSAGE);
        }

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(OSMessagingRequestHeaderConstants.AUTHORIZATION, osPeerEndpointDef.getAuthentication(processor));
        headers.put(OSMessagingRequestHeaderConstants.CORRELATION_ID, correlationId);
        headers.put(OSMessagingRequestHeaderConstants.ROUTING_TYPE, sync ? "sync" : "async");
        if (!StringUtils.isBlank(userLoginId)) {
            headers.put(OSMessagingRequestHeaderConstants.USER_ID, userLoginId);
        }

        if (function != null) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_FUNCTION, function.code());
        }

        if (!StringUtils.isBlank(service)) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_SERVICE, service);
        }

        PostResp<String> resp = osPeerEndpointDef.isLocalTarget()
                ? osMessagingLocalController.handleLocalMessaging(headers, reqJson)
                : IOUtils.postJsonToEndpoint(osPeerEndpointDef.getEndpointUrl(), reqJson, headers);
        if (resp.isError()) {
            throwOperationErrorException(new Exception(resp.getError()));
        }

        if (osInfo.isDebugging()) {
            logDebug("Response message [\n{0}]", resp.getRespJson());
        }

        return resp.getRespJson();
    }

    private String sendUploadMessage(final String target, final String processor, final OSMessagingFunction function,
            final String service, final String correlationId, final String userLoginId, final String fileSignature,
            final String fileChecksum, ContentDisposition disposition, InputStream in) throws UnifyException {
        if (osInfo.isDebugging()) {
            logDebug("Sending upload message [\n{0}]...", correlationId);
        }

        final OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap.get(target);
        if (!osPeerEndpointDef.isPresent()) {
            return prettyJson(new UnknownTargetResp(target));
        }

        if (!osPeerEndpointDef.isActive()) {
            return prettyJson(InactiveTargetResp.MESSAGE);
        }

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(OSMessagingRequestHeaderConstants.AUTHORIZATION, osPeerEndpointDef.getAuthentication(processor));
        headers.put(OSMessagingRequestHeaderConstants.CORRELATION_ID, correlationId);
        headers.put(OSMessagingRequestHeaderConstants.ROUTING_TYPE, "sync");
        if (!StringUtils.isBlank(userLoginId)) {
            headers.put(OSMessagingRequestHeaderConstants.USER_ID, userLoginId);
        }

        if (!StringUtils.isBlank(fileSignature)) {
            headers.put(OSMessagingRequestHeaderConstants.FILE_SIGNATURE, fileSignature);
        }

        if (!StringUtils.isBlank(fileChecksum)) {
            headers.put(OSMessagingRequestHeaderConstants.FILE_CHECKSUM, fileChecksum);
        }

        if (function != null) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_FUNCTION, function.code());
        }

        if (!StringUtils.isBlank(service)) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_SERVICE, service);
        }

        if (disposition != null) {
            headers.put(OSMessagingRequestHeaderConstants.UPLOAD_DETAIL,
                    HttpUtils.getUnifyContentDisposition(disposition));
        }

        final PostResp<String> resp = osPeerEndpointDef.isLocalTarget()
                ? osUploadLocalController.handleLocalUpload(headers, disposition, in)
                : IOUtils.postStreamToEndpoint(osPeerEndpointDef.getEndpointUploadUrl(), in, headers);
        if (resp.isError()) {
            throwOperationErrorException(new Exception(resp.getError()));
        }

        if (osInfo.isDebugging()) {
            logDebug("Response message [\n{0}]", resp.getRespJson());
        }

        return resp.getRespJson();
    }

    private String sendDownloadMessage(final String target, final String processor, final OSMessagingFunction function,
            final String service, final String correlationId, final String userLoginId, final String fileSignature,
            OutputStream out) throws UnifyException {
        if (osInfo.isDebugging()) {
            logDebug("Sending download message [\n{0}]...", correlationId);
        }

        final OSMessagingPeerEndpointDef osPeerEndpointDef = osPeerEndpointDefFactoryMap.get(target);
        if (!osPeerEndpointDef.isPresent()) {
            return prettyJson(new UnknownTargetResp(target));
        }

        if (!osPeerEndpointDef.isActive()) {
            return prettyJson(InactiveTargetResp.MESSAGE);
        }

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(OSMessagingRequestHeaderConstants.AUTHORIZATION, osPeerEndpointDef.getAuthentication(processor));
        headers.put(OSMessagingRequestHeaderConstants.CORRELATION_ID, correlationId);
        headers.put(OSMessagingRequestHeaderConstants.ROUTING_TYPE, "sync");
        if (!StringUtils.isBlank(userLoginId)) {
            headers.put(OSMessagingRequestHeaderConstants.USER_ID, userLoginId);
        }

        if (!StringUtils.isBlank(fileSignature)) {
            headers.put(OSMessagingRequestHeaderConstants.FILE_SIGNATURE, fileSignature);
        }

        if (function != null) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_FUNCTION, function.code());
        }

        if (!StringUtils.isBlank(service)) {
            headers.put(OSMessagingRequestHeaderConstants.DELEGATE_SERVICE, service);
        }

        final PostResp<String> resp = osPeerEndpointDef.isLocalTarget()
                ? osDownloadLocalController.handleLocalDownload(headers, out)
                : IOUtils.postGetStreamFromEndpoint(osPeerEndpointDef.getEndpointDownloadUrl(), out, headers);
        if (resp.isError()) {
            throwOperationErrorException(new Exception(resp.getError()));
        }

        if (osInfo.isDebugging()) {
            logDebug("Response message [\n{0}]", resp.getRespJson());
        }

        return resp.getRespJson();
    }

}
