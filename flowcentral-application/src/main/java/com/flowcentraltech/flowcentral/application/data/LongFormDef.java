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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Long form definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LongFormDef {

    private String appletName;
    
    private List<LongFormSectionDef> sections;
    
    private long version;

    private LongFormDef(String appletName, List<LongFormSectionDef> sections, long version) {
        this.appletName = appletName;
        this.sections = sections;
        this.version = version;
    }

    public String getAppletName() {
        return appletName;
    }

    public List<LongFormSectionDef> getSections() {
        return sections;
    }

    public long getVersion() {
        return version;
    }

    public static Builder newBuilder(String appletName, long version) {
        return new Builder(appletName, version);
    }

    public static class Builder {

        private String appletName;
        
        private List<LongFormSectionDef> sections;
        
        private long version;

        public Builder(String appletName, long version) {
            this.appletName = appletName;
            this.version = version;
            sections = new ArrayList<LongFormSectionDef>();
        }

        public Builder addSectionDef(String appletName, String sectionLabel, String secRenderer) {
            sections.add(new LongFormSectionDef(appletName, sectionLabel, secRenderer));
            return this;
        }

        public LongFormDef build() {
            return new LongFormDef(appletName, Collections.unmodifiableList(sections), version);
        }
    }

}
