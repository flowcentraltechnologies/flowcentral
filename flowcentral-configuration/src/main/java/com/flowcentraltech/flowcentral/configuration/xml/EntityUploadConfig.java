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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.batch.ConstraintAction;
import com.tcdng.unify.core.util.xml.adapter.ConstraintActionXmlAdapter;

/**
 * Entity upload configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityUploadConfig extends BaseNameConfig {

    private ConstraintAction constraintAction;

    private FieldSequenceConfig fieldSequence;

    public EntityUploadConfig() {
        this.constraintAction = ConstraintAction.FAIL;
    }

    public ConstraintAction getConstraintAction() {
        return constraintAction;
    }

    @XmlJavaTypeAdapter(ConstraintActionXmlAdapter.class)
    @XmlAttribute(name = "onConstraint")
    public void setConstraintAction(ConstraintAction constraintAction) {
        this.constraintAction = constraintAction;
    }

    public FieldSequenceConfig getFieldSequence() {
        return fieldSequence;
    }

    @XmlElement(required = true)
    public void setFieldSequence(FieldSequenceConfig fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

}
