/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Query;

/**
 * Workflow entity search widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-workentitysearch")
public class WorkEntitySearchWidget extends EntitySearchWidget {
    
    public static final Restriction WORK_ENTITY_RESTRICTION = new Amongst("baseType",
            ApplicationEntityUtils.BASE_WORK_TYPES);

    @Override
    protected void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException {
        super.addMoreResultRestriction(query);
        query.addRestriction(WORK_ENTITY_RESTRICTION);
    }
}
