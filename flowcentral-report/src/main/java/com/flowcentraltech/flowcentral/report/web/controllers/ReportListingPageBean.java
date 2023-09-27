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
package com.flowcentraltech.flowcentral.report.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.report.data.ReportGroupInfo;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Report listing page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReportListingPageBean extends AbstractPageBean {

    private List<ReportGroupInfo> groupList;

    public List<ReportGroupInfo> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ReportGroupInfo> groupList) {
        this.groupList = groupList;
    }

}
