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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for module static artifact generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Singleton(false)
public abstract class AbstractStaticModuleArtifactGenerator extends AbstractFlowCentralComponent
        implements StaticModuleArtifactGenerator {

    @Configurable
    private AppletUtilities appletUtilities;

    private String zipDir;

    private String snapshotZipDir;

    protected AbstractStaticModuleArtifactGenerator(String zipDir, String snapshotZipDir) {
        this.zipDir = zipDir;
        this.snapshotZipDir = snapshotZipDir;
    }

    protected AbstractStaticModuleArtifactGenerator(String zipDir) {
        this.zipDir = zipDir;
    }

    protected AbstractStaticModuleArtifactGenerator() {

    }

    @Override
    public final void generate(ExtensionModuleStaticFileBuilderContext ctx, String entityName, ZipOutputStream zos)
            throws UnifyException {
        if (checkGeneration(ctx, entityName)) {
            if (ctx.isSnapshotMode()) {
                if (!StringUtils.isBlank(snapshotZipDir)) {
                    if (snapshotZipDir.indexOf('{') >= 0) {
                        String packageFolder = ctx.getBasePackage().replaceAll("\\.", "/");
                        String lowerEntityName = entityName.toLowerCase();
                        snapshotZipDir = MessageFormat.format(snapshotZipDir, packageFolder, lowerEntityName);
                    }

                    if (ctx.addZipDir(snapshotZipDir)) {
                        try {
                            zos.putNextEntry(new ZipEntry(snapshotZipDir));
                        } catch (IOException e) {
                            throwOperationErrorException(e);
                        }
                    }
                }
            } else {
                if (!StringUtils.isBlank(zipDir)) {
                    if (zipDir.indexOf('{') >= 0) {
                        String packageFolder = ctx.getBasePackage().replaceAll("\\.", "/");
                        String lowerEntityName = entityName.toLowerCase();
                        zipDir = MessageFormat.format(zipDir, packageFolder, lowerEntityName);
                    }

                    if (ctx.addZipDir(zipDir)) {
                        try {
                            zos.putNextEntry(new ZipEntry(zipDir));
                        } catch (IOException e) {
                            throwOperationErrorException(e);
                        }
                    }
                }
            }

            doGenerate(ctx, entityName, zos);
        }
    }

    protected AppletUtilities au() {
        return appletUtilities;
    }

    protected abstract void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String name, ZipOutputStream zos)
            throws UnifyException;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected boolean checkGeneration(ExtensionModuleStaticFileBuilderContext ctx, String entityName)
            throws UnifyException {
        return true;
    }

    protected void openEntry(ExtensionModuleStaticFileBuilderContext ctx, String filename, ZipOutputStream zos)
            throws UnifyException {
        try {
            final String dir = ctx.isSnapshotMode() ? snapshotZipDir : zipDir;
            zos.putNextEntry(new ZipEntry(StringUtils.isBlank(dir) ? filename : dir + filename));
        } catch (IOException e) {
            throwOperationErrorException(e);
        }
    }

    protected void closeEntry(ZipOutputStream zos) throws UnifyException {
        try {
            zos.flush();
            zos.closeEntry();
        } catch (IOException e) {
            throwOperationErrorException(e);
        }
    }

    protected String getDescriptionKey(String applicationName, String category, String description) {
        return (applicationName + "." + category + "." + description.replaceAll("[^a-zA-Z0-9]+", "").trim())
                .toLowerCase();
    }
}
