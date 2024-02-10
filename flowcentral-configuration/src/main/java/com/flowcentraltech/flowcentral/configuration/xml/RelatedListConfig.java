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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Related list configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RelatedListConfig extends BaseNameConfig {

    private String applet;

    private String filter;

    private String editAction;

    public String getApplet() {
        return applet;
    }

    @XmlAttribute(required = true)
    public void setApplet(String applet) {
        this.applet = applet;
    }

    public String getFilter() {
        return filter;
    }

    @XmlAttribute
    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEditAction() {
        return editAction;
    }

    @XmlAttribute
    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

}
