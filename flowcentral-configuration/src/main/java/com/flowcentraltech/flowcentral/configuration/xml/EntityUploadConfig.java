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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tcdng.unify.core.batch.ConstraintAction;
import com.tcdng.unify.core.util.xml.adapter.ConstraintActionXmlAdapter;

/**
 * Entity upload configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class EntityUploadConfig extends BaseNameConfig {

    @JsonSerialize(using = ConstraintActionXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ConstraintActionXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "onConstraint")
    private ConstraintAction constraintAction;

    @JacksonXmlProperty
    private FieldSequenceConfig fieldSequence;

    public EntityUploadConfig() {
        this.constraintAction = ConstraintAction.FAIL;
    }

    public ConstraintAction getConstraintAction() {
        return constraintAction;
    }

    public void setConstraintAction(ConstraintAction constraintAction) {
        this.constraintAction = constraintAction;
    }

    public FieldSequenceConfig getFieldSequence() {
        return fieldSequence;
    }

    public void setFieldSequence(FieldSequenceConfig fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

}
