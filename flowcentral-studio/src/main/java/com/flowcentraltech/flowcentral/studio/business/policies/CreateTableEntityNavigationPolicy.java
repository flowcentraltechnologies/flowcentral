/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.business.policies;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.util.EntityCompositionUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityComposition;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.data.ValidationErrors;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.studio.constants.StudioModuleSysParamConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.sql.SqlColumnInfo;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.EntityTypeInfo;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Create Table entity navigation policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "studio.studioJsonEntity" })
@Component(name = "createtableentity-navigationpolicy", description = "Create Table Entity Nav, Policy")
public class CreateTableEntityNavigationPolicy extends AbstractStudioAppletNavigationPolicy {

    private static final String DEFAULT_DATEFORMAT = "dd-MM-yyyy";

    private static final String DEFAULT_DATETIMEFORMAT = "dd-MM-yyyy HH:mm:ss";

    @Configurable
    private SystemModuleService systemModuleService;

    public CreateTableEntityNavigationPolicy() {
        super(Arrays.asList("composition"));
    }

    @Override
    public void onInit(ValueStore inst) throws UnifyException {
        inst.store("applicationId",
                getSessionAttribute(Long.class, StudioSessionAttributeConstants.CURRENT_APPLICATION_ID));
        inst.store("baseType", EntityBaseType.BASE_ENTITY);
    }

    @Override
    public void onNext(ValueStore inst, ValidationErrors errors, int currentPage, Map<String, Object> pageAttributes)
            throws UnifyException {
        if (currentPage == 1) {
            final String entityName = inst.retrieve(String.class, "entityName");
            final String datasource = inst.retrieve(String.class, "datasource");
            final String table = inst.retrieve(String.class, "table");
            final List<String> columns = Arrays.asList(StringUtils.commaSplit(inst.retrieve(String.class, "columns")));
            Map<String, SqlColumnInfo> map = new HashMap<String, SqlColumnInfo>();
            for (SqlColumnInfo sqlColumnInfo : systemModuleService.findDataSourceColumns(datasource, table)) {
                map.put(sqlColumnInfo.getColumnName(), sqlColumnInfo);
            }

            final EntityTypeInfo.Builder etib = EntityTypeInfo.newBuilder(entityName, table, 0);
            for (String columnName : columns) {
                SqlColumnInfo sqlColumnInfo = map.get(columnName);
                etib.addFieldInfo(DataUtils.getDataType(sqlColumnInfo.getJavaType()),
                        NameUtils.inflateAsName(columnName), null, columnName, null, false);
            }

            final EntityTypeInfo entityTypeInfo = etib.build();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat(inst.isNotNull("dateFormatter")
                    ? StandardFormatType.fromCode(inst.retrieve(String.class, "dateFormatter")).format()
                    : DEFAULT_DATEFORMAT);
            final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(inst.isNotNull("dateTimeFormatter")
                    ? StandardFormatType.fromCode(inst.retrieve(String.class, "dateTimeFormatter")).format()
                    : DEFAULT_DATETIMEFORMAT);

            // Sets entity composition JSON
            final String tablePrefix = systemModuleService.getSysParameterValue(String.class,
                    StudioModuleSysParamConstants.DEFAULT_TABLE_PREFIX);
            final Long applicationId = inst.retrieve(Long.class, "applicationId");
            final ApplicationDef applicationDef = au().application().getApplicationDef(applicationId);
            final EntityComposition entityComposition = EntityCompositionUtils.createEntityComposition(dateFormatter,
                    dateTimeFormatter, tablePrefix, applicationDef.getModuleShortCode(), Arrays.asList(entityTypeInfo));
            final String compositionJson = DataUtils.asJsonString(entityComposition, PrintFormat.PRETTY);
            inst.store("refinedStructure", compositionJson);
        } else if (currentPage == 2) {
            inst.store("generateEntity", true);
            inst.store("generateImport", true);
            if (inst.isNull("loadSourceJSON")) {
                inst.store("loadSourceJSON", false);
            }

            if (inst.isNull("generateApplet")) {
                inst.store("generateApplet", true);
            }

            if (inst.isNull("generateRest")) {
                inst.store("generateRest", true);
            }

            final EntityComposition entityComposition = (EntityComposition) pageAttributes.get("composition");
            final String compositionJson = DataUtils.asJsonString(entityComposition, PrintFormat.PRETTY);
            inst.store("refinedStructure", compositionJson);

            if (StringUtils.isBlank(entityComposition.getEntries().get(0).getPkFieldName())) {
                errors.addValidationError("Primary key has not been selected!");
            }
        }
    }

    @Override
    public void onPrevious(ValueStore inst, ValidationErrors errors, int currentPage,
            Map<String, Object> pageAttributes) throws UnifyException {

    }

}
