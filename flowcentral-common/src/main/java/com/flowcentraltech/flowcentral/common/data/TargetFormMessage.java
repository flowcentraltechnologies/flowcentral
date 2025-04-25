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

package com.flowcentraltech.flowcentral.common.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Target for message.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TargetFormMessage {

    public enum TargetType {
        FIELD,
        SECTION
    }
    
    private Set<Target> targets;

    private FormMessage formMessage;

    private boolean skippable;
    
    public TargetFormMessage(Set<String> targets, FormMessage formMessage, boolean skippable) {
        this.targets = new HashSet<Target>();
        for (String target: targets) {
            this.targets.add(new FieldTarget(target));
        }
        
        this.targets = Collections.unmodifiableSet(this.targets);
        this.formMessage = formMessage;
        this.skippable = skippable;
    }

    public TargetFormMessage(FormMessage formMessage, boolean skippable, String... targets) {
        this.targets = new HashSet<Target>();
        for (String target: new HashSet<String>(Arrays.asList(targets))) {
            this.targets.add(new FieldTarget(target));
        }
        
        this.targets = Collections.unmodifiableSet(this.targets);
        this.formMessage = formMessage;
        this.skippable = skippable;
    }

    public TargetFormMessage(FormMessage formMessage, boolean skippable, Target... targets) {
        this.targets = Collections.unmodifiableSet(new HashSet<Target>(Arrays.asList(targets)));
        this.formMessage = formMessage;
        this.skippable = skippable;
    }

    public Set<Target> getTargets() {
        return targets;
    }

    public boolean isWithTargets() {
        return targets != null && !targets.isEmpty();
    }

    public boolean isFieldTarget(String target) {
        for (Target _target: targets) {
            if (_target.isFieldTarget() && _target.getName().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSectionTarget(String target) {
        for (Target _target: targets) {
            if (_target.isSectionTarget() && _target.getName().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSkippable() {
        return skippable;
    }

    public FormMessage getFormMessage() {
        return formMessage;
    }

    public static class FieldTarget extends Target {

        public FieldTarget(String name) {
            super(TargetType.FIELD, name);
        }
        
    }

    public static class SectionTarget extends Target {

        public SectionTarget(String name) {
            super(TargetType.SECTION, name);
        }
        
    }
    
    public static abstract class Target {
        
        private final TargetType type;
        
        private final String name;

        public Target(TargetType type, String name) {
            this.type = type;
            this.name = name;
        }

        public TargetType getType() {
            return type;
        }

        public String getName() {
            return name;
        } 
        
        public boolean isFieldTarget() {
            return TargetType.FIELD.equals(type);
        }
        
        public boolean isSectionTarget() {
            return TargetType.SECTION.equals(type);
        }
    }
}
