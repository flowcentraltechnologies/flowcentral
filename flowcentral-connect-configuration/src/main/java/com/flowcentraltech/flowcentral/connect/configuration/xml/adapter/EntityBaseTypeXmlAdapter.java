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
package com.flowcentraltech.flowcentral.connect.configuration.xml.adapter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectEntityBaseType;

/**
 * Interconnect entity base type XML adapter.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityBaseTypeXmlAdapter {

    public static class Serializer extends JsonSerializer<ConnectEntityBaseType> {

        @Override
        public void serialize(ConnectEntityBaseType value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value != null ? value.name() : null);
        }
        
    }

    public static class Deserializer extends JsonDeserializer<ConnectEntityBaseType> {

        @Override
        public ConnectEntityBaseType deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            String str = p.getText();
            return str != null ? ConnectEntityBaseType.valueOf(str.toUpperCase()) : null;
        }
        
    }

}
