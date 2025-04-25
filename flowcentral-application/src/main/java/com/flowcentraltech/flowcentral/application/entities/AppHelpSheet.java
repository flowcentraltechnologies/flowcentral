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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;

/**
 * Application help sheet entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_HELPSHEET")
public class AppHelpSheet extends BaseApplicationEntity {

    @Column(length = 128)
    private String entity;

    @Column(length = 128)
    private String label;

    @Column(name = "HELP_OVERVIEW", type = ColumnType.CLOB, nullable = true)
    private String helpOverview;

    @ChildList
    private List<AppHelpEntry> entryList;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHelpOverview() {
        return helpOverview;
    }

    public void setHelpOverview(String helpOverview) {
        this.helpOverview = helpOverview;
    }

    public List<AppHelpEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<AppHelpEntry> entryList) {
        this.entryList = entryList;
    }

}
