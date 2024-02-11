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
package com.flowcentraltech.flowcentral.configuration.business;

import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.FlowCentralInstall;
import com.flowcentraltech.flowcentral.configuration.data.NotifLargeTextInstall;
import com.flowcentraltech.flowcentral.configuration.data.NotifTemplateInstall;
import com.flowcentraltech.flowcentral.configuration.data.ReportInstall;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowInstall;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowWizardInstall;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Configuration loader component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ConfigurationLoader extends UnifyComponent {

    /**
     * Loads master module installation configuration.
     * 
     * @return the master installation configuration
     * @throws UnifyException
     *                        if an error occurs
     */
    FlowCentralInstall loadMasterModuleInstallation() throws UnifyException;

    /**
     * Loads application installation configuration.
     * 
     * @param configFile
     *                   the configuration file
     * @return the application install object
     * @throws UnifyException
     *                        if an error occurs
     */
    ApplicationInstall loadApplicationInstallation(String configFile) throws UnifyException;

    /**
     * Loads report installation configuration.
     * 
     * @param configFile
     *                   the configuration file
     * @return the report installation
     * @throws UnifyException
     *                        if an error occurs
     */
    ReportInstall loadReportInstallation(String configFile) throws UnifyException;

    /**
     * Loads notification template installation configuration.
     * 
     * @param configFile
     *                   the configuration file
     * @return the notification template installation
     * @throws UnifyException
     *                        if an error occurs
     */
    NotifTemplateInstall loadNotifTemplateInstallation(String configFile) throws UnifyException;

    /**
     * Loads notification large text installation configuration.
     * 
     * @param configFile
     *                   the configuration file
     * @return the notification large text installation
     * @throws UnifyException
     *                        if an error occurs
     */
    NotifLargeTextInstall loadNotifLargeTextInstallation(String configFile) throws UnifyException;

    /**
     * Loads workflow installation configuration.
     * 
     * @param configFile
     *                   the configuration file
     * @return the workflow installation
     * @throws UnifyException
     *                        if an error occurs
     */
    WorkflowInstall loadWorkflowInstallation(String configFile) throws UnifyException;

    /**
     * Loads workflow wizard installation configuration.
     * 
     * @param configFile
     *                   the configuration file
     * @return the workflow wizard installation
     * @throws UnifyException
     *                        if an error occurs
     */
    WorkflowWizardInstall loadWorkflowWizardInstallation(String configFile) throws UnifyException;

}
