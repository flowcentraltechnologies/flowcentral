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

package com.flowcentraltech.flowcentral.codegeneration.web.controllers;

import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Snapshot settings page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SnapshotSettingsPageBean extends AbstractPageBean {

    private String repository;

    private String snapshotSrc;

    private String branch;

    private boolean generateToRepo;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getSnapshotSrc() {
        return snapshotSrc;
    }

    public void setSnapshotSrc(String snapshotSrc) {
        this.snapshotSrc = snapshotSrc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public boolean isGenerateToRepo() {
        return generateToRepo;
    }

    public void setGenerateToRepo(boolean generateToRepo) {
        this.generateToRepo = generateToRepo;
    }
    
}
