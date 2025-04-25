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
package com.flowcentraltech.flowcentral.integration.endpoint.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Base class for event types.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseEventInst {

    private Long id;

    private Date createDt;

    private List<EventMessage> eventMessages;

    protected BaseEventInst(Long id, Date createDt) {
         this.id = id;
        this.createDt = createDt;
        this.eventMessages = new ArrayList<EventMessage>();
    }

    public BaseEventInst() {
        this.eventMessages = new ArrayList<EventMessage>();
    }

    public void addEventMessage(String fileName, byte[] file, String text) {
        eventMessages.add(new EventMessage(text, fileName, file));
    }

    public void addEventMessage(String fileName, byte[] data) {
        eventMessages.add(new EventMessage(fileName, data));
    }

    public void addEventMessage(byte[] data) {
        eventMessages.add(new EventMessage(data));
    }

    public void addEventMessage(String text) {
        eventMessages.add(new EventMessage(text));
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
}
