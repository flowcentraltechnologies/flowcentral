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

package com.flowcentraltech.flowcentral.codegeneration.web.controllers;

import com.flowcentraltech.flowcentral.codegeneration.data.CodeGenerationItem;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Extension module static files generation page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ExtensionModuleStaticFilesGenerationPageBean extends AbstractPageBean {
    
    private CodeGenerationItem codeGenerationItem;

    private Long moduleId;

    private String basePackage;

    public CodeGenerationItem getCodeGenerationItem() {
        return codeGenerationItem;
    }

    public void setCodeGenerationItem(CodeGenerationItem codeGenerationItem) {
        this.codeGenerationItem = codeGenerationItem;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
}
