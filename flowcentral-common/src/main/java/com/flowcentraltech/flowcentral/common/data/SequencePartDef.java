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

package com.flowcentraltech.flowcentral.common.data;

import com.flowcentraltech.flowcentral.common.constants.SequencePartType;

/**
 * Sequence part definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SequencePartDef {

    private final SequencePartType type;

    private final String text;

    private int numLen;

    public SequencePartDef(SequencePartType type) {
        this.type = type;
        this.text = null;
        this.numLen = 0;
    }

    public SequencePartDef(String text) {
        this.type = SequencePartType.TEXT;
        this.text = text;
        this.numLen = 0;
    }

    public SequencePartDef(SequencePartType type, int numLen) {
        this.type = type;
        this.text = null;
        this.numLen = numLen;
    }

    public SequencePartDef(SequencePartType type, String code) {
        this.type = type;
        this.text = code;
    }

    public SequencePartType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getCode() {
        return text;
    }

    public int getNumLen() {
        return numLen;
    }

    @Override
    public String toString() {
        return "SequencePartDef [type=" + type + ", text=" + text + ", numLen=" + numLen + "]";
    }
}
