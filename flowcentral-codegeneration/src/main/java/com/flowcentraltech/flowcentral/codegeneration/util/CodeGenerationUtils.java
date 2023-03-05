/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.codegeneration.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.entities.BaseEntityWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.EntityFieldType;
import com.tcdng.unify.core.database.dynamic.DynamicChildFieldInfo;
import com.tcdng.unify.core.database.dynamic.DynamicChildListFieldInfo;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;
import com.tcdng.unify.core.database.dynamic.DynamicFieldInfo;
import com.tcdng.unify.core.database.dynamic.DynamicForeignKeyFieldInfo;
import com.tcdng.unify.core.util.SqlUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.TypeInfo;

/**
 * Code generation utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class CodeGenerationUtils {

    private CodeGenerationUtils() {

    }

    public static Map<String, String> splitMessageReplacements(String replacementList) {
        Map<String, String> replacements = Collections.emptyMap();
        if (!StringUtils.isBlank(replacementList)) {
            replacements = new HashMap<String, String>();
            String[] items = replacementList.split("\\|");
            for (String item : items) {
                String[] replacement = item.split("=");
                if (replacement.length == 2) {
                    replacements.put(replacement[0], replacement[1]);
                }
            }

            replacements = Collections.unmodifiableMap(replacements);
        }

        return replacements;
    }

    public static String generateEntityWrapperJavaClassSource(DynamicEntityInfo dynamicEntityInfo)
            throws UnifyException {
        StringBuilder esb = new StringBuilder();
        StringBuilder fsb = new StringBuilder();
        StringBuilder msb = new StringBuilder();
        Set<String> importSet = new HashSet<String>();

        // Evaluate fields
        Set<String> fieldNames = new HashSet<String>();
        for (DynamicFieldInfo dynamicFieldInfo : dynamicEntityInfo.getFieldInfos()) {
            final String fieldName = dynamicFieldInfo.getFieldName();
            final String capField = StringUtils.capitalizeFirstLetter(fieldName);
            fieldNames.add(fieldName);

            TypeInfo enumEntityInfo = null;
            if (dynamicFieldInfo.isEnum()) {
                enumEntityInfo = new TypeInfo(dynamicFieldInfo.getEnumClassName());
                importSet.add(dynamicFieldInfo.getEnumClassName());
            }

            final EntityFieldType type = dynamicFieldInfo.getFieldType();
            String childClass = null;
            if (type.isForeignKey()) {
                DynamicForeignKeyFieldInfo fkInfo = (DynamicForeignKeyFieldInfo) dynamicFieldInfo;
                if (!fkInfo.isEnum()) {
                    if (!dynamicEntityInfo.getClassName().equals(fkInfo.getParentDynamicEntityInfo().getClassName())) {
                        importSet.add(fkInfo.getParentDynamicEntityInfo().getClassName());
                    }
                }
            } else if (type.isChild()) {
                DynamicChildFieldInfo childInfo = (DynamicChildFieldInfo) dynamicFieldInfo;
                childClass = childInfo.getChildDynamicEntityInfo().getClassName();
                importSet.add(childClass);
            } else if (type.isChildList()) {
                DynamicChildListFieldInfo childListInfo = (DynamicChildListFieldInfo) dynamicFieldInfo;
                importSet.add(List.class.getCanonicalName());
                childClass = childListInfo.getChildDynamicEntityInfo().getClassName();
                importSet.add(childClass);
            }

            TypeInfo childEntityInfo = childClass != null ? new TypeInfo(childClass) : null;;
            String fieldTypeName = null;
            String actFieldTypeName = null;
            if (type.isChild()) {
                actFieldTypeName = fieldTypeName = childEntityInfo.getSimpleName();
            } else if (type.isChildList()) {
                actFieldTypeName = "List";
                fieldTypeName = "List<" + childEntityInfo.getSimpleName() + ">";
            } else {
                Class<?> javaClass = dynamicFieldInfo.getDataType().javaClass();
                if (Date.class.equals(javaClass)) {
                    importSet.add(Date.class.getCanonicalName());
                } else if (BigDecimal.class.equals(javaClass)) {
                    importSet.add(BigDecimal.class.getCanonicalName());
                }

                actFieldTypeName = fieldTypeName = enumEntityInfo != null ? enumEntityInfo.getSimpleName()
                        : javaClass.getSimpleName();
            }

            final String fieldNameConst = SqlUtils.generateSchemaElementName(fieldName, true);
            fsb.append(" public static final String ").append(fieldNameConst).append(" = \"").append(fieldName)
                    .append("\";\n");

            msb.append(" public ").append(fieldTypeName).append(" get").append(capField).append("() {return ")
                    .append("(").append(fieldTypeName).append(") valueStore.retrieve(").append(actFieldTypeName)
                    .append(".class, ").append(fieldNameConst).append(";}\n");
            msb.append(" public void set").append(capField).append("(").append(fieldTypeName).append(" ")
                    .append(fieldName).append(") {valueStore.store(").append(fieldNameConst).append(",")
                    .append(fieldName).append(");}\n");
        }

        // Construct class
        TypeInfo baseEntityInfo = new TypeInfo(BaseEntityWrapper.class);
        TypeInfo typeInfo = new TypeInfo(dynamicEntityInfo.getClassName() + "Wrapper");
        final int index = typeInfo.getPackageName().lastIndexOf('.');
        final String packageName = typeInfo.getPackageName().substring(0, index) + "entitywrappers";
        esb.append("package ").append(packageName).append(";\n");
        List<String> importList = new ArrayList<String>(importSet);
        Collections.sort(importList);
        for (String imprt : importList) {
            esb.append("import ").append(imprt).append(";\n");
        }

        esb.append("import ").append(baseEntityInfo.getCanonicalName()).append(";\n");

        esb.append("public class ").append(typeInfo.getSimpleName()).append(" extends ")
                .append(baseEntityInfo.getSimpleName()).append(" {\n");
        esb.append(fsb);
        esb.append(msb);
        esb.append("}\n");
        return esb.toString();
    }
}
