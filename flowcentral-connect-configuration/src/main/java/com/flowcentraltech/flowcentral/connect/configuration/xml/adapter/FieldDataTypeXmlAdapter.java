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
package com.flowcentraltech.flowcentral.connect.configuration.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectFieldDataType;

/**
 * Interconnect field data type XML adapter.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FieldDataTypeXmlAdapter extends XmlAdapter<String, ConnectFieldDataType> {

    @Override
    public ConnectFieldDataType unmarshal(String str) throws Exception {
        return str != null ? ConnectFieldDataType.valueOf(str.toUpperCase()) : null;
    }

    @Override
    public String marshal(ConnectFieldDataType type) throws Exception {
        return type != null ? type.name() : null;
    }

}
