/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.business;

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.SnapshotDetails;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.studio.business.data.SnapshotResultDetails;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Studio module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface StudioModuleService extends FlowCentralService {
    
    /**
     * Takes studio snapshot.
     * 
     * @param taskMonitor
     *                      the task monitor
     * @param snapshotType
     *                      the snapshot type
     * @param snapshotName
     *                      the snapshot name
     * @param message
     *                      the message
     * @param resultDetails
     *                      the result details
     * @return success indicator
     * @throws UnifyException
     *                        if an error occurs
     */
    int takeStudioSnapshotTask(TaskMonitor taskMonitor, StudioSnapshotType snapshotType, String snapshotName,
            String message, SnapshotResultDetails resultDetails) throws UnifyException;

    /**
     * Find snapshot details.
     * 
     * @param fromDate
     *                 the snapshot from date
     * @param toDate
     *                 the snapshot to date
     * @return the snapshot details
     * @throws UnifyException
     *                        if an error occurs
     */
    List<SnapshotDetails> findSnapshotDetails(Date fromDate, Date toDate) throws UnifyException;

    /**
     * Gets a snapshot.
     * 
     * @param snapshotDetailsId
     *                          the snapshot details ID
     * @return the snapshot
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] getSnapshot(Long snapshotDetailsId) throws UnifyException;
    
    /**
     * Gets a studio applet definition.
     * 
     * @param appletName
     *                   the applet long name
     * @return the applet definition
     * @throws UnifyException
     *                        if applet name is invalid. If applet with name is
     *                        unknown. If an error occurs
     */
    AppletDef getAppletDef(String appletName) throws UnifyException;

    /**
     * Gets applet definitions for application entities.
     * 
     * @param applicationName
     *                        the application name
     * @param type
     *                        the component type
     * @param filter
     * @return list of applet definitions
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppletDef> findAppletDefs(String applicationName, StudioAppComponentType type, String filter)
            throws UnifyException;
}
