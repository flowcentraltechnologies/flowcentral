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
package com.flowcentraltech.flowcentral.application.data;

import java.util.List;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.data.ButtonInfo;

/**
 * Table loading definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableLoadingDef implements Listable {

    private String name;

    private String description;

    private String label;

    private String provider;

    private Object parameter;

    private int orderIndex;
    
    private List<ButtonInfo> actionBtnInfos;
    
    public TableLoadingDef(String name, String description, String label, String provider, int orderIndex, List<ButtonInfo> actionBtnInfos) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.provider = provider;
        this.orderIndex = orderIndex;
        this.actionBtnInfos = actionBtnInfos;
    }

    public TableLoadingDef(String name, String description, String label, String provider, Object parameter,
            int orderIndex, List<ButtonInfo> actionBtnInfos) {
         this.name = name;
        this.description = description;
        this.label = label;
        this.provider = provider;
        this.parameter = parameter;
        this.orderIndex = orderIndex;
        this.actionBtnInfos = actionBtnInfos;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Object getParameter() {
        return parameter;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getProvider() {
        return provider;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public List<ButtonInfo> getActionBtnInfos() {
        return actionBtnInfos;
    }

    public boolean isWithActionBtnInfos() {
        return !DataUtils.isBlank(actionBtnInfos);
    }

}
