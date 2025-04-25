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
package com.flowcentraltech.flowcentral.connect.common.data;

import java.util.List;

import com.tcdng.unify.common.data.RedirectErrorDTO;

/**
 * Flow central interconnect base broadcast response class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseBroadcastResponse extends BaseResponse {

    private List<RedirectErrorDTO> redirectErrors;

    public BaseBroadcastResponse(String errorCode, String errorMsg, List<RedirectErrorDTO> redirectErrors) {
        super(errorCode, errorMsg);
        this.redirectErrors = redirectErrors;
    }

    public BaseBroadcastResponse(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public BaseBroadcastResponse() {

    }

    public List<RedirectErrorDTO> getRedirectErrors() {
        return redirectErrors;
    }

    public void setRedirectErrors(List<RedirectErrorDTO> redirectErrors) {
        this.redirectErrors = redirectErrors;
    }

}
