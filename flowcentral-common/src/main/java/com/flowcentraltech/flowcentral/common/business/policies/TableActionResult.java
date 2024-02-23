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
package com.flowcentraltech.flowcentral.common.business.policies;

/**
 * Table action result
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableActionResult {

    private Object result;
    
    private boolean refreshContent;
    
    private boolean openTab;
    
    private boolean openPath;
    
    private boolean displayListingReport;

    public TableActionResult(Object result) {
        this.result = result;
    }

    public TableActionResult() {
        
    }

    public Object getResult() {
        return result;
    }

    public boolean isRefreshContent() {
        return refreshContent;
    }

    public void setRefreshContent(boolean refreshContent) {
        this.refreshContent = refreshContent;
    }

    public boolean isOpenTab() {
        return openTab;
    }

    public void setOpenTab(boolean openTab) {
        this.openTab = openTab;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isOpenPath() {
        return openPath;
    }

    public void setOpenPath(boolean openPath) {
        this.openPath = openPath;
    }

    public boolean isDisplayListingReport() {
        return displayListingReport;
    }

    public void setDisplayListingReport(boolean displayListingReport) {
        this.displayListingReport = displayListingReport;
    }

}
