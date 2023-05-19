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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Details case.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DetailsCase {

    private String tableName;

    private List<? extends Entity> content;

    private Formats formats;

    private List<Summary> preSummaries;

    private List<Summary> postSummaries;

    private int summaryTitleColumn;

    public DetailsCase(String tableName, List<? extends Entity> content) {
        this.tableName = tableName;
        this.content = content;
        this.formats = Formats.DEFAULT;
        this.preSummaries = Collections.emptyList();
        this.postSummaries = Collections.emptyList();
        this.summaryTitleColumn = 0;
    }

    public DetailsCase(String tableName, List<? extends Entity> details, Formats formats) {
        this.tableName = tableName;
        this.content = details;
        this.formats = formats;
        this.preSummaries = Collections.emptyList();
        this.postSummaries = Collections.emptyList();
        this.summaryTitleColumn = 0;
    }

    public DetailsCase(String tableName, List<? extends Entity> details, Formats formats,
            Map<String, Object> properties, List<Summary> preSummaries, List<Summary> postSummaries,
            int summaryTitleColumn) {
        this.tableName = tableName;
        this.content = details;
        this.formats = formats;
        this.preSummaries = preSummaries;
        this.postSummaries = postSummaries;
        this.summaryTitleColumn = summaryTitleColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public List<? extends Entity> getContent() {
        return content;
    }

    public Formats getFormats() {
        return formats;
    }

    public boolean isWithPreSummaries() {
        return !DataUtils.isBlank(preSummaries);
    }
    
    public List<Summary> getPreSummaries() {
        return preSummaries;
    }

    public boolean isWithPostSummaries() {
        return !DataUtils.isBlank(postSummaries);
    }

    public List<Summary> getPostSummaries() {
        return postSummaries;
    }

    public boolean isWithSummaries() {
        return isWithPreSummaries() || isWithPostSummaries();
    }

    public int getSummaryTitleColumn() {
        return summaryTitleColumn;
    }

}
