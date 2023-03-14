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

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.entities.BaseEntityWrapper;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.notification.data.BaseNotifTemplateWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifTemplateInfo;
import com.flowcentraltech.flowcentral.notification.util.NotificationCodeGenUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.EntityFieldType;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;
import com.tcdng.unify.core.database.dynamic.DynamicFieldInfo;
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

    private static final Set<String> RESERVED_BASE_FIELDS;

    static {
        Set<String> reserved = new HashSet<String>(ApplicationEntityUtils.RESERVED_BASE_FIELDS);
        reserved.remove("id");
        RESERVED_BASE_FIELDS = Collections.unmodifiableSet(reserved);
    }

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

        TypeInfo entityEntityInfo = new TypeInfo(Entity.class);
        TypeInfo exceptionEntityInfo = new TypeInfo(UnifyException.class);
        TypeInfo listEntityInfo = new TypeInfo(List.class);
        TypeInfo entityClassDefEntityInfo = new TypeInfo(EntityClassDef.class);
        TypeInfo valueStoreEntityInfo = new TypeInfo(ValueStore.class);
        importSet.add(entityEntityInfo.getCanonicalName());
        importSet.add(exceptionEntityInfo.getCanonicalName());
        importSet.add(listEntityInfo.getCanonicalName());
        importSet.add(entityClassDefEntityInfo.getCanonicalName());
        importSet.add(valueStoreEntityInfo.getCanonicalName());

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
            String fieldTypeName = null;
            String actFieldTypeName = null;
            if (type.isChild()) {
                actFieldTypeName = fieldTypeName = entityEntityInfo.getSimpleName();
            } else if (type.isChildList()) {
                actFieldTypeName = "List";
                fieldTypeName = "List<? extends " + entityEntityInfo.getSimpleName() + ">";
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

            msb.append(" public ").append(fieldTypeName).append(" get").append(capField)
                    .append("() throws UnifyException {return ").append("(").append(fieldTypeName)
                    .append(") valueStore.retrieve(").append(actFieldTypeName).append(".class, ").append(fieldNameConst)
                    .append(");}\n");

            if (!RESERVED_BASE_FIELDS.contains(fieldName)) {
                msb.append(" public void set").append(capField).append("(").append(fieldTypeName).append(" ")
                        .append(fieldName).append(") throws UnifyException {valueStore.store(").append(fieldNameConst)
                        .append(",").append(fieldName).append(");}\n");
            }
        }

        // Construct class
        TypeInfo baseEntityInfo = new TypeInfo(BaseEntityWrapper.class);
        TypeInfo typeInfo = new TypeInfo(dynamicEntityInfo.getClassName() + "Wrapper");
        final int index = typeInfo.getPackageName().lastIndexOf('.');
        final String packageName = typeInfo.getPackageName().substring(0, index) + ".entitywrappers";
        esb.append("package ").append(packageName).append(";\n");
        List<String> importList = new ArrayList<String>(importSet);
        Collections.sort(importList);
        for (String imprt : importList) {
            esb.append("import ").append(imprt).append(";\n");
        }

        esb.append("import ").append(baseEntityInfo.getCanonicalName()).append(";\n");

        esb.append("public class ").append(typeInfo.getSimpleName()).append(" extends ")
                .append(baseEntityInfo.getSimpleName()).append(" {\n");
        esb.append("public static final String ").append(ApplicationCodeGenUtils.ENTITY_NAME).append(" = \"")
                .append(dynamicEntityInfo.getAlias()).append("\";\n");
        esb.append(fsb);
        esb.append("public ").append(typeInfo.getSimpleName())
                .append("(EntityClassDef entityClassDef) throws UnifyException {super(entityClassDef);}\n");
        esb.append("public ").append(typeInfo.getSimpleName()).append(
                "(EntityClassDef entityClassDef, ValueStore valueStore) throws UnifyException {super(entityClassDef, valueStore);}\n");
        esb.append("public ").append(typeInfo.getSimpleName()).append(
                "(EntityClassDef entityClassDef, Entity inst) throws UnifyException {super(entityClassDef, inst);}\n");
        esb.append("public ").append(typeInfo.getSimpleName()).append(
                "(EntityClassDef entityClassDef, List<? extends Entity> instList) throws UnifyException {super(entityClassDef, instList);}\n");
        esb.append(msb);
        esb.append("}\n");
        return esb.toString();
    }

    public static String generateTemplateWrapperJavaClassSource(DynamicNotifTemplateInfo dynamicTemplateInfo)
            throws UnifyException {
        StringBuilder esb = new StringBuilder();
        StringBuilder msb = new StringBuilder();
        Set<String> importSet = new HashSet<String>();

        TypeInfo notifTemplateDefEntityInfo = new TypeInfo(NotifTemplateDef.class);
        TypeInfo exceptionEntityInfo = new TypeInfo(UnifyException.class);
        importSet.add(notifTemplateDefEntityInfo.getCanonicalName());
        importSet.add(exceptionEntityInfo.getCanonicalName());

        // Evaluate parameters
        for (final String param : dynamicTemplateInfo.getParams()) {
            final String capParam = StringUtils.capitalizeFirstLetter(param);
            msb.append(" public void set").append(capParam)
                    .append("(String val) throws UnifyException {nmb.addParam(\"").append(param).append("\", val);}\n");
        }

        // Construct class
        TypeInfo baseEntityInfo = new TypeInfo(BaseNotifTemplateWrapper.class);
        TypeInfo typeInfo = new TypeInfo(dynamicTemplateInfo.getTemplateClassName() + "Wrapper");
        final int index = typeInfo.getPackageName().lastIndexOf('.');
        final String packageName = typeInfo.getPackageName().substring(0, index) + ".templatewrappers";
        esb.append("package ").append(packageName).append(";\n");
        List<String> importList = new ArrayList<String>(importSet);
        Collections.sort(importList);
        for (String imprt : importList) {
            esb.append("import ").append(imprt).append(";\n");
        }

        esb.append("import ").append(baseEntityInfo.getCanonicalName()).append(";\n");

        esb.append("public class ").append(typeInfo.getSimpleName()).append(" extends ")
                .append(baseEntityInfo.getSimpleName()).append(" {\n");
        esb.append("public static final String ").append(NotificationCodeGenUtils.TEMPLATE_NAME).append(" = \"")
                .append(dynamicTemplateInfo.getTemplateName()).append("\";\n");
        esb.append("public ").append(typeInfo.getSimpleName())
                .append("(NotifTemplateDef notifTemplateDef) throws UnifyException {super(notifTemplateDef);}\n");
        esb.append(msb);
        esb.append("}\n");
        return esb.toString();
    }
}
