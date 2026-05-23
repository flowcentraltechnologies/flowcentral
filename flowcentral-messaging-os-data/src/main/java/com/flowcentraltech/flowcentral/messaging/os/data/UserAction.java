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

package com.flowcentraltech.flowcentral.messaging.os.data;

import java.util.Date;

/**
 * User action interface.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface UserAction {

    /**
     * Gets the action role.
     * 
     * @return the role
     */
    String getRole();

    /**
     * Gets action name.
     * 
     * @return the action name
     */
    String getActionName();

    /**
     * Gets action by.
     * 
     * @return the action made by
     */
    String getActionBy();

    /**
     * Gets the action timestamp
     * 
     * @return the timestamp
     */
    Date getActionDate();
}
