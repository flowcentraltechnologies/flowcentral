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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application applet filter entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_APPLETROUTETOAPPLET",
    uniqueConstraints = {
        @UniqueConstraint({ "appAppletId", "routeToApplet" }) })
public class AppAppletRouteToApplet extends BaseAuditEntity {

    @ForeignKey(AppApplet.class)
    private Long appAppletId;
    
    @Column(length = 128)
    private String routeToApplet;

    @Override
    public String getDescription() {
        return routeToApplet;
    }

    public Long getAppAppletId() {
        return appAppletId;
    }

    public void setAppAppletId(Long appAppletId) {
        this.appAppletId = appAppletId;
    }

    public String getRouteToApplet() {
        return routeToApplet;
    }

    public void setRouteToApplet(String routeToApplet) {
        this.routeToApplet = routeToApplet;
    }

}
