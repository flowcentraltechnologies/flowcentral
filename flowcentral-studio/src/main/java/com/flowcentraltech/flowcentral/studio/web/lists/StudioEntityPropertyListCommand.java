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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Studio entity property list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("entitypropertylist")
public class StudioEntityPropertyListCommand extends AbstractApplicationListCommand<PropertyListParams> {

    private final List<Listable> enumFields = Collections
            .unmodifiableList(Arrays.asList(new EnumListData("name"), new EnumListData("description")));

    public StudioEntityPropertyListCommand() {
        super(PropertyListParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, PropertyListParams params) throws UnifyException {
        if (params.isPresent()) {
            AppEntityField keyField = application().findAppEntityField(
                    new AppEntityFieldQuery().appEntityId(params.getAppEntityId()).name(params.getKey()));
            if (keyField != null) {
                if (EntityFieldDataType.ENUM_REF.equals(keyField.getDataType())) {
                    return enumFields;
                } else if (EntityFieldDataType.REF.equals(keyField.getDataType())
                        || EntityFieldDataType.REF_UNLINKABLE.equals(keyField.getDataType())) {
                    RefDef refDef = application().getRefDef(keyField.getReferences());
                    ApplicationEntityNameParts np = ApplicationNameUtils
                            .getApplicationEntityNameParts(refDef.getEntity());
                    return application()
                            .findAppEntityFields((AppEntityFieldQuery) new AppEntityFieldQuery()
                                    .applicationName(np.getApplicationName()).appEntityName(np.getEntityName())
                                    .dataTypeNotIn(EntityFieldDataType.CHILD, EntityFieldDataType.CHILD_LIST,
                                            /*EntityFieldDataType.REF_FILEUPLOAD,*/ EntityFieldDataType.CATEGORY_COLUMN,
                                            EntityFieldDataType.FOSTER_PARENT_ID,
                                            EntityFieldDataType.FOSTER_PARENT_TYPE, EntityFieldDataType.SCRATCH)
                                    .addSelect("id", "name", "label").addOrder("name"));
                }
            }
        }

        return Collections.emptyList();
    }

    public static class EnumListData implements Listable {

        private String name;

        public EnumListData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getListDescription() {
            return name;
        }

        @Override
        public String getListKey() {
            return name;
        }
    }
}
