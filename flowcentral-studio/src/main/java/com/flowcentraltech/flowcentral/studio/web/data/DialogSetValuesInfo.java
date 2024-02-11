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

package com.flowcentraltech.flowcentral.studio.web.data;

import com.flowcentraltech.flowcentral.application.web.widgets.SetValueEntries;

/**
 * Dialog set values info.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DialogSetValuesInfo {

    private String title;

    private SetValueEntries entries;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SetValueEntries getEntries() {
        return entries;
    }

    public void setEntries(SetValueEntries entries) {
        this.entries = entries;
    }

}
