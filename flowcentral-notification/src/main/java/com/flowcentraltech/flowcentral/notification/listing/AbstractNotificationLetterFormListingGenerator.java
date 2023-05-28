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

import com.flowcentraltech.flowcentral.application.listing.AbstractLetterFormListingGenerator;
import com.flowcentraltech.flowcentral.application.web.data.LetterFormListing;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.MapValueStore;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;

/**
 * Convenient abstract base class for notification letter form listing
 * generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotificationLetterFormListingGenerator extends AbstractLetterFormListingGenerator {

    @Configurable
    private NotificationModuleService notificationModuleService;

    public final void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    @Override
    protected final String getLetterBody(LetterFormListing letterFormListing) throws UnifyException {
        NotifLargeTextDef notifLargeTextDef = notificationModuleService
                .getNotifLargeTextDef(letterFormListing.getLetterName());
        ParameterizedStringGenerator generator = getParameterizedStringGenerator(
                new MapValueStore(letterFormListing.getProperties()).getReader(), notifLargeTextDef.getBodyTokenList());
        return generator.generate();
    }

}
