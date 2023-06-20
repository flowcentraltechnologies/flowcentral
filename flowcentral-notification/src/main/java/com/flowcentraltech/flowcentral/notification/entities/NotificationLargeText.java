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
package com.flowcentraltech.flowcentral.notification.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Table;

/**
 * Entity for storing notification large text information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFLARGETEXT")
public class NotificationLargeText extends BaseApplicationEntity {

    @Column(length = 128, nullable = true)
    private String entity;

    @Column(type = ColumnType.CLOB)
    private String body;
    
    @Column(nullable = true)
    private Integer linesPerPage;
    
    @Column(nullable = true)
    private Integer fontSizeInPixels;
    
    @ChildList
    private List<NotificationLargeTextParam> paramList;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getLinesPerPage() {
        return linesPerPage;
    }

    public void setLinesPerPage(Integer linesPerPage) {
        this.linesPerPage = linesPerPage;
    }

    public Integer getFontSizeInPixels() {
        return fontSizeInPixels;
    }

    public void setFontSizeInPixels(Integer fontSizeInPixels) {
        this.fontSizeInPixels = fontSizeInPixels;
    }

    public List<NotificationLargeTextParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<NotificationLargeTextParam> paramList) {
        this.paramList = paramList;
    }

}
