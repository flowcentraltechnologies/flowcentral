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
package com.flowcentraltech.flowcentral.integration.endpoint.data;

import java.util.Date;

/**
 * Read event instance.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReadEventInst extends BaseEventInst {

    private String eventProcessor;

    private String processorRule;

    public ReadEventInst(String eventProcessor, String processorRule, Long id, Date createDt) {
        super(id, createDt);
        this.eventProcessor = eventProcessor;
        this.processorRule = processorRule;
    }

    public ReadEventInst() {

    }

    public String getEventProcessor() {
        return eventProcessor;
    }

    public String getProcessorRule() {
        return processorRule;
    }

}
