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

import java.util.HashSet;
import java.util.Set;

import com.flowcentraltech.flowcentral.codegeneration.data.SnapshotMeta;

/**
 * Extension static file builder context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ExtensionStaticFileBuilderContext {

    private Set<String> zipDirs;

    private SnapshotMeta snapshotMeta;

    private String basePackage;

    private final boolean snapshotMode;

    public ExtensionStaticFileBuilderContext(SnapshotMeta snapshotMeta, String basePackage, boolean snapshotMode) {
        this.zipDirs = new HashSet<String>();
        this.snapshotMeta = snapshotMeta;
        this.basePackage = basePackage;
        this.snapshotMode = snapshotMode;
    }

    public ExtensionStaticFileBuilderContext(String basePackage, boolean snapshotMode) {
        this.zipDirs = new HashSet<String>();
        this.basePackage = basePackage;
        this.snapshotMode = snapshotMode;
    }

    public boolean isSnapshotMode() {
        return snapshotMode;
    }

    public boolean addZipDir(String zipDir) {
        return zipDirs.add(zipDir);
    }

    public SnapshotMeta getSnapshotMeta() {
        return snapshotMeta;
    }

    public String getBasePackage() {
        return basePackage;
    }

}
