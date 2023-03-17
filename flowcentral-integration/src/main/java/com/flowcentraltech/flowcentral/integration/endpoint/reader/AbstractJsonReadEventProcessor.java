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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.integration.data.JsonReadEvent;
import com.flowcentraltech.flowcentral.integration.data.ReadEventInst;
import com.flowcentraltech.flowcentral.integration.data.ReadEventInst.EventMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for read event processor.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractJsonReadEventProcessor<T> extends AbstractReadEventProcessor {

    private final Class<T> jsonClass;

    public AbstractJsonReadEventProcessor(Class<T> jsonClass) {
        this.jsonClass = jsonClass;
    }

    @Override
    public void process(ReadEventInst readEventInst) throws UnifyException {
        List<T> itemList = new ArrayList<>();
        for (EventMessage message : readEventInst.getEventMessages()) {
            T item = DataUtils.fromJsonString(jsonClass,
                    message.isText() ? message.getText() : new String(message.getFile()));
            itemList.add(item);
        }

        process(new JsonReadEvent<T>(readEventInst.getId(), readEventInst.getCreateDt(), itemList));
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

    protected abstract void process(JsonReadEvent<T> jsonReadEvent) throws UnifyException;
}
