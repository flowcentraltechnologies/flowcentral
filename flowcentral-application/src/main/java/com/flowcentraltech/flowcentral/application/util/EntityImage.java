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
package com.flowcentraltech.flowcentral.application.util;

/**
 * Entity image.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityImage {

    public static final EntityImage BLANK = new EntityImage();
    
    private String entity;
    
    private String fieldName;
    
    private Long instId;

    public EntityImage(String entity, Long instId, String fieldName) {
        this.entity = entity;
        this.fieldName = fieldName;
        this.instId = instId;
    }

    private EntityImage() {
        
    }

    public String getEntity() {
        return entity;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getInstId() {
        return instId;
    }
    
    public boolean isNullInstance() {
        return entity != null && fieldName != null && instId == null;
    }
    
    public boolean isPresent() {
        return entity != null && fieldName != null && instId != null;
    }

}
