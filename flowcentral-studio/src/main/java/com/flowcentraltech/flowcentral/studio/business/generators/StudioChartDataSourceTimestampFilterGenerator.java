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
package com.flowcentraltech.flowcentral.studio.business.generators;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.AbstractEntityBasedFilterGenerator;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;

@EntityReferences({ "application.appRef" })
@Component(name = "chartadatasourcetimestamp-filtergenerator",
        description = "Chart Datasource Timestamp Filter Generator")
public class StudioChartDataSourceTimestampFilterGenerator extends AbstractEntityBasedFilterGenerator {

    @Override
    public Restriction generate(ValueStoreReader entityInstReader, String rule) throws UnifyException {
        final String entity = entityInstReader.read(String.class, "entity");
        final String categoryField = entityInstReader.read(String.class, "categoryField");
        if (!StringUtils.isBlank(entity) && !StringUtils.isBlank(categoryField)) {
            EntityFieldDef entityFieldDef = application().getEntityDef(entity).getFieldDef(categoryField);
            if (entityFieldDef.isDate() || entityFieldDef.isTimestamp()) {
                return new IsNotNull(categoryField);
            }
        }

        return new Equals(categoryField, "0");
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

}
