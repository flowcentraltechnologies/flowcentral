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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.ZipOutputStream;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Extension module static settings Java class generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("extension-module-static-settings-java-generator")
public class ExtensionModuleStaticSettingsJavaGenerator extends AbstractStaticArtifactGenerator {

    public ExtensionModuleStaticSettingsJavaGenerator() {
        super("src/main/java/{0}/extension/{1}/constants/");
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String moduleName, ZipOutputStream zos)
            throws UnifyException {
        final String capModuleName = StringUtils.capitalizeFirstLetter(moduleName);
        final String lowerModuleName = moduleName.toLowerCase();
        final String className = capModuleName + "ModuleStaticSettings";
        final String filename = className + ".java";
        final String packageName = ctx.getBasePackage() + ".extension." + lowerModuleName + ".constants";

        openEntry(filename, zos);

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(zos));
        pw.print("package ");
        pw.print(packageName);
        pw.println(';');
        pw.println();

        pw.println(
                "import com.flowcentraltech.flowcentral.configuration.constants.AbstractExtensionModuleStaticSettings;");
        pw.println("import com.tcdng.unify.core.annotation.AutoDetect;");
        pw.println();
        pw.println();

        pw.println("@AutoDetect");
        pw.print("public class ");
        pw.print(className);
        pw.println(" extends AbstractExtensionModuleStaticSettings {");
        pw.println();
        pw.print("\tpublic ");
        pw.print(className);
        pw.println("() {");

        pw.print("\t\tsuper(null, \"config/extension-");
        pw.print(lowerModuleName);
        pw.println("-module.xml\",");
        pw.print("\t\t\t\"");
        pw.print(ctx.getBasePackage());
        pw.print(".resources.extension-");
        pw.print(lowerModuleName);
        pw.print("-messages\");");

        pw.println("\t}");

        pw.println("}");
        pw.println();

        pw.flush();

        closeEntry(zos);
    }
}
