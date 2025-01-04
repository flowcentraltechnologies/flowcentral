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
package com.flowcentraltech.flowcentral.connect.unify;

import java.lang.reflect.Field;
import java.util.Map;

import com.flowcentraltech.flowcentral.connect.common.AbstractInterconnect;
import com.flowcentraltech.flowcentral.connect.common.data.EntityFieldInfo;
import com.flowcentraltech.flowcentral.connect.common.data.EntityInfo;
import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectFieldDataType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.TableName;

/**
 * Flowcentral unify interconnect.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UnifyInterconnect extends AbstractInterconnect {

    public UnifyInterconnect() {
        super(RefType.OBJECT);
    }

    @Override
    protected String getTableName(Class<?> entityClass) throws Exception {
        Table ta = entityClass.getAnnotation(Table.class);
        String tableName =  ta != null ? ta.name() : null;
        if (tableName == null) {
            TableName tna = entityClass.getAnnotation(TableName.class);
            tableName =  tna != null ? tna.name() : null;
        }
        
        return tableName;
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
        if (type.isString()) {
            if (ca.type().isLob()) {
                type = ConnectFieldDataType.CLOB;
            }
        } else if (type.isDate()) {
            if (ca.type().isTimestamp()) {
                type = ConnectFieldDataType.TIMESTAMP;
            }
        }

        if (ca != null) {
            column = ca.name();
            length = type.isString() ? ca.length() : 0;
            precision = ca.precision();
            scale = ca.scale();
            nullable = ca.nullable();
        }

        return new EntityFieldInfo(type, field.getType(), name, description, column, fieldTypeInfo.getReferences(),
                fieldTypeInfo.getEnumImplClass(), precision, scale, length, nullable);
    }
}
