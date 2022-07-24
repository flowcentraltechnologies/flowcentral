/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Review result.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReviewResult {

    private List<String> skippableMessages;

    private List<String> requiredMessages;

    private ReviewResult(List<String> skippableMessages, List<String> requiredMessages) {
        this.skippableMessages = skippableMessages;
        this.requiredMessages = requiredMessages;
    }

    public List<String> getSkippableMessages() {
        return skippableMessages;
    }

    public List<String> getRequiredMessages() {
        return requiredMessages;
    }

    public boolean isWithSkippableMessages() {
        return !skippableMessages.isEmpty();
    }

    public boolean isSkippableOnly() {
        return !skippableMessages.isEmpty() && requiredMessages.isEmpty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<String> skippableMessages;

        private List<String> requiredMessages;

        public Builder() {
            this.skippableMessages = new ArrayList<String>();
            this.requiredMessages = new ArrayList<String>();
        }

        public Builder addSkippable(String message) {
            skippableMessages.add(message);
            return this;
        }

        public Builder addRequired(String message) {
            requiredMessages.add(message);
            return this;
        }

        public ReviewResult build() {
            return new ReviewResult(DataUtils.unmodifiableList(skippableMessages),
                    DataUtils.unmodifiableList(requiredMessages));
        }
    }
}
