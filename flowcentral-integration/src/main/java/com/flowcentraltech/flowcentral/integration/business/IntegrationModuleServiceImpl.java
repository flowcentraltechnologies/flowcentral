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
package com.flowcentraltech.flowcentral.integration.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleErrorConstants;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.data.ReadConfigDef;
import com.flowcentraltech.flowcentral.integration.endpoint.Endpoint;
import com.flowcentraltech.flowcentral.integration.endpoint.data.EventMessage;
import com.flowcentraltech.flowcentral.integration.endpoint.data.ReadEventInst;
import com.flowcentraltech.flowcentral.integration.endpoint.processor.ReadEventProcessor;
import com.flowcentraltech.flowcentral.integration.endpoint.reader.EndpointReadEventStatus;
import com.flowcentraltech.flowcentral.integration.endpoint.reader.EndpointReader;
import com.flowcentraltech.flowcentral.integration.endpoint.reader.EndpointReaderFactory;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfigQuery;
import com.flowcentraltech.flowcentral.integration.entities.ReadConfig;
import com.flowcentraltech.flowcentral.integration.entities.ReadConfigProc;
import com.flowcentraltech.flowcentral.integration.entities.ReadConfigProcQuery;
import com.flowcentraltech.flowcentral.integration.entities.ReadConfigQuery;
import com.flowcentraltech.flowcentral.integration.entities.ReadEvent;
import com.flowcentraltech.flowcentral.integration.entities.ReadEventMessage;
import com.flowcentraltech.flowcentral.integration.entities.ReadEventQuery;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.task.TaskExecType;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Implementation of integration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(IntegrationModuleNameConstants.INTEGRATION_MODULE_SERVICE)
public class IntegrationModuleServiceImpl extends AbstractFlowCentralService implements IntegrationModuleService {

    private static final int MINIMUM_READ_PERIOD_SECONDS = 1;

    private static final int MINIMUM_LOADING_SIZE = 1;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private EndpointReaderFactory endpointReaderFactory;

    private final Map<String, TaskMonitor> endpointReaderMonitors;

    private final FactoryMap<String, EndpointDef> endpointDefFactoryMap;

    private final FactoryMap<String, ReadConfigDef> readConfigDefFactoryMap;

    private final FactoryMap<String, List<ParamConfig>> endpointParamConfigByTypeMap;

    private final FactoryMap<String, List<ParamConfig>> endpointReaderParamConfigByTypeMap;

    public IntegrationModuleServiceImpl() {
        this.endpointReaderMonitors = new HashMap<String, TaskMonitor>();

        this.endpointDefFactoryMap = new FactoryMap<String, EndpointDef>(true)
            {
                @Override
                protected boolean stale(String endpointConfigName, EndpointDef endpointDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new EndpointConfigQuery().id(endpointDef.getId())) > endpointDef.getVersion());
                }

