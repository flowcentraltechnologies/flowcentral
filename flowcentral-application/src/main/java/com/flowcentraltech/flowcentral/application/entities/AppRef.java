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

import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;

/**
 * Application reference entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REF", indexes = { @Index("entity") })
public class AppRef extends BaseApplicationEntity {

    @Column(length = 128)
    private String entity;

    @Column(length = 64, nullable = true)
    private String orderField;

    @Column(length = 64, nullable = true)
    private String searchField;

    @Column(length = 64, nullable = true)
    private String searchTable;

    @Column(length = 64, nullable = true)
    private String selectHandler;

    @Column(length = 512, nullable = true)
    private String listFormat;
    
    @Column(length = 64, nullable = true)
    private String filterGenerator;

    @Column(length = 64, nullable = true)
    private String filterGeneratorRule;

    @Child(category = "ref")
    private AppFilter filter;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchTable() {
        return searchTable;
    }

    public void setSearchTable(String searchTable) {
        this.searchTable = searchTable;
    }

    public String getSelectHandler() {
        return selectHandler;
    }

    public void setSelectHandler(String selectHandler) {
        this.selectHandler = selectHandler;
    }

    public String getListFormat() {
        return listFormat;
    }

    public void setListFormat(String listFormat) {
        this.listFormat = listFormat;
    }

    public String getFilterGenerator() {
        return filterGenerator;
    }

    public void setFilterGenerator(String filterGenerator) {
        this.filterGenerator = filterGenerator;
    }

    public String getFilterGeneratorRule() {
        return filterGeneratorRule;
    }

    public void setFilterGeneratorRule(String filterGeneratorRule) {
        this.filterGeneratorRule = filterGeneratorRule;
    }

    public AppFilter getFilter() {
        return filter;
    }

    public void setFilter(AppFilter filter) {
        this.filter = filter;
    }

}
