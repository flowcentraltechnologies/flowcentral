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
package com.flowcentraltech.flowcentral.messaging.os.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpoint;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpointQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * OS messaging peer end-point list.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("osmessagingpeerendpointlist")
public class OSMessagingPeerEndpointListCommand extends AbstractOSMessagingListCommand<ZeroParams> {

    public OSMessagingPeerEndpointListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams param) throws UnifyException {
        List<OSMessagingPeerEndpoint> endpointList = osmessaging()
                .findOSMessagingEndpoints((OSMessagingPeerEndpointQuery) new OSMessagingPeerEndpointQuery()
                        .addSelect("name", "description").ignoreEmptyCriteria(true).addOrder("description"));
        if (!DataUtils.isBlank(endpointList)) {
            List<ListData> resultList = new ArrayList<ListData>();
            for (OSMessagingPeerEndpoint osMessagingEndpoint : endpointList) {
                resultList.add(new ListData(osMessagingEndpoint.getName(), osMessagingEndpoint.getDescription()));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

}
