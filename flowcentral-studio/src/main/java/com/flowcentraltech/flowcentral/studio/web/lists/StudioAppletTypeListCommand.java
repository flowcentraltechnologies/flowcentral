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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.list.ListManager;

/**
 * Studio applet type list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioapplettypelist")
public class StudioAppletTypeListCommand extends AbstractApplicationListCommand<ConfigTypeParams> {

    private static final List<AppletType> CREATABLE_APPLETTYPE_LIST = Collections
            .unmodifiableList(Arrays.asList(AppletType.MANAGE_ENTITYLIST, AppletType.MANAGE_ENTITYLIST_ASSIGN,
                    AppletType.MANAGE_ENTITYLIST_SINGLEFORM, AppletType.MANAGE_LOADINGLIST, AppletType.HEADLESS_TABS,
                    AppletType.CREATE_ENTITY, AppletType.CREATE_ENTITY_SINGLEFORM, AppletType.FORM_WIZARD,
                    AppletType.LISTING, AppletType.TASK_EXECUTION, AppletType.DATA_IMPORT, AppletType.FACADE,
                    AppletType.FACADE_MULTIPLE, AppletType.PATH_WINDOW, AppletType.PATH_PAGE));

    @Configurable
    private ListManager listManager;

    private LocaleFactoryMap<List<Listable>> listFactory;

    public StudioAppletTypeListCommand() {
        super(ConfigTypeParams.class);
        listFactory = new LocaleFactoryMap<List<Listable>>()
            {

                @Override
                protected List<Listable> create(Locale locale, Object... arg1) throws Exception {
                    List<Listable> list = new ArrayList<Listable>();
                    Map<String, Listable> map = listManager.getListMap(locale, "applettypelist");
                    for (AppletType type : CREATABLE_APPLETTYPE_LIST) {
                        list.add(map.get(type.code()));
                    }

                    return Collections.unmodifiableList(list);
                }

            };
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ConfigTypeParams params) throws UnifyException {
        if (params.isPresent()) {
            if (params.getType().isStatic()) {
                return listManager.getList(locale, "applettypelist");
            }
        }

        return listFactory.get(locale);
    }

}
