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
package com.flowcentraltech.flowcentral.integration.endpoint.writer;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.integration.data.WriteConfigDef;
import com.flowcentraltech.flowcentral.integration.endpoint.data.WriteEventInst;
import com.tcdng.unify.core.UnifyException;

/**
 * End-point writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EndpointWriter extends FlowCentralComponent {

    /**
     * Sets up this end-point writer.
     * 
     * @param writeConfigDef
     *                      the end-point writer definition
     * @throws UnifyException
     *                        if an error occurs
     */
    void setup(WriteConfigDef writeConfigDef) throws UnifyException;

    /**
     * Begins a event watch.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void beginWatch() throws UnifyException;

    /**
     * Sets current end-point write event.
     * 
     * @return true on success otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean setEvent(WriteEventInst writeEventInst) throws UnifyException;

    /**
     * Ends current inward end-point watch
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void endWatch() throws UnifyException;

}
