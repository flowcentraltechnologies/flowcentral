/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.chart.data;

import com.tcdng.unify.core.criterion.Restriction;

/**
 * Chart view option object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartViewOption {

    public static final ChartViewOption DEFAULT = new ChartViewOption("default");

    private String name;

    private Restriction restriction;

    public ChartViewOption(String name, Restriction restriction) {
        this.name = name;
        this.restriction = restriction;
    }

    private ChartViewOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public boolean isWithRestriction() {
        return restriction != null;
    }

}
