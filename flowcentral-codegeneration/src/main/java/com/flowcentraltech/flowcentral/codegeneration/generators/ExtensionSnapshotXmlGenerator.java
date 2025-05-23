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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.codegeneration.data.SnapshotMeta;
import com.flowcentraltech.flowcentral.configuration.xml.SnapshotConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Extension snapshot XML generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("extension-snapshot-xml-generator")
public class ExtensionSnapshotXmlGenerator extends AbstractStaticArtifactGenerator {

    @Override
    protected void doGenerate(ExtensionStaticFileBuilderContext ctx, ZipOutputStream zos) throws UnifyException {
        final String filename = "snapshot.xml";
        openEntry(ctx, filename, zos);

        final SnapshotConfig snapshotConfig = new SnapshotConfig();
        snapshotConfig.setApplicationCode(getApplicationCode());
        snapshotConfig.setApplicationName(getApplicationName());
        snapshotConfig.setApplicationVersion(getDeploymentVersion());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        snapshotConfig.setSnapshotTimestamp(sdf.format(new Date()));
        snapshotConfig.setSnapshotVersion("1.0.0");
        snapshotConfig.setSnapshotTakenBy(getUserToken() != null ? getUserToken().getUserLoginId() : null);
        SnapshotMeta meta = ctx.getSnapshotMeta();
        snapshotConfig.setSnapshotType(meta.getType());
        snapshotConfig.setSnapshotTitle(meta.getTitle());
        snapshotConfig.setSnapshotMessage(meta.getMessage());
        ConfigurationUtils.writeConfig(snapshotConfig, zos);
        closeEntry(zos);
    }
}
