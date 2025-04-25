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

import com.flowcentraltech.flowcentral.codegeneration.util.CodeGenerationUtils;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifTemplateInfo;
import com.flowcentraltech.flowcentral.notification.util.NotificationCodeGenUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.TypeInfo;

/**
 * Extension module notification template wrappers Java class generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("extension-module-templatewrappers-java-generator")
public class ExtensionModuleNotifTemplateWrappersJavaGenerator extends AbstractJavaArtifactGenerator {

    @Configurable
    private NotificationModuleService notificationModuleService;

    public ExtensionModuleNotifTemplateWrappersJavaGenerator() {
        super("{0}/utilities/{1}/templatewrappers/");
    }

    protected final boolean checkGeneration(ExtensionModuleStaticFileBuilderContext ctx, String entityName)
            throws UnifyException {
        return notificationModuleService.countNotifTemplatesByModule(entityName) > 0;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String moduleName, ZipOutputStream zos)
            throws UnifyException {
        for (DynamicNotifTemplateInfo dynamicTemplateInfo : notificationModuleService
                .generateNotifTemplateInfos(ctx.getBasePackage(), moduleName)) {
            TypeInfo typeInfo = new TypeInfo(dynamicTemplateInfo.getTemplateClassName());
            final String filename = typeInfo.getSimpleName() + "Wrapper.java";
            openEntry(ctx, filename, zos);
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(zos));
                String src = CodeGenerationUtils.generateTemplateWrapperJavaClassSource(NotificationCodeGenUtils
                        .generateUtilitiesTemplateWrapperPackageName(ctx.getBasePackage(), moduleName),
                        dynamicTemplateInfo);
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
