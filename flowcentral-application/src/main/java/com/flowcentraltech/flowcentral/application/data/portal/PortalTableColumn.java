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
package com.flowcentraltech.flowcentral.application.data.portal;

/**
 * Portal table column object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PortalTableColumn {

    private String field;

    private String label;
    
    private String order;
    
    private String linkAct;
    
    private int widthRatio;

    public PortalTableColumn(String field, String label, String order, String linkAct, int widthRatio) {
        this.field = field;
        this.label = label;
        this.order = order;
        this.linkAct = linkAct;
        this.widthRatio = widthRatio;
    }

    public String getField() {
        return field;
    }

    public String getLabel() {
        return label;
    }

    public String getOrder() {
        return order;
    }

    public String getLinkAct() {
        return linkAct;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

}
