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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
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

    private EntityDef entityDef;

    private Map<String, Object> properties;

    private List<String> columnFieldNames;

    private List<? extends Entity> details;

    private DetailsFormListing(String generator, EntityDef entityDef, Map<String, Object> properties,
            List<String> columnFieldNames, List<? extends Entity> details) {
        this.generator = generator;
        this.entityDef = entityDef;
        this.properties = properties;
        this.columnFieldNames = columnFieldNames;
        this.details = details;
    }

    public String getGenerator() {
        return generator;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public <T> T getProperty(Class<T> dataType, String name) throws UnifyException {
        return DataUtils.convert(dataType, properties.get(name)) ;
    }

    public List<String> getColumnFieldNames() {
        return columnFieldNames;
    }

    public List<? extends Entity> getDetails() {
        return details;
    }

    public static Builder newBuilder(EntityDef entityDef, List<? extends Entity> details) {
        return new Builder(entityDef, details);
    }

    public static class Builder {

        private String generator;

        private EntityDef entityDef;

        private Map<String, Object> properties;

        private List<String> columnFieldNames;

        private List<? extends Entity> details;

        public Builder(EntityDef entityDef, List<? extends Entity> details) {
            this.properties = new HashMap<String, Object>();
            this.columnFieldNames = new ArrayList<String>();
            this.entityDef = entityDef;
            this.details = details;
        }

        public Builder useGenerator(String generator) throws UnifyException {
            this.generator = generator;
            return this;
        }

        public Builder addProperty(String name, Object val) throws UnifyException {
            properties.put(name, val);
            return this;
        }

        public Builder addColumn(String fieldName) throws UnifyException {
            internalAddColumn(fieldName);
            return this;
        }

        public Builder addColumns(List<String> fieldNames) throws UnifyException {
            for (String fieldName : fieldNames) {
                internalAddColumn(fieldName);
            }

            return this;
        }

        public Builder addColumns(TableDef tableDef) throws UnifyException {
            for (TableColumnDef tableColumnDef : tableDef.getVisibleColumnDefList()) {
                internalAddColumn(tableColumnDef.getFieldName());
            }

            return this;
        }

        private void internalAddColumn(String fieldName) throws UnifyException {
            entityDef.getFieldDef(fieldName); // Check
            columnFieldNames.add(fieldName);
        }

        public DetailsFormListing build() {
            if (StringUtils.isBlank(generator)) {
                throw new IllegalArgumentException("Generator is required!");
            }

            return new DetailsFormListing(generator, entityDef, Collections.unmodifiableMap(properties),
                    Collections.unmodifiableList(columnFieldNames), Collections.unmodifiableList(details));
        }
    }
}
