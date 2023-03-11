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

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;
import com.tcdng.unify.core.util.DynamicEntityUtils;
import com.tcdng.unify.core.util.TypeInfo;

/**
 * Extension module entities Java class generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("extension-module-entities-java-generator")
public class ExtensionModuleEntitiesJavaGenerator extends AbstractStaticArtifactGenerator {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    public ExtensionModuleEntitiesJavaGenerator() {
        super("src/main/java/{0}/extension/{1}/entities/");
    }

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String moduleName, ZipOutputStream zos)
            throws UnifyException {
        for (DynamicEntityInfo dynamicEntityInfo : applicationModuleService
                .generateDynamicEntityInfos(ctx.getEntityList(), ctx.getBasePackage(), true)) {
            TypeInfo typeInfo = new TypeInfo(dynamicEntityInfo.getClassName());
            final String filename = typeInfo.getSimpleName() + ".java";
            openEntry(filename, zos);
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(zos));
                String src = DynamicEntityUtils.generateEntityJavaClassSource(dynamicEntityInfo);
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
