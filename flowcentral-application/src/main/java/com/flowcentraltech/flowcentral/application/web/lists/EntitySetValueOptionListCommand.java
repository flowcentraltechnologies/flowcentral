/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.list.AbstractListCommand;

/**
 * Entity set value option list command.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("entitysetvalueoptionlist")
public class EntitySetValueOptionListCommand extends AbstractListCommand<EntityDefFieldListParams> {

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
