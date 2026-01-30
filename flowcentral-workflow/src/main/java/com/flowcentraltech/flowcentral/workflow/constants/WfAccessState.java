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

package com.flowcentraltech.flowcentral.workflow.constants;

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Workflow access state.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum WfAccessState implements EnumConst {

    START("S"),
    USER_ACTION("U"),
    END("E");

    private final String code;

    private WfAccessState(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return START.code;
    }

    public boolean isStart() {
        return START.equals(this);
    }

    public boolean isUserAction() {
        return USER_ACTION.equals(this);
    }

    public boolean isEnd() {
        return END.equals(this);
    }
    
    public static WfAccessState fromCode(String code) {
        return EnumUtils.fromCode(WfAccessState.class, code);
    }

    public static WfAccessState fromName(String name) {
        return EnumUtils.fromName(WfAccessState.class, name);
    }

}
