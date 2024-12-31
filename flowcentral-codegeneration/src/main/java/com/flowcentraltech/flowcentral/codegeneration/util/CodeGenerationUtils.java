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

package com.flowcentraltech.flowcentral.codegeneration.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.entities.BaseEntityWrapper;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo;
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo.ApplicationInfo;
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo.EnumInfo;
import com.flowcentraltech.flowcentral.common.constants.ComponentType;
import com.flowcentraltech.flowcentral.common.entities.EntityWrapperIterator;
import com.flowcentraltech.flowcentral.notification.data.BaseNotifLargeTextWrapper;
import com.flowcentraltech.flowcentral.notification.data.BaseNotifTemplateWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextDef;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifLargeTextInfo;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifTemplateInfo;
import com.flowcentraltech.flowcentral.notification.util.NotificationCodeGenUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DynamicEntityFieldType;
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

    public static String generateUtilitiesConstantsClassName(String basePackage, String moduleName,
            String constantName) {
        return CodeGenerationUtils.generateUtilitiesConstantsPackageName(basePackage, moduleName) + "."
                + StringUtils.capitalizeFirstLetter(constantName) + "Module";
    }

    public static String generateUtilitiesConstantsPackageName(String basePackage, String moduleName) {
        return basePackage + ".utilities." + moduleName.toLowerCase() + ".constants";
    }

    private static final String PASSWORD = "password";

    public static String generateEntityWrapperJavaClassSource(String packageName, DynamicEntityInfo dynamicEntityInfo)
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

        importSet.add(Iterator.class.getCanonicalName());
        importSet.add(EntityWrapperIterator.class.getCanonicalName());

        // Evaluate fields
        Set<String> fieldNames = new HashSet<String>();
        for (DynamicFieldInfo dynamicFieldInfo : dynamicEntityInfo.getFieldInfos()) {
            final String fieldName = dynamicFieldInfo.getFieldName();
            if (dynamicEntityInfo.isSkipPasswordFields() && fieldName.toLowerCase().contains(PASSWORD)) {
                continue;
            }

            final String capField = StringUtils.capitalizeFirstLetter(fieldName);
            fieldNames.add(fieldName);

            TypeInfo enumEntityInfo = null;
            if (dynamicFieldInfo.isEnum()) {
                enumEntityInfo = new TypeInfo(dynamicFieldInfo.getEnumClassName());
                importSet.add(dynamicFieldInfo.getEnumClassName());
            }

            final DynamicEntityFieldType type = dynamicFieldInfo.getFieldType();
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
        esb.append("package ").append(packageName).append(";\n");
        List<String> importList = new ArrayList<String>(importSet);
        Collections.sort(importList);
        for (String imprt : importList) {
            esb.append("import ").append(imprt).append(";\n");
        }

        esb.append("import ").append(baseEntityInfo.getCanonicalName()).append(";\n");

        esb.append("public class ").append(typeInfo.getSimpleName()).append(" extends ")
                .append(baseEntityInfo.getSimpleName()).append(" implements Iterable<").append(typeInfo.getSimpleName())
                .append("> {\n");
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
        esb.append("public Iterator<").append(typeInfo.getSimpleName())
                .append("> iterator() {return new EntityWrapperIterator<").append(typeInfo.getSimpleName())
                .append(">(this);}");
        esb.append("}\n");
        return esb.toString();
    }

    public static String generateTemplateWrapperJavaClassSource(String packageName,
            DynamicNotifTemplateInfo dynamicTemplateInfo) throws UnifyException {
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

    public static String generateLargeTextWrapperJavaClassSource(String packageName,
            DynamicNotifLargeTextInfo dynamicLargeTextInfo) throws UnifyException {
        StringBuilder esb = new StringBuilder();
        StringBuilder msb = new StringBuilder();
        Set<String> importSet = new HashSet<String>();

        TypeInfo notifLargeTextDefEntityInfo = new TypeInfo(NotifLargeTextDef.class);
        TypeInfo exceptionEntityInfo = new TypeInfo(UnifyException.class);
        TypeInfo mapEntityInfo = new TypeInfo(Map.class);
        importSet.add(notifLargeTextDefEntityInfo.getCanonicalName());
        importSet.add(exceptionEntityInfo.getCanonicalName());
        importSet.add(mapEntityInfo.getCanonicalName());

        // Evaluate parameters
        for (final String param : dynamicLargeTextInfo.getParams()) {
            final String capParam = StringUtils.capitalizeFirstLetter(param);
            msb.append(" public void set").append(capParam)
                    .append("(String val) throws UnifyException {properties.put(\"").append(param)
                    .append("\", val);}\n");
        }

        // Construct class
        TypeInfo baseEntityInfo = new TypeInfo(BaseNotifLargeTextWrapper.class);
        TypeInfo typeInfo = new TypeInfo(dynamicLargeTextInfo.getLargeTextClassName() + "Wrapper");
        esb.append("package ").append(packageName).append(";\n");
        List<String> importList = new ArrayList<String>(importSet);
        Collections.sort(importList);
        for (String imprt : importList) {
            esb.append("import ").append(imprt).append(";\n");
        }

        esb.append("import ").append(baseEntityInfo.getCanonicalName()).append(";\n");

        esb.append("public class ").append(typeInfo.getSimpleName()).append(" extends ")
                .append(baseEntityInfo.getSimpleName()).append(" {\n");
        esb.append("public static final String ").append(NotificationCodeGenUtils.LARGETEXT_NAME).append(" = \"")
                .append(dynamicLargeTextInfo.getLargeTextName()).append("\";\n");
        esb.append("public ").append(typeInfo.getSimpleName())
                .append("(NotifLargeTextDef notifLargeTextDef) throws UnifyException {super(notifLargeTextDef);}\n");
        esb.append("public ").append(typeInfo.getSimpleName()).append(
                "(NotifLargeTextDef notifLargeTextDef, Map<String, Object> parameters) throws UnifyException {super(notifLargeTextDef, parameters);}\n");
        esb.append(msb);
        esb.append("}\n");
        return esb.toString();
    }

    public static String generateModuleNameConstantsJavaClassSource(TypeInfo typeInfo, String packageName,
            DynamicModuleInfo dynamicModuleInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n");
        sb.append("public interface ").append(typeInfo.getSimpleName()).append(" {\n");
        sb.append("String NAME = \"").append(dynamicModuleInfo.getModuleName()).append("\";\n");
        for (ApplicationInfo applicationInfo : dynamicModuleInfo.getApplications()) {
            sb.append("interface ").append(StringUtils.capitalizeFirstLetter(applicationInfo.getApplicationName()))
                    .append("Application {\n");
            sb.append("String NAME = \"").append(applicationInfo.getApplicationName()).append("\";\n");

            for (Map.Entry<ComponentType, List<String>> entry : applicationInfo.getComponentNames().entrySet()) {
                sb.append("interface ").append(StringUtils.capitalizeFirstLetter(entry.getKey().term())).append(" {\n");
                for (String componentName : entry.getValue()) {
                    final String fieldNameConst = SqlUtils.generateSchemaElementName(componentName, true);
                    sb.append("String ").append(fieldNameConst).append(" = \"")
                            .append(applicationInfo.getApplicationName()).append('.').append(componentName)
                            .append("\";\n");
                }

                sb.append("}\n");
            }

            sb.append("}\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    public static String generateApplicationEnumJavaClassSource(TypeInfo typeInfo, String packageName,
            ApplicationInfo applicationInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n");
        sb.append("public interface ").append(StringUtils.capitalizeFirstLetter(applicationInfo.getApplicationName()))
                .append("Enums {\n");
        sb.append("String NAME = \"").append(applicationInfo.getApplicationName()).append("\";\n");

        for (EnumInfo enumInfo : applicationInfo.getEnumerations()) {
            sb.append("interface ").append(StringUtils.capitalizeFirstLetter(enumInfo.getEnumName())).append(" {\n");
            for (Map.Entry<String, String> entry : enumInfo.getOptions().entrySet()) {
                final String fieldNameConst = StringUtils.flatten(entry.getValue()).toUpperCase();
                sb.append("String ").append(fieldNameConst).append(" = \"").append(entry.getKey()).append("\";\n");
            }

            sb.append("}\n");
        }

        sb.append("}\n");
        return sb.toString();
    }
}
