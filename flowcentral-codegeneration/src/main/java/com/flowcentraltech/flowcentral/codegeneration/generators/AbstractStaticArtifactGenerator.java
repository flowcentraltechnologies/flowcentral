/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for static artifact generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Singleton(false)
public abstract class AbstractStaticArtifactGenerator extends AbstractFlowCentralComponent
        implements StaticArtifactGenerator {

    @Configurable
    private AppletUtilities appletUtilities;

    private String zipDir;

    private String snapshotZipDir;

    protected AbstractStaticArtifactGenerator(String zipDir, String snapshotZipDir) {
        this.zipDir = zipDir;
        this.snapshotZipDir = snapshotZipDir;
    }

    protected AbstractStaticArtifactGenerator(String zipDir) {
        this.zipDir = zipDir;
    }

    protected AbstractStaticArtifactGenerator() {

    }

    @Override
    public final void generate(ExtensionStaticFileBuilderContext ctx, ZipOutputStream zos) throws UnifyException {
        if (ctx.isSnapshotMode()) {
            if (!StringUtils.isBlank(snapshotZipDir)) {
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
                if (ctx.addZipDir(zipDir)) {
                    try {
                        zos.putNextEntry(new ZipEntry(zipDir));
                    } catch (IOException e) {
                        throwOperationErrorException(e);
                    }
                }
            }
        }

        doGenerate(ctx, zos);
    }

    protected AppletUtilities au() {
        return appletUtilities;
    }

    protected abstract void doGenerate(ExtensionStaticFileBuilderContext ctx, ZipOutputStream zos)
            throws UnifyException;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected boolean checkGeneration(ExtensionStaticFileBuilderContext ctx, String entityName) throws UnifyException {
        return true;
    }

    protected void openEntry(ExtensionStaticFileBuilderContext ctx, String filename, ZipOutputStream zos)
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
