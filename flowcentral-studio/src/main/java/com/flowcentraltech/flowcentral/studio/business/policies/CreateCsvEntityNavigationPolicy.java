/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.util.EntityCompositionUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityComposition;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage.FieldTarget;
import com.flowcentraltech.flowcentral.common.data.ValidationErrors;
import com.flowcentraltech.flowcentral.studio.constants.StudioModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.EntityTypeInfo;
import com.tcdng.unify.core.util.EntityTypeUtils;

/**
 * Create CSV entity navigation policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "studio.studioJsonEntity" })
@Component(name = "createcsventity-navigationpolicy", description = "Create CSV Entity Nav, Policy")
public class CreateCsvEntityNavigationPolicy extends AbstractStudioAppletNavigationPolicy {

    private static final String DEFAULT_DATEFORMAT = "dd-MM-yyyy";

    private static final String DEFAULT_DATETIMEFORMAT = "dd-MM-yyyy HH:mm:ss";

    @Configurable
    private SystemModuleService systemModuleService;

    public CreateCsvEntityNavigationPolicy() {
        super(Arrays.asList("composition"));
    }

    @Override
    public void onNext(ValueStore inst, ValidationErrors errors, int currentPage, Map<String, Object> pageAttributes)
            throws UnifyException {
        if (currentPage == 1) {
            final String sourceCsv = inst.retrieve(String.class, "sourceJson");
            final String entityName = inst.retrieve(String.class, "entityName");
            List<EntityTypeInfo> entityTypeInfos = EntityTypeUtils.getEntityTypeInfoFromCsv(entityName, sourceCsv);
            if (DataUtils.isBlank(entityTypeInfos)) {
                errors.addValidationError(new FieldTarget("sourceJson"), "$m{studio.csventity.validation.csv.invalid}");
            }

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
                    dateTimeFormatter, tablePrefix, applicationDef.getModuleShortCode(), entityTypeInfos);
            final String compositionJson = DataUtils.asJsonString(entityComposition, PrintFormat.PRETTY);
            inst.store("refinedStructure", compositionJson);
        } else if (currentPage == 2) {
            inst.store("generateEntity", true);
            inst.store("generateImport", true);
            if (inst.isNull("loadSourceJSON")) {
                inst.store("loadSourceJSON", true);
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
        }
    }

    @Override
    public void onPrevious(ValueStore inst, ValidationErrors errors, int currentPage,
            Map<String, Object> pageAttributes) throws UnifyException {

    }

}