                @Override
                protected EndpointDef create(String endpointConfigName, Object... arg1) throws Exception {
                    EndpointConfig endpointConfig = environment()
                            .list(new EndpointConfigQuery().name(endpointConfigName));
                    if (endpointConfig == null) {
                        throw new UnifyException(IntegrationModuleErrorConstants.CANNOT_FIND_ENDPOINT_CONFIG,
                                endpointConfigName);
                    }

                    ParamValuesDef pvd = CommonInputUtils.getParamValuesDef(
                            endpointParamConfigByTypeMap.get(endpointConfig.getEndpoint()),
                            endpointConfig.getEndpointParams());
                    return new EndpointDef(endpointConfigName, endpointConfig.getEndpoint(), endpointConfig.getId(),
                            endpointConfig.getVersionNo(), pvd);
                }

            };

        this.readConfigDefFactoryMap = new FactoryMap<String, ReadConfigDef>(true)
            {
                @Override
                protected boolean stale(String readConfigName, ReadConfigDef readConfigDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new ReadConfigQuery().name(readConfigName)) > readConfigDef.getVersion());
                }

                @Override
                protected ReadConfigDef create(String readConfigName, Object... arg1) throws Exception {
                    ReadConfig readConfig = environment().list(new ReadConfigQuery().name(readConfigName));
                    if (readConfig == null) {
                        throw new UnifyException(IntegrationModuleErrorConstants.CANNOT_FIND_INWARD_ENDPOINT,
                                readConfigName);
                    }

                    ParamValuesDef pvd = CommonInputUtils.getParamValuesDef(
                            endpointReaderParamConfigByTypeMap.get(readConfig.getEndpointReader()),
                            readConfig.getReaderParams());
                    return new ReadConfigDef(readConfigName, readConfig.getEndpointReader(),
                            readConfig.getEventProcessor(), readConfig.getEndpointReaderPeriod(),
                            readConfig.getEndpointMaxLoadingSize(), readConfig.getId(), readConfig.getVersionNo(), pvd);
                }

            };

        endpointParamConfigByTypeMap = new FactoryMap<String, List<ParamConfig>>()
            {

                @Override
                protected List<ParamConfig> create(String endpointName, Object... arg1) throws Exception {
                    return DataUtils.unmodifiableList(getComponentParamConfigs(Endpoint.class, endpointName));
                }

            };

        endpointReaderParamConfigByTypeMap = new FactoryMap<String, List<ParamConfig>>()
            {

                @Override
                protected List<ParamConfig> create(String endpointReaderName, Object... arg1) throws Exception {
                    return DataUtils
                            .unmodifiableList(getComponentParamConfigs(EndpointReader.class, endpointReaderName));
                }

            };
    }

    public void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public void setEndpointReaderFactory(EndpointReaderFactory endpointReaderFactory) {
        this.endpointReaderFactory = endpointReaderFactory;
    }

    @Override
    public List<EndpointConfig> findEndpointConfigs(EndpointConfigQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<ParamConfig> getEndpointParamConfigs(String endpointComponentName) throws UnifyException {
        return endpointParamConfigByTypeMap.get(endpointComponentName);
    }

    @Override
    public List<ParamConfig> getEndpointReaderParamConfigs(String endpointReaderComponentName) throws UnifyException {
        return endpointReaderParamConfigByTypeMap.get(endpointReaderComponentName);
    }

    @Override
    public EndpointDef getEndpointDef(String endpointConfigName) throws UnifyException {
        return endpointDefFactoryMap.get(endpointConfigName);
    }

    @Override
    public ReadConfigDef getReadConfigDef(String readConfigName) throws UnifyException {
        return readConfigDefFactoryMap.get(readConfigName);
    }

    @Taskable(name = "executeEndpointRead", description = "Execute Endpoint Read",
            parameters = { @Parameter(name = "readConfigName", description = "Read Configuration Name",
                    type = String.class, mandatory = true) })
    public int executeEndpointRead(TaskMonitor taskMonitor, String readConfigName) throws UnifyException {
        ReadConfigDef readConfigDef = getReadConfigDef(readConfigName);
        EndpointReader endpointReader = endpointReaderFactory.getEndpointReader(readConfigDef);
        try {
            final Long readConfigProcId = environment().value(Long.class, "id",
                    new ReadConfigProcQuery().readConfigId(readConfigDef.getReadConfigId()));
            ReadEvent readEvent = new ReadEvent();
            readEvent.setNode(getNodeId());
            readEvent.setReadConfigProcId(readConfigProcId);
            readEvent.setStatus(EndpointReadEventStatus.UNPROCESSED);
            int loading = readConfigDef.getMaxLoadingSize() < MINIMUM_LOADING_SIZE ? MINIMUM_LOADING_SIZE
                    : readConfigDef.getMaxLoadingSize();
            while (loading > 0 && endpointReader.next()) {
                ReadEventInst readEventInst = endpointReader.getEvent();
                if (!readEventInst.isEmpty()) {
                    List<ReadEventMessage> messageList = new ArrayList<ReadEventMessage>();
                    for (EventMessage eventMessage : readEventInst.getEventMessages()) {
                        ReadEventMessage readEventMsg = new ReadEventMessage();
                        readEventMsg.setFileName(eventMessage.getFileName());
                        readEventMsg.setFile(eventMessage.getFile());
                        readEventMsg.setText(eventMessage.getText());
                        messageList.add(readEventMsg);
                    }

                    readEvent.setMessageList(messageList);
                    environment().create(readEvent);
                    commitTransactions();
                    loading--;
                }
            }
        } finally {
            endpointReaderFactory.disposeEndpointReader(endpointReader);
        }
        return 0;
    }

    @Periodic(PeriodicType.FASTER)
    public void executeEndpointReads(TaskMonitor taskMonitor) throws UnifyException {
        final boolean readProcessingEnabled = systemModuleService.getSysParameterValue(boolean.class,
                IntegrationModuleSysParamConstants.INTGERATION_INWARD_ENDPOINT_PROCESSING_ENABLED);
        if (readProcessingEnabled) {
            // Get Names of active inward end-point
            Set<String> actReadConfigNames = environment().valueSet(String.class, "name",
                    new ReadConfigQuery().status(RecordStatus.ACTIVE));

            // Kill inward end-point readers that are inactive
            Iterator<Map.Entry<String, TaskMonitor>> it = endpointReaderMonitors.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, TaskMonitor> entry = it.next();
                if (!actReadConfigNames.contains(entry.getKey())) {
                    entry.getValue().cancel();
                    it.remove();
                }
            }

            // Fire newly active transport readers
            for (String readConfigName : actReadConfigNames) {
                if (!endpointReaderMonitors.containsKey(readConfigName)) {
                    // Fetch reader period
                    ReadConfigDef readConfigDef = getReadConfigDef(readConfigName);
                    final long readerMillSec = CalendarUtils.getMilliSecondsByFrequency(FrequencyUnit.SECOND,
                            readConfigDef.getReaderPeriod() < MINIMUM_READ_PERIOD_SECONDS ? MINIMUM_READ_PERIOD_SECONDS
                                    : readConfigDef.getReaderPeriod());

                    // Setup and launch task
                    TaskSetup taskSetup = TaskSetup.newBuilder(TaskExecType.RUN_PERIODIC).addTask("executeEndpointRead")
                            .setParam("readConfigName", readConfigName).periodInMillSec(readerMillSec)
                            .useStatusLogger(ApplicationComponents.APPLICATION_TASKSTATUSLOGGER).build();
                    TaskMonitor inwTaskMonitor = launchTask(taskSetup);
                    endpointReaderMonitors.put(readConfigName, inwTaskMonitor);
                }
            }
        } else {
            if (!endpointReaderMonitors.isEmpty()) {
                Iterator<Map.Entry<String, TaskMonitor>> it = endpointReaderMonitors.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, TaskMonitor> entry = it.next();
                    entry.getValue().cancel();
                    it.remove();
                }
            }
        }
    }

    @Periodic(PeriodicType.FASTER)
    public void processReadConfigEvents(TaskMonitor taskMonitor) throws UnifyException {
        ReadConfigProc readConfigProc = grabPendingReadConfigProcessing();
        if (readConfigProc != null) {
            Long readConfigProcId = readConfigProc.getId();
            long successCount = readConfigProc.getSuccessCounter();
            long failCount = readConfigProc.getFailureCounter();
            try {
                for (Long readEventId : environment().valueList(Long.class, "id", new ReadEventQuery()
                        .readConfigProcId(readConfigProcId).status(EndpointReadEventStatus.UNPROCESSED))) {
                    ReadEventInst readEventInst = getEndpointReadEvent(
                            environment().list(ReadEvent.class, readEventId));
                    if (processReadEvent(taskMonitor, readEventInst)) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                }

            } finally {
                readConfigProc.setSuccessCounter(successCount);
                readConfigProc.setFailureCounter(failCount);
                releaseReadConfigProcessing(readConfigProc);
            }
        }
    }

    @Periodic(PeriodicType.FAST)
    public void executeEndpointReadHousekeep(TaskMonitor taskMonitor) throws UnifyException {
        Map<String, List<Long>> readEventIds = environment().valueListMap(String.class, "readConfigName", Long.class,
                "id", new ReadEventQuery().node(getNodeId()).statusNot(EndpointReadEventStatus.UNPROCESSED));
        for (Map.Entry<String, List<Long>> entry : readEventIds.entrySet()) {
            ReadConfigDef readConfigDef = getReadConfigDef(entry.getKey());
            EndpointReader endpointReader = endpointReaderFactory.getEndpointReader(readConfigDef);
            try {
                for (Long readEventId : entry.getValue()) {
                    try {
                        ReadEvent readEvent = environment().find(ReadEvent.class, readEventId);
                        endpointReader.housekeepWatch(getEndpointReadEvent(readEvent), readEvent.getStatus());
                        environment().delete(ReadEvent.class, readEventId);
                    } catch (Exception e) {
                        logError(e);
                    } finally {
                        commitTransactions();
                    }
                }

                environment().deleteAll(new ReadEventQuery().idIn(entry.getValue()));
            } finally {
                endpointReaderFactory.disposeEndpointReader(endpointReader);
            }
        }
    }

    @Synchronized("int:pending-inwendpointpointproc")
    public ReadConfigProc grabPendingReadConfigProcessing() throws UnifyException {
        ReadConfigProc readConfigProc = null;
        try {
            List<Long> idList = environment().valueList(Long.class, "readConfigProcId", new ReadEventQuery()
                    .inProcessFlag(Boolean.FALSE).status(EndpointReadEventStatus.UNPROCESSED).setDistinct(true));
            if (!idList.isEmpty()) {
                List<ReadConfigProc> readConfigProcList = environment()
                        .listAll(new ReadConfigProcQuery().idIn(idList).setMin("lastProcessTime"));
                if (readConfigProcList.size() > 0) {
                    readConfigProc = readConfigProcList.get(0);
                    environment().updateAll(new ReadConfigProcQuery().id(readConfigProc.getId()),
                            new Update().add("inProcessFlag", Boolean.TRUE));
                }
            }
        } finally {
            commitTransactions();
        }
        return readConfigProc;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {
        // Kill all transport readers
        Iterator<Map.Entry<String, TaskMonitor>> it = endpointReaderMonitors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TaskMonitor> entry = it.next();
            entry.getValue().cancel();
            it.remove();
        }

        super.onTerminate();
    }

    private boolean processReadEvent(TaskMonitor taskMonitor, ReadEventInst readEventInst) throws UnifyException {
        EndpointReadEventStatus status = EndpointReadEventStatus.FAILED;
        try {
            ReadEventProcessor processor = (ReadEventProcessor) getComponent(readEventInst.getEventProcessor());
            processor.process(readEventInst);
            status = EndpointReadEventStatus.SUCCESSFUL;
        } catch (Exception ex) {
            logError(ex);
        } finally {
            commitTransactions();
            environment().updateAll(new ReadEventQuery().id(readEventInst.getId()), new Update().add("status", status));
            commitTransactions();
        }

        return status.isSuccess();
    }

    private void releaseReadConfigProcessing(ReadConfigProc readConfigProc) throws UnifyException {
        try {
            environment().updateAll(new ReadConfigProcQuery().id(readConfigProc.getId()),
                    new Update().add("lastProcessTime", environment().getNow())
                            .add("successCounter", readConfigProc.getSuccessCounter())
                            .add("failureCounter", readConfigProc.getFailureCounter())
                            .add("inProcessFlag", Boolean.FALSE));
        } finally {
            commitTransactions();
        }
    }

    private ReadEventInst getEndpointReadEvent(ReadEvent readEvent) {
        ReadEventInst readEventInst = new ReadEventInst(readEvent.getEventProcessor(), readEvent.getProcessorRule(),
                readEvent.getId(), readEvent.getCreateDt());
        for (ReadEventMessage readEventMsg : readEvent.getMessageList()) {
            readEventInst.addEventMessage(readEventMsg.getFileName(), readEventMsg.getFile(), readEventMsg.getText());
        }

        return readEventInst;
    }

}
