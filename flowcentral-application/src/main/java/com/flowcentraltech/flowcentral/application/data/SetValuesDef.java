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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.EntitySetValuesGenerator;
import com.flowcentraltech.flowcentral.application.business.FieldSetValueGenerator;
import com.flowcentraltech.flowcentral.application.constants.ProcessVariable;
import com.flowcentraltech.flowcentral.application.util.GeneratorNameParts;
import com.flowcentraltech.flowcentral.application.util.GeneratorNameUtils;
import com.flowcentraltech.flowcentral.common.constants.SessionParamType;
import com.flowcentraltech.flowcentral.common.util.LingualDateUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.LingualDateType;
import com.flowcentraltech.flowcentral.configuration.constants.LingualStringType;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application set values definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetValuesDef {

    private final List<SetValueDef> setValueList;

    private final String valueGenerator;

    private final String name;

    private final String description;

    private Set<String> fields;

    private SetValuesDef(List<SetValueDef> setValueList, String valueGenerator, String name, String description) {
        this.setValueList = setValueList;
        this.valueGenerator = valueGenerator;
        this.name = name;
        this.description = description;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getFields() {
        if (fields == null) {
            synchronized (this) {
                if (fields == null) {
                    fields = new HashSet<String>();
                    if (setValueList != null) {
                        for (SetValueDef setValueDef : setValueList) {
                            fields.add(setValueDef.getFieldName());
                        }
                    }

                    fields = Collections.unmodifiableSet(fields);
                }
            }
        }
        return fields;
    }

    public List<SetValueDef> getSetValueList() {
        return setValueList;
    }

    public boolean isBlank() {
        return setValueList == null || setValueList.isEmpty();
    }

    public void apply(AppletUtilities au, EntityDef entityDef, Date now, Entity inst, Map<String, Object> variables,
            String trigger) throws UnifyException {
        apply(au, entityDef, now, new BeanValueStore(inst), variables, trigger);
    }

    public void apply(AppletUtilities au, EntityDef entityDef, Date now, ValueStore valueStore,
            Map<String, Object> variables, String trigger) throws UnifyException {
        if (!StringUtils.isBlank(valueGenerator)) {
            EntitySetValuesGenerator entityValueGenerator = au.getComponent(EntitySetValuesGenerator.class,
                    valueGenerator);
            entityValueGenerator.generate(entityDef, valueStore, trigger);
        }

        for (SetValueDef setValueDef : setValueList) {
            final EntityFieldDef entityFieldDef = entityDef.getFieldDef(setValueDef.getFieldName());
            if (entityFieldDef.isSupportSetValue()) {
                final EntityFieldDataType fieldType = entityFieldDef.getDataType();
                Object val = null;
                switch (setValueDef.getType()) {
                    case SESSION_PARAMETER:
                        SessionParamType sessionParamtype = SessionParamType.fromCode(setValueDef.getParam());
                        if (sessionParamtype != null) {
                            val = au.getSessionAttribute(Object.class, sessionParamtype.attribute());
                        }
                        break;
                    case PROCESS_VARIABLE:
                        ProcessVariable variable = DataUtils.convert(ProcessVariable.class, setValueDef.getParam());
                        if (variable != null) {
                            val = variables.get(variable.variableKey());
                        }
                        break;
                    case GENERATOR:
                        String generatorName = setValueDef.getParam();
                        String rule = null;
                        if (GeneratorNameUtils.isFullNameWithParts(generatorName)) {
                            GeneratorNameParts np = GeneratorNameUtils.getGeneratorNameParts(generatorName);
                            generatorName = np.getComponentName();
                            rule = np.getRule();
                        }

                        FieldSetValueGenerator gen = au.getComponent(FieldSetValueGenerator.class, generatorName);
                        val = gen.generate(entityDef, valueStore, rule);
                        break;
                    case NULL:
                        val = null;
                        break;
                    case IMMEDIATE_LINGUAL:
                        if (EntityFieldDataType.STRING.equals(fieldType)) {
                            LingualStringType lingType = DataUtils.convert(LingualStringType.class,
                                    setValueDef.getParam());
                            if (lingType != null) {
                                val = lingType.value();
                            }
                        } else if (fieldType.isDate() || fieldType.isTimestamp()) {
                            val = LingualDateUtils.getDateFromNow(now,
                                    DataUtils.convert(LingualDateType.class, setValueDef.getParam()));
                        }
                        break;
                    case IMMEDIATE_FIELD:
                        val = evaluateImmediateField(au, valueStore, setValueDef.getParam());
                        break;
                    case IMMEDIATE:
                    default:
                        val = setValueDef.getParam();
                        break;
                }

                // Make sure it is impossible to set a tenant ID field with an invalid value
                if (entityFieldDef.isTenantId()) {
                    val = au.getMappedDestTenantId((Long) val);
                    if (val == null) {
                        throw new IllegalArgumentException(
                                "Attempting to set tenant ID field with an invalid value. Definition [{" + name
                                        + "}].");
                    }
                }

                valueStore.store(setValueDef.getFieldName(), val);
            }
        }
    }

    private Object evaluateImmediateField(AppletUtilities au, ValueStore valueStore, String fieldName)
            throws UnifyException {
        Object val = valueStore.retrieve(fieldName);
        if ((val instanceof String) && ((String) val).indexOf('{') >= 0) {
            List<StringToken> tokenList = StringUtils.breakdownParameterizedString((String) val);
            ParameterizedStringGenerator generator = au.getStringGenerator(valueStore.getReader(), tokenList);
            return generator.generate();
        }

        return val;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private String description;

        private String valueGenerator;

        private List<SetValueDef> setValueList;

        public Builder() {
            setValueList = new ArrayList<SetValueDef>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder valueGenerator(String valueGenerator) {
            this.valueGenerator = valueGenerator;
            return this;
        }

        public Builder addSetValueDef(SetValueType type, String fieldName, String param) {
            setValueList.add(new SetValueDef(type, fieldName, param));
            return this;
        }

        public SetValuesDef build() throws UnifyException {
            return new SetValuesDef(DataUtils.unmodifiableList(setValueList), valueGenerator, name, description);
        }
    }
}
