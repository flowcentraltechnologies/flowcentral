/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ReplicationMatchType;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValueConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValuesConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application replication utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationReplicationUtils {

    private ApplicationReplicationUtils() {

    }

    public static ApplicationReplicationContext createApplicationReplicationContext(AppletUtilities au,
            String srcApplicationName, String destApplicationName, byte[] replicationRulesFile) throws UnifyException {
        ApplicationReplicationRule.Builder narrb = new ApplicationReplicationRule.Builder(ReplicationMatchType.PREFIX);
        ApplicationReplicationRule.Builder carrb = new ApplicationReplicationRule.Builder(ReplicationMatchType.PREFIX);
        carrb.replace(srcApplicationName + ".", destApplicationName + ".");
        ApplicationReplicationRule.Builder marrb = new ApplicationReplicationRule.Builder(ReplicationMatchType.WILD_SUFFIX);
        ApplicationReplicationRule.Builder clarrb = new ApplicationReplicationRule.Builder(ReplicationMatchType.CLASS);        
        return new ApplicationReplicationContext(au, narrb.build(),
                carrb.build(), marrb.build(),
                clarrb.build());
    }

    public static FilterConfig getReplicatedFilterConfig(ApplicationReplicationContext ctx, AppFilter appFilter)
            throws UnifyException {
        FilterConfig filterConfig = InputWidgetUtils.getFilterConfig(ctx.au(), appFilter);
        if (filterConfig != null) {

        }

        return filterConfig;
    }

    public static SetValuesConfig getReplicatedSetValuesConfig(ApplicationReplicationContext ctx, String valueGenerator,
            AppSetValues appSetValues) throws UnifyException {
        SetValuesConfig setValuesConfig = InputWidgetUtils.getSetValuesConfig(valueGenerator, appSetValues);
        if (setValuesConfig != null) {
            setValuesConfig.setDescription(ctx.messageSwap(setValuesConfig.getDescription()));
            if (!DataUtils.isBlank(setValuesConfig.getSetValueList())) {
                for (SetValueConfig setValueConfig : setValuesConfig.getSetValueList()) {
                    if (SetValueType.GENERATOR.equals(setValueConfig.getType())) {
                        setValueConfig.setValue(ctx.componentSwap(setValueConfig.getValue()));
                    }
                }
            }
        }

        return setValuesConfig;
    }
}
