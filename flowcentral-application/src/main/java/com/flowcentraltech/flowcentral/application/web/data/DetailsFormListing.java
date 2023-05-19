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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<DetailsCase> caseList;

    private Map<String, Object> properties;

    private String generator;

    private int columns;

    private boolean spreadSheet;

    private DetailsFormListing(List<DetailsCase> caseList, Map<String, Object> properties, String generator,
            int columns, boolean spreadSheet) {
        this.caseList = caseList;
        this.properties = properties;
        this.generator = generator;
        this.columns = columns;
        this.spreadSheet = spreadSheet;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public <T> T getProperty(Class<T> dataType, String name) throws UnifyException {
        return DataUtils.convert(dataType, properties.get(name));
    }

    public List<DetailsCase> getCaseList() {
        return caseList;
    }

    public String getGenerator() {
        return generator;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isSpreadSheet() {
        return spreadSheet;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<DetailsCase> caseList;

        private String generator;

        private Formats formats;

        private String tableName;

        private Map<String, Object> properties;

        private List<Summary> preSummaries;

        private List<Summary> postSummaries;

        private List<? extends Entity> content;

        private int summaryTitleColumn;

        private int columns;

        private boolean spreadSheet;

        private boolean caseStarted;

        protected Builder() {
            this.caseList = new ArrayList<DetailsCase>();
            this.properties = new HashMap<String, Object>();
            this.columns = 1;
        }

        public Builder beginCase() {
            if (caseStarted) {
                throw new RuntimeException("Details case already started.");
            }

            preSummaries = new ArrayList<Summary>();
            postSummaries = new ArrayList<Summary>();
            formats = Formats.DEFAULT;
            caseStarted = true;
            return this;
        }

        public Builder endCase() {
            if (!caseStarted) {
                throw new RuntimeException("No details case started.");
            }

            if (StringUtils.isBlank(tableName)) {
                throw new RuntimeException("Table name is required for details case.");
            }

            if (content == null) {
                throw new RuntimeException("Content is required for details case.");
            }

            caseList.add(new DetailsCase(tableName, content, formats, properties, preSummaries, postSummaries,
                    summaryTitleColumn));
            tableName = null;
            content = null;
            preSummaries = null;
            postSummaries = null;
            caseStarted = false;
            return this;
        }

        public Builder addCase(DetailsCase _case) {
            if (caseStarted) {
                throw new RuntimeException("A details case is open.");
            }

            caseList.add(_case);
            return this;
        }

        public Builder usingGenerator(String generator) {
            this.generator = generator;
            return this;
        }

        public Builder columns(int columns) {
            this.columns = columns <= 0 ? 1 : columns;
            return this;
        }

        public Builder asSpreadSheet(boolean spreadSheet) {
            this.spreadSheet = spreadSheet;
            return this;
        }

        public Builder addProperty(String name, Object val) {
            properties.put(name, val);
            return this;
        }

        public Builder addProperties(Map<String, Object> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder tableName(String tableName) throws UnifyException {
            checkDetailsCaseStarted();
            this.tableName = tableName;
            return this;
        }

        public Builder withContent(List<? extends Entity> content) {
            checkDetailsCaseStarted();
            this.content = content;
            return this;
        }

        public Builder summaryTitleColumn(int summaryTitleColumn) {
            checkDetailsCaseStarted();
            this.summaryTitleColumn = summaryTitleColumn;
            return this;
        }

        public Builder addPreSummary(Summary summary) {
            checkDetailsCaseStarted();
            preSummaries.add(summary);
            return this;
        }

        public Builder addPostSummary(Summary summary) {
            checkDetailsCaseStarted();
            postSummaries.add(summary);
            return this;
        }

        public Builder usingFormats(Formats formats) {
            checkDetailsCaseStarted();
            this.formats = formats;
            return this;
        }

        private void checkDetailsCaseStarted() {
            if (!caseStarted) {
                throw new RuntimeException("Details case not open.");
            }
        }

        public DetailsFormListing build() {
            if (StringUtils.isBlank(generator)) {
                throw new IllegalArgumentException("Generator is required!");
            }

            if (caseList.isEmpty()) {
                throw new IllegalArgumentException("There must be at least one details case.");
            }

            if (caseStarted) {
                throw new RuntimeException("Last details case is open.");
            }

            return new DetailsFormListing(caseList, properties, generator, columns, spreadSheet);
        }
    }
}
