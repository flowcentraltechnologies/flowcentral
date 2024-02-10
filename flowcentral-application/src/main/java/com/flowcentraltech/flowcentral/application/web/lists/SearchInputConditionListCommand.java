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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ListManager;

/**
 * Search input condition list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("searchinputconditionlist")
public class SearchInputConditionListCommand extends AbstractApplicationListCommand<EntityDefFieldListParams> {

    private DataTypeConditions dataTypeConditions;

    public SearchInputConditionListCommand() {
        super(EntityDefFieldListParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityDefFieldListParams params) throws UnifyException {
        if (params.isPresent()) {
            if (params.getFieldName().startsWith("f:")) {
                EntityFieldDef entityFieldDef = params.getEntityDef().getFieldDef(params.getFieldName().substring(2));
                if (entityFieldDef.isWithResolvedTypeFieldDef()) {
                    entityFieldDef = entityFieldDef.getResolvedTypeFieldDef();
                }

                DataTypeConditions _dataTypeConditions = getDataTypeConditions();
                EntityFieldDataType type = entityFieldDef.getDataType();
                if (type.isBoolean()) {
                    return _dataTypeConditions.getBooleanConditions();
                }

                if (type.isNumber() || type.isDate() || type.isTimestamp()) {
                    return _dataTypeConditions.getNumberConditions();
                }

                if (type.isEnumDataType()) {
                    return _dataTypeConditions.getEnumConditions();
                }

                if (type.isString()) {
                    return _dataTypeConditions.getStringConditions();
                }
            }
        }

        return Collections.emptyList();
    }

    private DataTypeConditions getDataTypeConditions() throws UnifyException {
        if (dataTypeConditions == null) {
            synchronized (this) {
                if (dataTypeConditions == null) {
                    ListManager listManager = getComponent(ListManager.class);
                    List<Listable> booleanConditions = new ArrayList<>();
                    List<Listable> numberConditions = new ArrayList<>();
                    List<Listable> stringConditions = new ArrayList<>();
                    List<Listable> enumConditions = new ArrayList<>();
                    List<? extends Listable> srclist = listManager.getList(getApplicationLocale(),
                            "searchconditiontypelist");
                    for (Listable listable : srclist) {
                        SearchConditionType condition = SearchConditionType.fromCode(listable.getListKey());
                        if (condition.supportsBoolean()) {
                            booleanConditions.add(listable);
                        }

                        if (condition.supportsNumber()) {
                            numberConditions.add(listable);
                        }

                        if (condition.supportsString()) {
                            stringConditions.add(listable);
                        }

                        if (condition.supportsEnum()) {
                            enumConditions.add(listable);
                        }
                    }

                    dataTypeConditions = new DataTypeConditions(booleanConditions, numberConditions, stringConditions,
                            enumConditions);
                }
            }
        }

        return dataTypeConditions;
    }

    private class DataTypeConditions {

        private final List<? extends Listable> booleanConditions;

        private final List<? extends Listable> numberConditions;

        private final List<? extends Listable> stringConditions;

        private final List<? extends Listable> enumConditions;

        public DataTypeConditions(List<? extends Listable> booleanConditions, List<? extends Listable> numberConditions,
                List<? extends Listable> stringConditions, List<? extends Listable> enumConditions) {
            this.booleanConditions = Collections.unmodifiableList(booleanConditions);
            this.numberConditions = Collections.unmodifiableList(numberConditions);
            this.stringConditions = Collections.unmodifiableList(stringConditions);
            this.enumConditions = Collections.unmodifiableList(enumConditions);
        }

        public List<? extends Listable> getBooleanConditions() {
            return booleanConditions;
        }

        public List<? extends Listable> getNumberConditions() {
            return numberConditions;
        }

        public List<? extends Listable> getStringConditions() {
            return stringConditions;
        }

        public List<? extends Listable> getEnumConditions() {
            return enumConditions;
        }
    }

}
