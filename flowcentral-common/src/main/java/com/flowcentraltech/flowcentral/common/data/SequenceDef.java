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

package com.flowcentraltech.flowcentral.common.data;

import java.util.List;

/**
 * Sequence definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SequenceDef {

    private final String skeleton;

    private final List<SequencePartDef> partList;

    private final boolean withDatePart;

    private final boolean withDayPart;

    public SequenceDef(String skeleton, List<SequencePartDef> partList, boolean withDatePart, boolean withDayPart) {
        this.skeleton = skeleton;
        this.partList = partList;
        this.withDatePart = withDatePart;
        this.withDayPart = withDayPart;
    }

    public String getSkeleton() {
        return skeleton;
    }

    public List<SequencePartDef> getPartList() {
        return partList;
    }

    public boolean isWithDatePart() {
        return withDatePart;
    }

    public boolean isWithDayPart() {
        return withDayPart;
    }

}
