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
package com.flowcentraltech.flowcentral.connect.springboot;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flowcentraltech.flowcentral.connect.common.AbstractInterconnect;
import com.flowcentraltech.flowcentral.connect.common.data.EntityFieldInfo;
import com.flowcentraltech.flowcentral.connect.common.data.EntityInfo;
import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectFieldDataType;

/**
 * Flowcentral spring boot interconnect.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component
public class SpringBootInterconnect extends AbstractInterconnect {

    @Autowired
    public SpringBootInterconnect() {
        super(RefType.OBJECT);
    }

    @Override
    protected EntityFieldInfo createEntityFieldInfo(Map<String, EntityInfo> _entitiesbyclassname, Field field)
            throws Exception {
        FieldTypeInfo fieldTypeInfo = getFieldTypeInfo(_entitiesbyclassname, field);
        final String name = field.getName();
        ConnectFieldDataType type = fieldTypeInfo.getType();
        String description = null;
        String column = null;
        int length = 0;
        int precision = 0;
        int scale = 0;
        boolean nullable = false;

        Column ca = field.getAnnotation(Column.class);
        if (ca != null) {
            column = ca.name();
            length = ca.length();
            precision = ca.precision();
            scale = ca.scale();
            nullable = ca.nullable();
        }

        if (type.isDate()) {
            Temporal ta = field.getAnnotation(Temporal.class);
            if (ta != null && (TemporalType.TIMESTAMP.equals(ta.value()) || TemporalType.TIME.equals(ta.value()))) {
                type = ConnectFieldDataType.TIMESTAMP;
            }
        }

        return new EntityFieldInfo(type, name, description, column, fieldTypeInfo.getReferences(),
                fieldTypeInfo.getEnumImplClass(), precision, scale, length, nullable);
    }
}
