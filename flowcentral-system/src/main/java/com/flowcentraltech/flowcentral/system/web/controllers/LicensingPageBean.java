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

package com.flowcentraltech.flowcentral.system.web.controllers;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.system.data.LicenseDef;
import com.flowcentraltech.flowcentral.system.data.LicenseEntryDef;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Licensing page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LicensingPageBean extends AbstractPageBean {

    private LicenseDef licenseDef;

    private String formClientTitle;

    private String formAccountNo;
    
    private byte[] licenseFile;

    public LicenseDef getLicenseDef() {
        return licenseDef;
    }

    public void setLicenseDef(LicenseDef licenseDef) {
        this.licenseDef = licenseDef;
    }

    public String getType() {
        return licenseDef != null? licenseDef.getType() : null;
    }

    public String getClientTitle() {
        return licenseDef != null? licenseDef.getClientTitle() : null;
    }

    public List<LicenseEntryDef> getEntryList() {
        return licenseDef != null? licenseDef.getEntryList() : Collections.emptyList();
    }

    public String getFormClientTitle() {
        return formClientTitle;
    }

    public void setFormClientTitle(String formClientTitle) {
        this.formClientTitle = formClientTitle;
    }

    public String getFormAccountNo() {
        return formAccountNo;
    }

    public void setFormAccountNo(String formAccountNo) {
        this.formAccountNo = formAccountNo;
    }

    public byte[] getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(byte[] licenseFile) {
        this.licenseFile = licenseFile;
    }
    
}
