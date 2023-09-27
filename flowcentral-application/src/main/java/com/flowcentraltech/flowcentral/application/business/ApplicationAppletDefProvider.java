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

package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Application applet definition provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationAppletDefProvider extends FlowCentralComponent {

    /**
     * Checks if this provider provides applet.
     * 
     * @param appletName
     *                   the applet name
     * @return a true if provider provides applet otherwise false
     */
    boolean providesApplet(String appletName);

    /**
     * Gets an applet definition.
     * 
     * @param appletName
     *                   the applet long name
     * @return the applet definition
     * @throws UnifyException
     *                        if applet with name is not found. if an error occurs
     */
    AppletDef getAppletDef(String appletName) throws UnifyException;

    /**
     * Gets a list of applet definitions by role.
     * 
     * @param applicationName
     *                        the application name
     * @param roleCode
     *                        the role code
     * @param appletFilter
     *                        optional applet filter by label
     * @return list of applet definitions
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppletDef> getAppletDefsByRole(String applicationName, String roleCode, String appletFilter)
            throws UnifyException;
}
