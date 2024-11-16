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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.data.FormMessage;

/**
 * Form wizard.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWizard {

    private String formName;
    
    private List<MiniForm> forms;

    private FormContext formContext;

    private int currentPage;
    
    private final int pageCount;
    
    public FormWizard(String formName, List<MiniForm> forms, FormContext formContext) {
        this.formName = formName;
        this.forms = forms;
        this.formContext = formContext;
        this.pageCount = forms.size();
    }

    public List<FormMessage> getMessages() {
        return formContext.getValidationErrors();
    }

    public String getFormName() {
        return formName;
    }

    public FormContext getFormContext() {
        return formContext;
    }

    public MiniForm getForm() {
        return forms.get(currentPage);
    }
    
    public int getPageCount() {
        return pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }
    
    public boolean isFirstPage() {
        return currentPage == 0;
    }
    
    public boolean isLastPage() {
        return currentPage == (pageCount - 1);
    }
    
    public boolean prevPage() {
        if (currentPage > 0) {
            currentPage--;
            return true;
        }
        
        return false;
    }
    
    public boolean nextPage() {
        if (currentPage < (pageCount - 1)) {
            currentPage++;
            return true;
        }
        
        return false;
    }
}
