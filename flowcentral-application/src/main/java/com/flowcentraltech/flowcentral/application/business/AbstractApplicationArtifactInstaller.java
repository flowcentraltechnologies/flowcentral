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

package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntityQuery;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.configuration.business.ConfigurationLoader;
import com.flowcentraltech.flowcentral.configuration.data.Messages;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Convenient abstract base class for application artifact installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplicationArtifactInstaller extends AbstractFlowCentralComponent
        implements ApplicationArtifactInstaller {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private ConfigurationLoader configurationLoader;

    @Configurable
    private EnvironmentService environmentService;

    @Override
    public int deleteApplicationArtifacts(TaskMonitor taskMonitor, Long applicationId) throws UnifyException {
        int deletion = 0;
        for (DeletionParams params : getDeletionParams()) {
            deletion += deleteApplicationArtifacts(taskMonitor, params, applicationId, false);
        }
        
        return deletion;
    }

    @Override
    public int deleteCustomApplicationArtifacts(TaskMonitor taskMonitor, Long applicationId) throws UnifyException {
        int deletion = 0;
        for (DeletionParams params : getDeletionParams()) {
            deletion += deleteApplicationArtifacts(taskMonitor, params, applicationId, true);
        }
        
        return deletion;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    @Override
    protected String resolveApplicationMessage(String message, Object... params) throws UnifyException {
        Messages messages = getSessionAttribute(Messages.class,
                FlowCentralSessionAttributeConstants.ALTERNATIVE_RESOURCES_BUNDLE);
        String msg = messages != null ? messages.resolveMessage(message, params) : null;
        return msg == null ? super.resolveApplicationMessage(message, params) : msg;
    }

    protected void registerPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode,
            String privilegeDesc) throws UnifyException {
        applicationPrivilegeManager.registerPrivilege(applicationId, privilegeCategoryCode, privilegeCode,
                privilegeDesc);
    }

    protected ConfigurationLoader getConfigurationLoader() {
        return configurationLoader;
    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected class DeletionParams {
        private final String name;

        private final BaseApplicationEntityQuery<?> query;

        public DeletionParams(String name, BaseApplicationEntityQuery<?> query) {
            this.name = name;
            this.query = query;
        }

        public final String getName() {
            return name;
        }

        public final BaseApplicationEntityQuery<?> getQuery() {
            return query;
        }
    }

    protected abstract List<DeletionParams> getDeletionParams() throws UnifyException;

    private int deleteApplicationArtifacts(TaskMonitor taskMonitor, DeletionParams deletionParams, Long applicationId,
            boolean custom) throws UnifyException {
        int deletion = 0;
        if (custom) {
            logDebug(taskMonitor, "Deleting custom application {0}...", deletionParams.getName());
            deletion = environment().deleteAll(deletionParams.getQuery().applicationId(applicationId).isCustom());
            logDebug(taskMonitor, "[{1}] custom application {0} deleted.", deletionParams.getName(), deletion);
        } else {
            logDebug(taskMonitor, "Deleting application {0}...", deletionParams.getName());
            deletion = environment().deleteAll(deletionParams.getQuery().applicationId(applicationId));
            logDebug(taskMonitor, "[{1}] application {0} deleted.", deletionParams.getName(), deletion);
        }

        return deletion;
    }

}
