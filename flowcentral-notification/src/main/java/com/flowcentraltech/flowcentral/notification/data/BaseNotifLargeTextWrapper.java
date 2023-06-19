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
package com.flowcentraltech.flowcentral.notification.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenient abstract base class for notification large text wrappers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseNotifLargeTextWrapper implements NotifLargeTextWrapper {

    protected final NotifLargeTextDef notifLargeTextDef;
    
    protected final Map<String, Object> properties;

    public BaseNotifLargeTextWrapper(NotifLargeTextDef notifLargeTextDef) {
        this.notifLargeTextDef = notifLargeTextDef;
        this.properties = new HashMap<String, Object>();
    }

    public BaseNotifLargeTextWrapper(NotifLargeTextDef notifLargeTextDef, Map<String, Object> properties) {
        this.notifLargeTextDef = notifLargeTextDef;
        this.properties = properties;
    }

    @Override
    public String getLargeTextName() {
        return notifLargeTextDef.getLongName();
    }

    @Override
    public String getEntity() {
        return notifLargeTextDef.getEntity();
    }

    @Override
    public void addParameter(String name, Object val) {
        properties.put(name, val);
    }

    @Override
    public Map<String, Object> getParameters() {
        return properties;
    }
}
