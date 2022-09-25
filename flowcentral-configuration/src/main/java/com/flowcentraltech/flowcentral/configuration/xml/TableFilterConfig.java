/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Table filter configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableFilterConfig extends FilterConfig {

    private String rowColor;

    private String legendLabel;

    public String getRowColor() {
        return rowColor;
    }

    @XmlAttribute
    public void setRowColor(String rowColor) {
        this.rowColor = rowColor;
    }

    public String getLegendLabel() {
        return legendLabel;
    }

    @XmlAttribute
    public void setLegendLabel(String legendLabel) {
        this.legendLabel = legendLabel;
    }

}
