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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.web.widgets.EntitySearchWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.list.ListManager;

/**
 * Entity reference search widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entityrefsearch")
@UplAttributes({
        @UplAttribute(name = "styleClass", type = String.class, defaultVal = "$e{fc-entitysearch}"),
        @UplAttribute(name = "ref", type = String[].class, defaultVal = "application.appRefRef") })
public class EntityRefSearchWidget extends EntitySearchWidget {

    @Configurable
    private ListManager listManager;

    public void setListManager(ListManager listManager) {
        this.listManager = listManager;
    }

    public String getListkey() throws UnifyException {
        return "longName";
    }

    @Override
    protected List<? extends Listable> doSearch(String input, int limit) throws UnifyException {
        return getResultByRef(input, limit);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException {
        super.addMoreResultRestriction(query);

        Set<String> childEntityList = (Set<String>) getWriteWork().get(Set.class, "ref.childentitylist");
        if (childEntityList != null) {
            query.addAmongst("entity", childEntityList);
        }
    }
}
