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

package com.flowcentraltech.flowcentral.system.business.policies;

import java.util.Optional;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.system.entities.DataSourceConnection;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.JDBCConnectionInfo;
import com.tcdng.unify.core.util.SqlUtils;

/**
 * Form action policy for testing datasource connections.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "system.dataSourceConnection" })
@Component("testdatasourceconnection-actionpolicy")
public class TestDataSourceConnectionFormActionPolicy extends AbstractFormActionPolicy {

    @Override
    public boolean checkAppliesTo(ValueStoreReader reader) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = new EntityActionResult(ctx);
        final DataSourceConnection dataSourceConnection = (DataSourceConnection) ctx.getInst();
        final JDBCConnectionInfo jdbcConnectionInfo = SqlUtils.getJDBCConnectionInfo(dataSourceConnection.getDialect(),
                dataSourceConnection.getHost(), dataSourceConnection.getPort(), dataSourceConnection.getDatabase(),
                dataSourceConnection.getService(), dataSourceConnection.getSchema(), dataSourceConnection.getUserName(),
                dataSourceConnection.getPassword());
        final Optional<String> error = SqlUtils.testJDBCConnection(jdbcConnectionInfo);
        String msg = null;
        if (error.isPresent()) {
           msg = "DataSource connection test failed! Reason: " + error.get();
            result.setFailureHint(msg);
        } else {
            msg = "DataSource connection test successful!";
            result.setSuccessHint("DataSource connection test successful!");
        }
        
        dataSourceConnection.setLastResult(msg);
        dataSourceConnection.setLastOn(environment().getNow());
        
        if (dataSourceConnection.getId() == null) {
            result.setSkipUpdate(true);
        } else {
           environment().updateByIdVersion(dataSourceConnection);
        }
        
        return result;
    }

}
