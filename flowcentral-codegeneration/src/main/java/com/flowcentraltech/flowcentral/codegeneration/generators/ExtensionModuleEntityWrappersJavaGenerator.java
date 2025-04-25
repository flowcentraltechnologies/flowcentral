/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.codegeneration.util.CodeGenerationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;
import com.tcdng.unify.core.util.TypeInfo;

/**
 * Extension module entity wrappers Java class generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("extension-module-entitywrappers-java-generator")
public class ExtensionModuleEntityWrappersJavaGenerator extends AbstractJavaArtifactGenerator {

    @Configurable
    private AppletUtilities au;

    public ExtensionModuleEntityWrappersJavaGenerator() {
        super("{0}/utilities/{1}/entitywrappers/");
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String moduleName, ZipOutputStream zos)
            throws UnifyException {
        final boolean skipPasswordFields = au.system().getSysParameterValue(boolean.class,
                CodeGenerationModuleSysParamConstants.CODEGEN_SKIP_PASSWORD_FIELDS);
        for (DynamicEntityInfo dynamicEntityInfo : au.application().generateDynamicEntityInfos(ctx.getEntityList(),
                ctx.getBasePackage(), false)) {
            dynamicEntityInfo.setSkipPasswordFields(skipPasswordFields);
            TypeInfo typeInfo = new TypeInfo(dynamicEntityInfo.getClassName());
            final String filename = typeInfo.getSimpleName() + "Wrapper.java";
            openEntry(ctx, filename, zos);
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(zos));
                String src = CodeGenerationUtils.generateEntityWrapperJavaClassSource(ApplicationCodeGenUtils
                        .generateUtilitiesEntityWrapperPackageName(ctx.getBasePackage(), moduleName),
                        dynamicEntityInfo);
                bw.write(src);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                throwOperationErrorException(e);
            }
            closeEntry(zos);
        }

        closeEntry(zos);
    }
}
