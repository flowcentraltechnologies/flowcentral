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
package com.flowcentraltech.flowcentral.notification.listing;

import java.util.Map;

import com.flowcentraltech.flowcentral.application.listing.AbstractLetterFormListingGenerator;
import com.flowcentraltech.flowcentral.application.web.data.LetterFormListing;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextDef;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.MapValueStore;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for large text letter form listing
 * generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractLargeTextLetterFormListingGenerator extends AbstractLetterFormListingGenerator {

    @Configurable
    private NotificationModuleService notificationModuleService;

    public final void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> wrapperType)
            throws UnifyException {
        return notificationModuleService.wrapperOfNotifLargeText(wrapperType);
    }

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> wrapperType,
            Map<String, Object> parameters) throws UnifyException {
        return notificationModuleService.wrapperOfNotifLargeText(wrapperType, parameters);
    }

    @Override
    protected final String getLetterBody(ValueStoreReader reader, LetterFormListing letterFormListing)
            throws UnifyException {
        NotifLargeTextDef notifLargeTextDef = notificationModuleService
                .getNotifLargeTextDef(letterFormListing.getLetterName(letterFormListing.getCurrentListingIndex()));
        ParameterizedStringGenerator generator = getParameterizedStringGenerator(
                letterFormListing.isEmptyParameters() ? reader
                        : new MapValueStore(letterFormListing.getParameters()).getReader(),
                notifLargeTextDef.getBodyTokenList());
        return generator.generate();
    }

}
