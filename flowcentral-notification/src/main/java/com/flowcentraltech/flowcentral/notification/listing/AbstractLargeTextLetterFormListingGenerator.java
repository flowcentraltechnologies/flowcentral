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
import com.flowcentraltech.flowcentral.common.data.FontSetting;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextDef;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.MapValueStore;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for large text letter form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractLargeTextLetterFormListingGenerator extends AbstractLetterFormListingGenerator {

    @Configurable
    private NotificationModuleService notificationModuleService;

    private static final int DEFAULT_FONTSIZE_IN_PIXELS = 12;

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> wrapperType)
            throws UnifyException {
        return notificationModuleService.wrapperOfNotifLargeText(wrapperType);
    }

    protected final <T extends NotifLargeTextWrapper> T getLargeTextWrapper(Class<T> wrapperType,
            Map<String, Object> parameters) throws UnifyException {
        return notificationModuleService.wrapperOfNotifLargeText(wrapperType, parameters);
    }

    @Override
    protected FontSetting getFontSetting(ValueStoreReader reader) throws UnifyException {
        NotifLargeTextDef notifLargeTextDef = notificationModuleService.getNotifLargeTextDef(getLetterName(reader));
        final int fontSizeInPixels = notifLargeTextDef.getFontSizeInPixels() <= 0 ? DEFAULT_FONTSIZE_IN_PIXELS
                : notifLargeTextDef.getFontSizeInPixels();
        return notifLargeTextDef.getFontFamily() != null
                ? new FontSetting(notifLargeTextDef.getFontFamily(), fontSizeInPixels)
                : new FontSetting(fontSizeInPixels);
    }

    @Override
    protected final int getNumberOfParts(ValueStoreReader reader) throws UnifyException {
        NotifLargeTextDef notifLargeTextDef = notificationModuleService.getNotifLargeTextDef(getLetterName(reader));
        return notifLargeTextDef.getNumberOfPages();
    }

    @Override
    protected final String getLetterBody(ValueStoreReader reader, LetterFormListing letterFormListing)
            throws UnifyException {
        NotifLargeTextDef notifLargeTextDef = notificationModuleService.getNotifLargeTextDef(getLetterName(reader));
        Map<String, Object> parameters = getParameters(reader);
        ParameterizedStringGenerator generator = getParameterizedStringGenerator(
                DataUtils.isBlank(parameters) ? reader : new MapValueStore(parameters).getReader(),
                notifLargeTextDef.getBodyTokenList(letterFormListing.getListingIndex()));
        return generator.generate();
    }

    protected abstract String getLetterName(ValueStoreReader reader) throws UnifyException;

    protected abstract Map<String, Object> getParameters(ValueStoreReader reader) throws UnifyException;

}
