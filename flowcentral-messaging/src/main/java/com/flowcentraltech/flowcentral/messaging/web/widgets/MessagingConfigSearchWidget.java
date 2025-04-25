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

package com.flowcentraltech.flowcentral.messaging.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.EntitySearchWidget;
import com.flowcentraltech.flowcentral.common.data.SearchInput;
import com.flowcentraltech.flowcentral.messaging.business.MessagingProvider;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;

/**
 * Messaging configuration search.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-messagingconfigsearch")
@UplAttributes({
        @UplAttribute(name = "ref", type = String[].class, defaultVal = "$l{}"),
        @UplAttribute(name = "direct", type = boolean.class, defaultVal = "true"),
        @UplAttribute(name = "listKey", type = String.class),
        @UplAttribute(name = "entityName", type = String.class),
        @UplAttribute(name = "styleClass", type = String.class, defaultVal = "$e{fc-entitysearch}") })
public class MessagingConfigSearchWidget extends EntitySearchWidget {

    @Configurable
    private MessagingProvider messagingProvider;

    @Override
    protected Listable doCurrentSelect(Object keyVal) throws UnifyException {
        if (keyVal != null && messagingProvider != null) {
            return messagingProvider.getConfig((String) keyVal);
        }

        return null;
    }

    @Override
    protected List<? extends Listable> doSearch(String input, int limit) throws UnifyException {
        if (messagingProvider != null) {
            return messagingProvider.getConfigList(new SearchInput(input, limit));
        }

        return Collections.emptyList();
    }

}
