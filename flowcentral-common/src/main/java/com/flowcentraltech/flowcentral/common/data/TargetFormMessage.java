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

package com.flowcentraltech.flowcentral.common.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Target for message.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TargetFormMessage {

    private Set<String> targets;

    private FormMessage formMessage;

    private boolean skippable;
    
    public TargetFormMessage(Set<String> targets, FormMessage formMessage, boolean skippable) {
        this.targets = targets;
        this.formMessage = formMessage;
        this.skippable = skippable;
    }

    public TargetFormMessage(FormMessage formMessage, boolean skippable, String... targets) {
        this.targets = new HashSet<String>(Arrays.asList(targets));
        this.formMessage = formMessage;
        this.skippable = skippable;
    }

    public Set<String> getTargets() {
        return targets;
    }

    public boolean isWithTargets() {
        return targets != null && !targets.isEmpty();
    }

    public boolean isTarget(String target) {
        return targets.contains(target);
    }

    public boolean isSkippable() {
        return skippable;
    }

    public FormMessage getFormMessage() {
        return formMessage;
    }

    @Override
    public String toString() {
        return "TargetFormMessage [targets=" + targets + ", formMessage=" + formMessage + "]";
    }
}
