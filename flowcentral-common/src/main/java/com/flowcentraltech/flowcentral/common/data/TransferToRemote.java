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
package com.flowcentraltech.flowcentral.common.data;

/**
 * Transfer remote.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TransferToRemote {

    public enum TransferType {
        REPLACE_FILE_IN_DIRECTORY,
        REPLACE_DIRECTORY_WITH_ZIP
    }

    private TransferType type;

    private String remoteName;

    private String remoteBranch;

    private String fileName;

    private String workingPath;

    private byte[] file;

    public TransferToRemote(TransferType type, String remoteName, String remoteBranch, String workingPath,
            String fileName, byte[] file) {
        this.type = type;
        this.remoteName = remoteName;
        this.remoteBranch = remoteBranch;
        this.workingPath = workingPath;
        this.fileName = fileName;
        this.file = file;
    }

    public TransferType getType() {
        return type;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public String getRemoteBranch() {
        return remoteBranch;
    }

    public String getWorkingPath() {
        return workingPath;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFile() {
        return file;
    }
}
