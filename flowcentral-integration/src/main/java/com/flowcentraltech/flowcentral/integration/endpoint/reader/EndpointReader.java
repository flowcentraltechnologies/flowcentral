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
package com.flowcentraltech.flowcentral.integration.endpoint.reader;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.integration.data.ReadConfigDef;
import com.flowcentraltech.flowcentral.integration.endpoint.data.ReadEventInst;
import com.tcdng.unify.core.UnifyException;

/**
 * End-point reader.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EndpointReader extends FlowCentralComponent {

    /**
     * Sets up this end-point reader.
     * 
     * @param readConfigDef
     *                      the end-point reader definition
     * @throws UnifyException
     *                        if an error occurs
     */
    void setup(ReadConfigDef readConfigDef) throws UnifyException;

    /**
     * Begins a event watch.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void beginWatch() throws UnifyException;

    /**
     * Returns true if an event component exists in current watch
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean next() throws UnifyException;

    /**
     * Returns current end-point read event.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    ReadEventInst getEvent() throws UnifyException;

    /**
     * Ends current inward end-point watch
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void endWatch() throws UnifyException;

    /**
     * Method to execute on completion of inward file end-point event processing.
     * 
     * @param event
     *               the inward file event
     * @param status
     *               the event status
     * @throws UnifyException
     *                        if an error occurs
     */
    void housekeepWatch(ReadEventInst event, EndpointReadEventStatus status) throws UnifyException;
}
