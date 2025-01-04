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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.list.ListManager;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Studio tab content type list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiotabcontenttypelist")
public class StudioTabContentTypeListCommand extends AbstractApplicationListCommand<ZeroParams> {

    @Configurable
    private ListManager listManager;

    private LocaleFactoryMap<List<Listable>> listFactory;

    public StudioTabContentTypeListCommand() {
        super(ZeroParams.class);
        listFactory = new LocaleFactoryMap<List<Listable>>()
            {

                @Override
                protected List<Listable> create(Locale locale, Object... arg1) throws Exception {
                    List<Listable> list = new ArrayList<Listable>();
                    Map<String, Listable> map = listManager.getListMap(locale, "tabcontenttypelist");
                    for (TabContentType type : TabContentType.values()) {
                        Listable listable = map.get(type.code());
                        list.add(new ListData(type.name(), listable.getListDescription()));
                    }

                    return Collections.unmodifiableList(list);
                }

            };
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams zeroParams) throws UnifyException {
        return listFactory.get(locale);
    }

}
