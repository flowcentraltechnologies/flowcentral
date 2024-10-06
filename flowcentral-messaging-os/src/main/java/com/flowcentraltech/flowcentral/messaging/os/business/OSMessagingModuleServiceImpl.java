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
package com.flowcentraltech.flowcentral.messaging.os.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingRequestHeaderConstants;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAsyncResponse;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsync;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingAsyncQuery;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingEndpoint;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingEndpointQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractQueuedExec;
import com.tcdng.unify.core.business.QueuedExec;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Implementation of OS messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(OSMessagingModuleNameConstants.OSMESSAGING_MODULE_SERVICE)
public class OSMessagingModuleServiceImpl extends AbstractFlowCentralService implements OSMessagingModuleService {

    private static final String PROCESS_MESSAGE_ASYNC =  "os::processmessageasync";
    
    private static final int MAX_MESSAGING_THREADS = 32;
    
    private static final int MAX_PROCESSING_BATCH_SIZE = 128;

    private final QueuedExec<Long> queuedExec;

    public OSMessagingModuleServiceImpl() {
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
                                osMessagingAsync.getEndpoint(), osMessagingAsync.getMessage());
                        environment().updateById(OSMessagingAsync.class, osMessagingAsyncId,
                                new Update()
                                .add("processing", Boolean.FALSE)
                                .add("sentOn", getNow())
                                .add("responseCode", resp.getResponseCode())
                                .add("responseMsg", resp.getResponseMessage()));
                    } catch (UnifyException e) {
                        logError(e);
                        try {
                            final Date nextAttemptOn = CalendarUtils.getDateWithFrequencyOffset(getNow(), FrequencyUnit.SECOND,
                                    60);
                            environment().updateById(OSMessagingAsync.class, osMessagingAsyncId,
                                    new Update()
                                    .add("nextAttemptOn", nextAttemptOn)
                                    .add("processing", Boolean.FALSE));
                        } catch (UnifyException e1) {
                            logError(e);
                        }
                    }
                }

            };
    }

    @Override
    public List<OSMessagingEndpoint> findMessagingEndpoints(OSMessagingEndpointQuery query) throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public <T extends BaseOSMessagingResp, U extends BaseOSMessagingReq> T sendSynchronousMessage(Class<T> respClass,
            U request, String endpointName) throws UnifyException {
        return sendMessage(respClass, endpointName, request);
    }

    @Override
    public <T extends BaseOSMessagingReq> void sendAsynchronousMessage(T request, String endpointName)
            throws UnifyException {
        sendAsynchronousMessage(request, endpointName, 0);
    }

    @Override
    public <T extends BaseOSMessagingReq> void sendAsynchronousMessage(T request, String endpointName,
            long delayInSeconds) throws UnifyException {
        final Date nextAttemptOn = CalendarUtils.getDateWithFrequencyOffset(getNow(), FrequencyUnit.SECOND,
                delayInSeconds <= 0 ? 0 : delayInSeconds);
        OSMessagingAsync osMessagingAsync = new OSMessagingAsync();
        osMessagingAsync.setEndpoint(endpointName);
        osMessagingAsync.setMessage(DataUtils.asJsonString(request, PrintFormat.NONE));
        osMessagingAsync.setNextAttemptOn(nextAttemptOn);
        environment().create(osMessagingAsync);
    }

//    @Synchronized(PROCESS_MESSAGE_ASYNC)
//    @Periodic(PeriodicType.FASTER)
//    public void processMessageAsync(TaskMonitor taskMonitor) throws UnifyException {
//        final List<Long> pendingList = environment().valueList(Long.class, "id",
//                new OSMessagingAsyncQuery().isDue(getNow()).isNotProcessing().setLimit(MAX_PROCESSING_BATCH_SIZE));
//        if (!DataUtils.isBlank(pendingList)) {
//            environment().updateAll(new OSMessagingAsyncQuery().idIn(pendingList),
//                    new Update().add("processing", Boolean.TRUE));
//            keepThreadAndClusterSafe("processBefore", new OSMessagingAsyncQuery().isProcessing().idIn(pendingList));
//            for (Long osMessagingAsyncId : pendingList) {
//                queuedExec.execute(osMessagingAsyncId);
//            }
//        }
//    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private <T extends BaseOSMessagingResp> T sendMessage(Class<T> respClass, String endpointName, Object message)
            throws UnifyException {
        OSMessagingEndpoint endpoint = environment().find(new OSMessagingEndpointQuery().name(endpointName));
        if (endpoint == null) {
            throwOperationErrorException(
                    new IllegalArgumentException("Unknown OS application endpoint [" + endpointName + "]."));
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(OSMessagingRequestHeaderConstants.OS_SOURCE_APPLICATION, getApplicationCode());
        headers.put(OSMessagingRequestHeaderConstants.OS_TARGET_APPLICATION, endpoint.getTarget());
        headers.put(OSMessagingRequestHeaderConstants.OS_MESSAGING_PROCESSOR, endpoint.getProcessor());
        final String messagingUrl = endpoint.getNodeUrl() + OSMessagingModuleNameConstants.OSMESSAGING_CONTROLLER;
        return IOUtils.postObjectToEndpointUsingJson(respClass, messagingUrl, message, headers);
    }
}
