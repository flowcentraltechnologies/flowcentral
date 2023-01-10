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

package com.flowcentraltech.flowcentral.application.data;

/**
 * Usage type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum UsageType {

    APPLET,
    ENTITY,
    FORM;

    public boolean isEntity() {
        return ENTITY.equals(this);
    }

    public boolean isApplet() {
        return APPLET.equals(this);
    }

    public boolean isForm() {
        return FORM.equals(this);
    }

    public static boolean isQualifiesEntity(UsageType type) {
        return type == null || ENTITY.equals(type);
    }

    public static boolean isQualifiesApplet(UsageType type) {
        return type == null || APPLET.equals(type);
    }

    public static boolean isQualifiesForm(UsageType type) {
        return type == null || FORM.equals(type);
    }
}
