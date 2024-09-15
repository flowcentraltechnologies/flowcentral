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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Reference configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class RefConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String searchField;

    @JacksonXmlProperty(isAttribute = true)
    private String orderField;

    @JacksonXmlProperty(isAttribute = true)
    private String searchTable;

    @JacksonXmlProperty(isAttribute = true)
    private String selectHandler;
    
    @JacksonXmlProperty(isAttribute = true)
    private String listFormat;

    @JacksonXmlProperty(isAttribute = true)
    private String filterGenerator;

    @JacksonXmlProperty(isAttribute = true)
    private String filterGeneratorRule;

    @JacksonXmlProperty
    private FilterConfig filter;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
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

    public FilterConfig getFilter() {
        return filter;
    }

    public void setFilter(FilterConfig filter) {
        this.filter = filter;
    }
}
