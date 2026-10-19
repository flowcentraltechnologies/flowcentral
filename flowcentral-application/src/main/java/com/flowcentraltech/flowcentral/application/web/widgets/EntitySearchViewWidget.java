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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.common.annotation.LongName;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractControl;

/**
 * Entity search and view input widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entitysearchview")
@LongName("application.entitysearchview")
@UplAttributes({
    @UplAttribute(name = "ref", type = String.class, mandatory = true)})
public class EntitySearchViewWidget extends AbstractControl {

    @Configurable
    private AppletUtilities au;

    @Action
    public void entitySearch() throws UnifyException {

    }

    @Action
    public void entityView() throws UnifyException {

    }

    @SuppressWarnings("unchecked")
    public EntitySearchViewInfo getItemEntitySearchViewInfo() throws UnifyException {
        final RefDef refDef = application().getRefDef(getUplAttribute(String.class, "ref"));
        final Object keyVal = getValue(Object.class);
        String label = null;
        if (keyVal != null) {
            final EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
            Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
            ;
            query.addEquals("id", keyVal);

            Listable result = environment().listLean(query);
            if (result != null && refDef != null) {
                label = refDef.isWithListFormat()
                        ? specialParamProvider().getStringGenerator(new BeanValueStore(result).getReader(),
                                getValueStore().getReader(), refDef.getListFormat()).generate()
                        : application().getEntityDescription(entityClassDef, (Entity) result, refDef.getSearchField());
            }
        }

        return new EntitySearchViewInfo(keyVal, label, refDef.getSearchTable(), refDef.getViewForm());
    }
    
    public String getSearchButtonId() throws UnifyException {
        return getPrefixedId("sch_");
    }

    public String getViewButtonId() throws UnifyException {
        return getPrefixedId("viw_");
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected EnvironmentService environment() {
        return au.environment();
    }

    protected SpecialParamProvider specialParamProvider() {
        return au.specialParamProvider();
    }
	
}
