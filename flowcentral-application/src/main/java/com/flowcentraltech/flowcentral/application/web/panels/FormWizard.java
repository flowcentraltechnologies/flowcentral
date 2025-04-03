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
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.IconBar;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form wizard.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWizard extends AbstractForm {

    private IconBar iconBar;
    
    private String title;
    
    private String formName;
    
    private String navPolicy;
    
    private List<MiniForm> forms;
    
    private String submitCaption;
    
    private String submitStyleClass;

    private boolean submit;
    
    private String execCaption;
    
    private String execStyleClass;
    
    private String execProcessor;

    private boolean execute;

    private int currentPage;
    
    private final int pageCount;
    
    public FormWizard(String formName, String navPolicy, IconBar iconBar, List<MiniForm> forms, SectorIcon sectorIcon,
            BreadCrumbs breadCrumbs) {
        super(forms.get(0).getCtx(), sectorIcon, breadCrumbs);
        this.formName = formName;
        this.navPolicy = navPolicy;
        this.iconBar = iconBar;
        this.forms = forms;
        this.pageCount = forms.size();
    }

    public List<FormMessage> getMessages() {
        return getFormContext().getValidationErrors();
    }

    public String getFormName() {
        return formName;
    }

    public String getNavPolicy() {
        return navPolicy;
    }

    public boolean isWithNavPolicy() {
        return !StringUtils.isBlank(navPolicy);
    }
    
    public String getSubmitCaption() {
        return submitCaption;
    }

    public void setSubmitCaption(String submitCaption) {
        this.submitCaption = submitCaption;
    }

    public String getSubmitStyleClass() {
        return submitStyleClass;
    }

    public void setSubmitStyleClass(String submitStyleClass) {
        this.submitStyleClass = submitStyleClass;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public String getExecCaption() {
        return execCaption;
    }

    public void setExecCaption(String execCaption) {
        this.execCaption = execCaption;
    }

    public String getExecStyleClass() {
        return execStyleClass;
    }

    public void setExecStyleClass(String execStyleClass) {
        this.execStyleClass = execStyleClass;
    }

    public String getExecProcessor() {
        return execProcessor;
    }

    public void setExecProcessor(String execProcessor) {
        this.execProcessor = execProcessor;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public FormContext getFormContext() {
        return forms.get(currentPage).getCtx();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IconBar getIconBar() {
        return iconBar;
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
            iconBar.setSelected(currentPage);
            return true;
        }
        
        return false;
    }
    
    public boolean nextPage() {
        if (currentPage < (pageCount - 1)) {
            currentPage++;
            iconBar.setSelected(currentPage);
            return true;
        }
        
        return false;
    }
}
