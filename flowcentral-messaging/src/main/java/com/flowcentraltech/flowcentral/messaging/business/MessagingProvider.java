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
package com.flowcentraltech.flowcentral.messaging.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.SearchInput;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Listable;

/**
 * Messaging provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface MessagingProvider extends FlowCentralComponent {

    /**
     * Gets messaging configuration by name.
     * 
     * @param configName
     *                   the configuration name to use
     * @return the configuration if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Listable getConfig(String configName) throws UnifyException;

    /**
     * Get messaging configuration list based on supplied search input.
     * 
     * @param searchInput
     *                    the search input
     * @return the configuration list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getConfigList(SearchInput searchInput) throws UnifyException;

    /**
     * Sends a message to an configuration destination.
     * 
     * @param configName
     *                    the configuration
     * @param destination
     *                    the destination
     * @param text
     *                    the message
     * @throws UnifyException
     *                        if an error occurs
     */
    void sendMessage(String configName, String destination, String text) throws UnifyException;

    /**
     * Receives a message from an configuration source.
     * 
     * @param configName
     *                   the configuration
     * @param source
     *                   the source
     * @return the message if available otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    String receiveMessage(String configName, String source) throws UnifyException;
}
