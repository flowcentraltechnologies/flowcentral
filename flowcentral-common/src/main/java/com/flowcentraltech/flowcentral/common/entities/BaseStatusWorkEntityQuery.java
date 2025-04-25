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
package com.flowcentraltech.flowcentral.common.entities;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;

/**
 * Base query object for base work entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseStatusWorkEntityQuery<T extends BaseStatusWorkEntity> extends BaseWorkEntityQuery<T> {

    public BaseStatusWorkEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseStatusWorkEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public final BaseStatusWorkEntityQuery<T> status(RecordStatus status) {
        return (BaseStatusWorkEntityQuery<T>) addEquals("status", status);
    }
}
