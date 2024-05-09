/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EnumerationDef;
import com.flowcentraltech.flowcentral.application.data.EnumerationItemDef;
import com.flowcentraltech.flowcentral.application.entities.AppEnumeration;
import com.flowcentraltech.flowcentral.application.entities.AppEnumerationItem;
import com.flowcentraltech.flowcentral.application.entities.AppEnumerationQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.list.AbstractDynamicListManager;

/**
 * Default implementation of dynamic list manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.APPLICATION_DYNAMICLIST_MANAGER)
public class DynamicListManagerImpl extends AbstractDynamicListManager {

    @Configurable
    private EnvironmentService environmentService;
    
    private FactoryMap<String, EnumerationDef> enumDefFactoryMap;
    
    public DynamicListManagerImpl() {

        this.enumDefFactoryMap = new FactoryMap<String, EnumerationDef>(true)
            {
                @Override
                protected boolean stale(String longName, EnumerationDef enumerationDef) throws Exception {
                    return environmentService.value(long.class, "versionNo",
                            new AppEnumerationQuery().id(enumerationDef.getId())) > enumerationDef.getVersion();
                }

                @Override
                protected EnumerationDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    AppEnumeration appEnumeration = environmentService
                            .list(Query.of(AppEnumeration.class).addEquals("name", nameParts.getEntityName())
                                    .addEquals("applicationName", nameParts.getApplicationName()));
                    
                    List<EnumerationItemDef> itemsList = new ArrayList<EnumerationItemDef>();
                    for (AppEnumerationItem item : appEnumeration.getItemList()) {
                        itemsList.add(new EnumerationItemDef(item.getCode(), item.getLabel()));
                    }

                    return new EnumerationDef(nameParts, appEnumeration.getDescription(), appEnumeration.getId(),
                            appEnumeration.getVersionNo(), appEnumeration.getLabel(), itemsList);
                }
            };
    }
    
    @Override
    public List<? extends Listable> getList(Locale locale, String listName, Object... params) throws UnifyException {
      return enumDefFactoryMap.get(listName).getItemsList();
    }

}
