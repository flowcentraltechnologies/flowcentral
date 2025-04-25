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
package com.flowcentraltech.flowcentral.integration.endpoint.data;

import java.util.Date;
import java.util.List;

/**
 * Read event instance.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class JsonReadEvent<T> {

    private Long id;

    private Date createDt;
    
    private List<T> itemList;

    public JsonReadEvent(Long id, Date createDt, List<T> itemList) {
        this.id = id;
        this.createDt = createDt;
        this.itemList = itemList;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public List<T> getItemList() {
        return itemList;
    }
}
