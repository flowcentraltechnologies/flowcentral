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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Help sheet definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class HelpSheetDef extends BaseApplicationEntityDef {

    private String label;

    private String helpOverview;

    private Map<String, HelpEntryDef> entries;

    private HelpSheetDef(ApplicationEntityNameParts nameParts, String description, Long id, long version, String label,
            String helpOverview, Map<String, HelpEntryDef> entries) {
        super(nameParts, description, id, version);
        this.label = label;
        this.helpOverview = helpOverview;
        this.entries = entries;
    }

    public String getLabel() {
        return label;
    }

    public String getHelpOverview() {
        return helpOverview;
    }

    public boolean isWithHelpOverview() {
        return !StringUtils.isBlank(helpOverview);
    }
    
    public Map<String, HelpEntryDef> getEntries() {
        return entries;
    }
    
    public boolean isWithHelpEntry(String fieldName) {
        return entries.containsKey(fieldName);
    }

    public HelpEntryDef getHelpEntryDef(String fieldName) {
        return entries.get(fieldName);
    }

    public static Builder newBuilder(String label, String helpOverview,  String longName,
            String description, Long id, long version) {
        return new Builder(label, helpOverview, longName, description, id, version);
    }

    public static class Builder {

        private String label;

        private String helpOverview;

        private Map<String, HelpEntryDef> entries;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(String label, String helpOverview, String longName, String description, Long id, long version) {
            this.label = label;
            this.helpOverview = helpOverview;
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            this.entries = new HashMap<String, HelpEntryDef>();
        }

        public Builder addEntry(String fieldName, String helpContent) {
            entries.put(fieldName, new HelpEntryDef(fieldName, helpContent));
            return this;
        }

        public HelpSheetDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new HelpSheetDef(nameParts, description, id, version, label,
                    helpOverview, Collections.unmodifiableMap(entries));
        }
    }
}
