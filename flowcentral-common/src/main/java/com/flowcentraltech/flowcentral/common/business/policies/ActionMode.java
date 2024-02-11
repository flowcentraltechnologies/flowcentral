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

package com.flowcentraltech.flowcentral.common.business.policies;

/**
 * User action mode..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum ActionMode {
    ACTION_ONLY,
    ACTION_AND_NEXT,
    ACTION_AND_CLOSE,
    DELETE_AND_CLOSE;

    public boolean isWithNext() {
        return ACTION_AND_NEXT.equals(this);
    }

    public boolean isWithClose() {
        return ACTION_AND_CLOSE.equals(this) || DELETE_AND_CLOSE.equals(this);
    }

    public boolean isDelete() {
        return DELETE_AND_CLOSE.equals(this);
    }
}