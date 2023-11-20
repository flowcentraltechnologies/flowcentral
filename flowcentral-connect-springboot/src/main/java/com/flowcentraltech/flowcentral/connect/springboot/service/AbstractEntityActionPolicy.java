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

import com.flowcentraltech.flowcentral.connect.configuration.constants.EvaluationMode;

/**
 * Convenient abstract base class for entity action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityActionPolicy<T> implements EntityActionPolicy<T> {

    private final static String[] NO_MESSAGES = new String[0];
    
    @Override
    public String[] validate(EvaluationMode evaluationMode, T entityBean) {
        return NO_MESSAGES;
    }

}
