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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;

/**
 * Application property rule entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_PROPRULE")
public class AppPropertyRule extends BaseApplicationEntity {

    @Column(length = 128)
    private String entity;

    @Column(length = 64)
    private String choiceField;

    @Column(length = 64)
    private String listField;

    @Column(length = 64)
    private String propNameField;

    @Column(length = 64)
    private String propValField;

    @Column(length = 128, nullable = true)
    private String defaultList;

    @Column
    private boolean ignoreCase;

    @ChildList
    private List<AppPropertyRuleChoice> choiceList;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getChoiceField() {
        return choiceField;
    }

    public void setChoiceField(String choiceField) {
        this.choiceField = choiceField;
    }

    public String getListField() {
        return listField;
    }

    public void setListField(String listField) {
        this.listField = listField;
    }

    public String getPropNameField() {
        return propNameField;
    }

    public void setPropNameField(String propNameField) {
        this.propNameField = propNameField;
    }

    public String getPropValField() {
        return propValField;
    }

    public void setPropValField(String propValField) {
        this.propValField = propValField;
    }

    public String getDefaultList() {
        return defaultList;
    }

    public void setDefaultList(String defaultList) {
        this.defaultList = defaultList;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public List<AppPropertyRuleChoice> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<AppPropertyRuleChoice> choiceList) {
        this.choiceList = choiceList;
    }

}
