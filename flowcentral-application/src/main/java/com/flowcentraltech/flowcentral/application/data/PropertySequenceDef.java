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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application property sequence definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertySequenceDef {

    private List<PropertySequenceEntryDef> sequenceList;

    private String name;

    private String description;

    private PropertySequenceDef(List<PropertySequenceEntryDef> sequenceList, String name, String description) {
        this.sequenceList = sequenceList;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PropertySequenceEntryDef> getSequenceList() {
        return sequenceList;
    }

    public boolean isBlank() {
        return sequenceList == null || sequenceList.isEmpty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String name;

        private String description;

        private List<PropertySequenceEntryDef> sequenceList;

        public Builder() {
            sequenceList = new ArrayList<PropertySequenceEntryDef>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addSequenceEntryDef(String property, String label) {
            sequenceList.add(new PropertySequenceEntryDef(property, label));
            return this;
        }

        public PropertySequenceDef build() throws UnifyException {
            return new PropertySequenceDef(DataUtils.unmodifiableList(sequenceList), name, description);
        }
    }
}
