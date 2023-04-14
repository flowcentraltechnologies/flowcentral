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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo;
import com.flowcentraltech.flowcentral.codegeneration.util.CodeGenerationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.TypeInfo;

/**
 * Extension module component names Java class generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("extension-module-componentnames-java-generator")
public class ExtensionModuleComponentNamesJavaGenerator extends AbstractStaticArtifactGenerator {

    public ExtensionModuleComponentNamesJavaGenerator() {
        super("src/main/java/{0}/utilities/{1}/constants/");
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String moduleName, ZipOutputStream zos)
            throws UnifyException {
        final DynamicModuleInfo dynamicModuleInfo = ctx.getDynamicModuleInfo();
        final String className = CodeGenerationUtils.generateUtilitiesConstantsClassName(ctx.getBasePackage(),
                moduleName, moduleName);
        final TypeInfo typeInfo = new TypeInfo(className);

        final String filename = typeInfo.getSimpleName() + ".java";
        openEntry(filename, zos);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(zos));
            String src = CodeGenerationUtils.generateModuleNameConstantsJavaClassSource(typeInfo, CodeGenerationUtils
                    .generateUtilitiesConstantsPackageName(ctx.getBasePackage(), moduleName),
                    dynamicModuleInfo);
            bw.write(src);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throwOperationErrorException(e);
        }
        closeEntry(zos);

        closeEntry(zos);
    }
}
