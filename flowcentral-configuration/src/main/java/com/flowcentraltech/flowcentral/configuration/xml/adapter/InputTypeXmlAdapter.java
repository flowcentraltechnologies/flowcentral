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
package com.flowcentraltech.flowcentral.configuration.xml.adapter;

import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.util.xml.AbstractEnumConstDeserializer;
import com.tcdng.unify.core.util.xml.AbstractEnumConstSerializer;
import com.tcdng.unify.core.util.xml.AbstractEnumConstXmlAdapter;

/**
 * Input type XML adapter.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class InputTypeXmlAdapter extends AbstractEnumConstXmlAdapter {
    
    public static class Serializer extends AbstractEnumConstSerializer<InputType> {
        public Serializer() {
            
        }

    }
    
    public static class Deserializer extends AbstractEnumConstDeserializer<InputType> {

        public Deserializer() {
            super(InputType.class);
        }

    }
}
