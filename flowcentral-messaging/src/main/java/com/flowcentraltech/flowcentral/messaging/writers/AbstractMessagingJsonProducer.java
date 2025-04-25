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
package com.flowcentraltech.flowcentral.messaging.writers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.messaging.data.Message;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Messaging JSON producer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractMessagingJsonProducer<T> extends AbstractMessagingProducer {

    private final String handler;

    protected AbstractMessagingJsonProducer(String handler) {
        this.handler = handler;
    }

    @Override
    public List<Message> produce(String config, String destination) throws UnifyException {
        List<T> msgs = produce();
        if (!DataUtils.isBlank(msgs)) {
            List<Message> list = new ArrayList<Message>();
            for (T msg : msgs) {
                Message.Builder mb = Message.newBuilder(config, destination);
                mb.handler(handler);
                mb.body(asJson(msg));
                list.add(mb.build());
            }

            return list;
        }

        return Collections.emptyList();
    }

    protected abstract List<T> produce() throws UnifyException;
}
