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

package com.flowcentraltech.flowcentral.application.util;

import java.util.Arrays;

import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.database.Query;

/**
 * Application query utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationQueryUtils {

    private ApplicationQueryUtils() {

    }

    public static void addWidgetTypeCriteria(Query<AppWidgetType> query, EntityFieldDataType entityFieldDataType) {
        if (entityFieldDataType == null) {
            query.addRestriction(new IsNull("dataType"));
        } else {
            if (entityFieldDataType.isEntityRef()) {
                if (entityFieldDataType.isRefFileUpload()) {
                    query.addRestriction(new And().add(new Equals("applicationName", "application"))
                            .add(new Equals("name", "fileupload")));
                } else {
                    query.addRestriction(new And().add(new Equals("applicationName", "application"))
                            .add(new Amongst("name", Arrays.asList("entitylist", "entitysearch", "entityselect"))));
                }
            } else if (EntityFieldDataType.BLOB.equals(entityFieldDataType)) {
                query.addRestriction(new And().add(new Equals("applicationName", "application"))
                        .add(new Amongst("name", Arrays.asList("fileuploadwildcard", "fileuploadimage", "fileuploadcsv",
                                "fileuploadexcel", "fileuploadpdf", "fileuploadtext", "picture"))));
            } else if (entityFieldDataType.isEnumDataType()) {
                query.addRestriction(new Or()
                        .add(new And().add(new Equals("applicationName", "application")).add(
                                new Amongst("name", Arrays.asList("enumlist", "enumreadonlytext", "enumlistlabel"))))
                        .add(new And().add(new Equals("dataType", DataType.STRING))
                                .add(new Equals("enumOption", Boolean.TRUE)).add(new Equals("listOption", true))));
            } else if (entityFieldDataType.isEnumDynamic()) {
                query.addEquals("applicationName", "application").addEquals("name", "dynamicenumlist");
            } else if (entityFieldDataType.isMapped()) {
                query.addRestriction(new Equals("inputType", InputType.MAPPED));
            } else {
                DataType dataType = entityFieldDataType.dataType();
                if (dataType == null) {
                    query.addRestriction(new IsNull("dataType"));
                } else {
                    query.addRestriction(new And().add(new Amongst("dataType", dataType.convertibleFromTypes()))
                            .add(new Equals("listOption", true)));
                }
            }
        }
    }
}
