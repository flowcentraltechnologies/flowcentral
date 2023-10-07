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
package com.flowcentraltech.flowcentral.delegate.data;

import java.util.List;

import com.flowcentraltech.flowcentral.connect.common.data.EntityListingDTO;
import com.flowcentraltech.flowcentral.connect.common.data.RedirectErrorDTO;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Delegate listing DTO
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DelegateEntityListingDTO {

    private List<EntityListingDTO> listings;
    
    private List<RedirectErrorDTO> redirectErrors;

    public DelegateEntityListingDTO(List<EntityListingDTO> listings, List<RedirectErrorDTO> redirectErrors) {
        this.listings = listings;
        this.redirectErrors = redirectErrors;
    }

    public List<EntityListingDTO> getListings() {
        return listings;
    }

    public List<RedirectErrorDTO> getRedirectErrors() {
        return redirectErrors;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }
}
