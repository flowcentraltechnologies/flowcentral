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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Reference configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RefConfig extends BaseNameConfig {

    private String entity;

    private String searchField;

    private String searchTable;

    private String selectHandler;
    
    private String listFormat;

    private String filterGenerator;

    private String filterGeneratorRule;

    private FilterConfig filter;

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSearchField() {
        return searchField;
    }

    @XmlAttribute(required = true)
    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchTable() {
        return searchTable;
    }

    @XmlAttribute
    public void setSearchTable(String searchTable) {
        this.searchTable = searchTable;
    }

    public String getSelectHandler() {
        return selectHandler;
    }

    @XmlAttribute
    public void setSelectHandler(String selectHandler) {
        this.selectHandler = selectHandler;
    }

    public String getListFormat() {
        return listFormat;
    }

    @XmlAttribute
    public void setListFormat(String listFormat) {
        this.listFormat = listFormat;
    }

    public String getFilterGenerator() {
        return filterGenerator;
    }

    @XmlAttribute
    public void setFilterGenerator(String filterGenerator) {
        this.filterGenerator = filterGenerator;
    }

    public String getFilterGeneratorRule() {
        return filterGeneratorRule;
    }

    @XmlAttribute
    public void setFilterGeneratorRule(String filterGeneratorRule) {
        this.filterGeneratorRule = filterGeneratorRule;
    }

    public FilterConfig getFilter() {
        return filter;
    }

    @XmlElement
    public void setFilter(FilterConfig filter) {
        this.filter = filter;
    }
}
