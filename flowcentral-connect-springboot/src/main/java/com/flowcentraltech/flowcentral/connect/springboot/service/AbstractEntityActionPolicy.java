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
package com.flowcentraltech.flowcentral.connect.springboot.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

import com.flowcentraltech.flowcentral.connect.configuration.constants.EvaluationMode;

/**
 * Convenient abstract base class for entity action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityActionPolicy<T> implements EntityActionPolicy<T> {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    private final static String[] NO_MESSAGES = new String[0];
    
    @Value("${flowcentral.interconnect.logging.enabled:false}")
    private boolean logging;
    
    @Override
    public String[] validate(EvaluationMode evaluationMode, T entityBean) {
        return NO_MESSAGES;
    }

    protected void logInfo(String message, Object... params) {
        if (logging) {
            LOGGER.log(Level.INFO, message, params);
        }
    }

    protected void logWarn(String message, Object... params) {
        if (logging) {
            LOGGER.log(Level.WARNING, message, params);
        }
    }

    protected void logSevere(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
    }

}
