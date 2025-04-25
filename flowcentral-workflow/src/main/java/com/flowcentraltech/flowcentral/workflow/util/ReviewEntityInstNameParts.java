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

package com.flowcentraltech.flowcentral.workflow.util;

import com.flowcentraltech.flowcentral.application.data.EntityInstNameParts;

/**
 * Review entity instance name parts.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReviewEntityInstNameParts {

    private String reviewEntityInstName;

    private EntityInstNameParts reviewInstNameParts;

    private EntityInstNameParts instNameParts;

    public ReviewEntityInstNameParts(String reviewEntityInstName, EntityInstNameParts reviewInstNameParts,
            EntityInstNameParts instNameParts) {
        this.reviewEntityInstName = reviewEntityInstName;
        this.reviewInstNameParts = reviewInstNameParts;
        this.instNameParts = instNameParts;
    }

    public String getReviewEntityInstName() {
        return reviewEntityInstName;
    }

    public EntityInstNameParts getReviewInstNameParts() {
        return reviewInstNameParts;
    }

    public EntityInstNameParts getInstNameParts() {
        return instNameParts;
    }

}
