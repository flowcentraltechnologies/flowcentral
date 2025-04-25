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

package com.flowcentraltech.flowcentral.codegeneration.data;

/**
 * Code generation item.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CodeGenerationItem {

    private SnapshotMeta snapshotMeta;

    private String basePackage;

    private String filename;

    private byte[] data;

    public CodeGenerationItem(SnapshotMeta snapshotMeta, String basePackage) {
        this.snapshotMeta = snapshotMeta;
        this.basePackage = basePackage;
    }

    public CodeGenerationItem(String basePackage) {
        this.basePackage = basePackage;
    }

    public SnapshotMeta getSnapshotMeta() {
        return snapshotMeta;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
