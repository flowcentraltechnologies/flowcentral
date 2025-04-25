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
 * Code generation settings page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class CodeGenerationSettingsPageBean extends AbstractPageBean {

    private String packageBase;

    private String repository;

    private String extensionSrc;

    private String utilitiesSrc;

    private String branch;

    private boolean generateToRepo;

    public String getPackageBase() {
        return packageBase;
    }

    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getExtensionSrc() {
        return extensionSrc;
    }

    public void setExtensionSrc(String extensionSrc) {
        this.extensionSrc = extensionSrc;
    }

    public String getUtilitiesSrc() {
        return utilitiesSrc;
    }

    public void setUtilitiesSrc(String utilitiesSrc) {
        this.utilitiesSrc = utilitiesSrc;
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
