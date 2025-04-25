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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Or;

/**
 * Application applet filter query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppAppletFilterQuery extends BaseConfigNamedEntityQuery<AppAppletFilter> {

    public AppAppletFilterQuery() {
        super(AppAppletFilter.class);
    }

    public AppAppletFilterQuery appAppletId(Long appAppletId) {
        return (AppAppletFilterQuery) addEquals("appAppletId", appAppletId);
    }

    public AppAppletFilterQuery byOwner(String ownerId) {
        return (AppAppletFilterQuery) addRestriction(new Or().add(new Equals("ownershipType", OwnershipType.GLOBAL))
                .add(new And().add(new Equals("ownershipType", OwnershipType.USER)).add(new Equals("owner", ownerId))));
    }

    public AppAppletFilterQuery quickFilter(boolean quickFilter) {
        return (AppAppletFilterQuery) addEquals("quickFilter", quickFilter);
    }

}
