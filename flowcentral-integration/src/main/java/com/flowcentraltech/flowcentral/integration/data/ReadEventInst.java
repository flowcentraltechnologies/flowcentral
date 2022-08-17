/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Read event instance.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReadEventInst {

    private String eventProcessor;

    private String processorRule;

    private Long id;

    private Date createDt;

    private List<EventMessage> eventMessages;

    public ReadEventInst(String eventProcessor, String processorRule, Long id, Date createDt) {
        this.eventProcessor = eventProcessor;
        this.processorRule = processorRule;
        this.id = id;
        this.createDt = createDt;
        this.eventMessages = new ArrayList<EventMessage>();
    }

    public ReadEventInst() {
        this.eventMessages = new ArrayList<EventMessage>();
    }

    public ReadEventInst addEventMessage(String fileName, byte[] data) {
        eventMessages.add(new EventMessage(fileName, data));
        return this;
    }

    public String getEventProcessor() {
        return eventProcessor;
    }

    public String getProcessorRule() {
        return processorRule;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public List<EventMessage> getEventMessages() {
        return eventMessages;
    }

    public int fileCount() {
        return eventMessages.size();
    }

    public boolean isEmpty() {
        return eventMessages.isEmpty();
    }

    public static class EventMessage {

        private String fileName;

        private byte[] message;

        public EventMessage(String fileName, byte[] message) {
            this.fileName = fileName;
            this.message = message;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getMessage() {
            return message;
        }
    }
}
