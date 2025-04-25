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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.LocaleFactoryMap;

/**
 * Entity set value option list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("entitysetvalueoptionlist")
public class EntitySetValueOptionListCommand extends AbstractFlowCentralListCommand<EntityDefFieldListParams> {

    private final LocaleFactoryMap<OptionsInfo> map = new LocaleFactoryMap<OptionsInfo>()
        {
            @Override
            protected OptionsInfo create(Locale locale, Object... arg1) throws Exception {
                List<ListData> list = new ArrayList<ListData>();
                List<ListData> lingualList = new ArrayList<ListData>();
                List<ListData> refList = new ArrayList<ListData>();
                for (SetValueType type : SetValueType.values()) {
                    ListData listData = new ListData(type.code(), resolveSessionMessage(type.label()));
                    lingualList.add(listData);
                    if (!type.isLingual()) {
                        list.add(listData);
                    }

                    if (type.isSupportsRef()) {
                        refList.add(listData);
                    }
                }

                return new OptionsInfo(Collections.unmodifiableList(list), Collections.unmodifiableList(lingualList),
                        Collections.unmodifiableList(refList));
            }

        };

    public EntitySetValueOptionListCommand() {
        super(EntityDefFieldListParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityDefFieldListParams params) throws UnifyException {
        if (params.isPresent()) {
            OptionsInfo optionsInfo = map.get(locale);
            EntityFieldDataType type = params.getEntityDef().getFieldDef(params.getFieldName()).getDataType();
            if (type.isSupportLingual()) {
                return optionsInfo.getLingualList();
            }

            if (type.isEntityRef()) {
                return optionsInfo.getRefList();
            }

            return optionsInfo.getList();
        }

        return Collections.emptyList();
    }

    private class OptionsInfo {

        private List<ListData> list;

        private List<ListData> lingualList;

        private List<ListData> refList;

        public OptionsInfo(List<ListData> list, List<ListData> lingualList, List<ListData> refList) {
            this.list = list;
            this.lingualList = lingualList;
            this.refList = refList;
        }

        public List<ListData> getList() {
            return list;
        }

        public List<ListData> getLingualList() {
            return lingualList;
        }

        public List<ListData> getRefList() {
            return refList;
        }
    }
}
