/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
 * Form listing options
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormListingOptions {

    private final String formActionName;
    
    private final int optionFlags;

    private final boolean important;
    
    private final FormListing formListing;
    
    public FormListingOptions() {
        this.formActionName = null;
        this.optionFlags = ~0;
        this.important = false;
        this.formListing = null;
    }
    
    public FormListingOptions(FormListing formListing) {
        this.formActionName = null;
        this.optionFlags = ~0;
        this.important = false;
        this.formListing = formListing;
    }

    public FormListingOptions(String formActionName) {
        this.formActionName = formActionName;
        this.optionFlags = ~0;
        this.important = false;
        this.formListing = null;
    }

    public FormListingOptions(int optionFlags) {
        this.formActionName = null;
        this.optionFlags = optionFlags;
        this.important = true;
        this.formListing = null;
    }

    public FormListingOptions(String formActionName, int optionFlags) {
        this.formActionName = formActionName;
        this.optionFlags = optionFlags;
        this.important = true;
        this.formListing = null;
    }

    public String getFormActionName() {
        return formActionName;
    }

    public int getOptionFlags() {
        return optionFlags;
    }
    
    public boolean isOption(int option) {
        return (optionFlags & option) ==  option; 
    }

    public boolean isImportant() {
        return important;
    }

    public FormListing getFormListing() {
        return formListing;
    }

}
