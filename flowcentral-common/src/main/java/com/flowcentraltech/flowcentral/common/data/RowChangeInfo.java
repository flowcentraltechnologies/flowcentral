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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Row change information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RowChangeInfo {
    
    private String trigger;
    
    private int rowIndex;

    public RowChangeInfo(String trigger, int rowIndex) {
        this.trigger = trigger;
        this.rowIndex = rowIndex;
    }

    public RowChangeInfo(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String swapTrigger(String trigger) {
        String _trigger = this.trigger;
        this.trigger = trigger;
        return _trigger;
    }
    
    public String getTrigger() {
        return trigger;
    }

    public int getRowIndex() {
        return rowIndex;
    }
    
    public boolean matchRowIndex(int rowIndex) {
        return this.rowIndex == rowIndex;
    }
    
    public boolean matchTrigger(String trigger) {
        return trigger != null && trigger.equals(this.trigger);
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }
}