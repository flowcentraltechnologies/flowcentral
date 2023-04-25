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
package com.flowcentraltech.flowcentral.application.web.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Details form listing.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DetailsFormListing {

    private String generator;

    private Formats formats;

    private List<Summary> preSummaries;

    private List<Summary> postSummaries;

    private TableDef tableDef;

    private Map<String, Object> properties;

    private List<? extends Entity> details;

    private int summaryTitleColumn;

    private boolean spreadSheet;

    private DetailsFormListing(String generator, Formats formats, TableDef tableDef, Map<String, Object> properties,
            List<? extends Entity> details, int summaryTitleColumn, boolean spreadSheet, List<Summary> preSummaries,
            List<Summary> postSummaries) {
        this.generator = generator;
        this.formats = formats;
        this.tableDef = tableDef;
        this.properties = properties;
        this.details = details;
        this.summaryTitleColumn = summaryTitleColumn;
        this.spreadSheet = spreadSheet;
        this.preSummaries = preSummaries;
        this.postSummaries = postSummaries;
    }

    public String getGenerator() {
        return generator;
    }

    public Formats getFormats() {
        return formats;
    }

    public TableDef getTableDef() {
        return tableDef;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public <T> T getProperty(Class<T> dataType, String name) throws UnifyException {
        return DataUtils.convert(dataType, properties.get(name));
    }

    public List<? extends Entity> getDetails() {
        return details;
    }

    public boolean isSerialNo() {
        return tableDef.isSerialNo();
    }

    public int getSummaryTitleColumn() {
        return summaryTitleColumn;
    }

    public List<Summary> getPreSummaries() {
        return preSummaries;
    }

    public List<Summary> getPostSummaries() {
        return postSummaries;
    }

    public boolean isWithPreSummaries() {
        return !DataUtils.isBlank(preSummaries);
    }

    public boolean isWithPostSummaries() {
        return !DataUtils.isBlank(postSummaries);
    }

    public boolean isWithSummaries() {
        return isWithPreSummaries() || isWithPostSummaries();
    }
    
    public boolean isSpreadSheet() {
        return spreadSheet;
    }

    public static Builder newBuilder(TableDef tableDef, List<? extends Entity> details) {
        return new Builder(tableDef, details);
    }

    public static class Builder {

        private String generator;

        private Formats formats;

        private TableDef tableDef;

        private Map<String, Object> properties;

        private List<Summary> preSummaries;

        private List<Summary> postSummaries;

        private List<? extends Entity> details;

        private int summaryTitleColumn;

        private boolean spreadSheet;

        public Builder(TableDef tableDef, List<? extends Entity> details) {
            this.properties = new HashMap<String, Object>();
            this.tableDef = tableDef;
            this.details = details;
            this.preSummaries = new ArrayList<Summary>();
            this.postSummaries = new ArrayList<Summary>();
        }

        public Builder useGenerator(String generator) throws UnifyException {
            this.generator = generator;
            return this;
        }

        public Builder spreadSheet(boolean spreadSheet) throws UnifyException {
            this.spreadSheet = spreadSheet;
            return this;
        }

        public Builder summaryTitleColumn(int summaryTitleColumn) throws UnifyException {
            this.summaryTitleColumn = summaryTitleColumn;
            return this;
        }

        public Builder addPreSummary(Summary summary) throws UnifyException {
            preSummaries.add(summary);
            return this;
        }

        public Builder addPostSummary(Summary summary) throws UnifyException {
            postSummaries.add(summary);
            return this;
        }

        public Builder useFormats(Formats formats) throws UnifyException {
            this.formats = formats;
            return this;
        }

        public Builder addProperty(String name, Object val) throws UnifyException {
            properties.put(name, val);
            return this;
        }

        public Builder addProperties(Map<String, Object> properties) throws UnifyException {
            this.properties.putAll(properties);
            return this;
        }

        public DetailsFormListing build() {
            if (StringUtils.isBlank(generator)) {
                throw new IllegalArgumentException("Generator is required!");
            }

            return new DetailsFormListing(generator, formats, tableDef, Collections.unmodifiableMap(properties),
                    Collections.unmodifiableList(details), summaryTitleColumn, spreadSheet,
                    Collections.unmodifiableList(preSummaries), Collections.unmodifiableList(postSummaries));
        }
    }
}
