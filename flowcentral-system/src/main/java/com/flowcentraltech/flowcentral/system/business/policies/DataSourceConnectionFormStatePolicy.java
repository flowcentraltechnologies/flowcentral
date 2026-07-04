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

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.data.TargetFormTabStates;
import com.flowcentraltech.flowcentral.common.data.TargetFormWidgetStates;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.JDBCConnectionComponentDef.Type;
import com.tcdng.unify.core.database.JDBCConnectionDef;
import com.tcdng.unify.core.database.sql.SqlDialectNameConstants;
import com.tcdng.unify.core.util.SqlUtils;

/**
 * Consolidated form state policy for datasource connections.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "system.dataSourceConnection" })
@Component("datasourceconnection-formstatepolicy")
public class DataSourceConnectionFormStatePolicy extends AbstractConsolidatedFormStatePolicy {

    @Override
    public boolean performAutoUpdates(ValueStore instValueStore) throws UnifyException {
        return false;
    }

    @Override
    public void onFormConstruct(ValueStore instValueStore) throws UnifyException {

    }

    @Override
    public void onFormSwitch(ValueStore instValueStore, String trigger) throws UnifyException {
        if ("dialect".equals(trigger)) {
            final String dialect = instValueStore.retrieve(String.class, "dialect");
            if (dialect != null) {
                final JDBCConnectionDef jdbcConnectionDef = SqlUtils.getConnectionDef(dialect);
                instValueStore.store("host",
                        jdbcConnectionDef.getJDBCConnectionComponentDef(Type.HOST).getDefaultVal());
                instValueStore.store("port",
                        jdbcConnectionDef.getJDBCConnectionComponentDef(Type.PORT).getDefaultVal());
                instValueStore.store("service",
                        jdbcConnectionDef.getJDBCConnectionComponentDef(Type.SERVICE).getDefaultVal());
                instValueStore.store("database",
                        jdbcConnectionDef.getJDBCConnectionComponentDef(Type.DATABASE).getDefaultVal());
                instValueStore.store("schema",
                        jdbcConnectionDef.getJDBCConnectionComponentDef(Type.SCHEMA).getDefaultVal());
            }
        }
    }

    @Override
    protected boolean autoUpdateSupported() throws UnifyException {
        return false;
    }

    @Override
    protected void evaluateTabStates(ValueStoreReader reader, String trigger, TargetFormTabStates states)
            throws UnifyException {

    }

    @Override
    protected void evaluateWidgetStates(ValueStoreReader reader, String trigger, TargetFormWidgetStates states)
            throws UnifyException {
        final String dialect = reader.read(String.class, "dialect");
        if (dialect != null) {
            switch (dialect) {
                case SqlDialectNameConstants.MSSQL:
                case SqlDialectNameConstants.MSSQL_2012:
                    states.setFieldState(TriState.TRUE, TriState.CONFORMING, TriState.CONFORMING, TriState.CONFORMING,
                            "service", "database");
                    states.setFieldState(TriState.FALSE, TriState.CONFORMING, TriState.CONFORMING, TriState.TRUE,
                            "schema");
                    break;
                case SqlDialectNameConstants.ORACLE:
                case SqlDialectNameConstants.ORACLE_12C:
                    states.setFieldState(TriState.TRUE, TriState.CONFORMING, TriState.CONFORMING, TriState.CONFORMING,
                            "service");
                    states.setFieldState(TriState.FALSE, TriState.CONFORMING, TriState.CONFORMING, TriState.TRUE,
                            "schema", "database");
                    break;
                case SqlDialectNameConstants.POSTGRESQL:
                    states.setFieldState(TriState.TRUE, TriState.CONFORMING, TriState.CONFORMING, TriState.CONFORMING,
                            "database", "schema");
                    states.setFieldState(TriState.FALSE, TriState.CONFORMING, TriState.CONFORMING, TriState.TRUE,
                            "service");
                    break;
                case SqlDialectNameConstants.MARIADB:
                case SqlDialectNameConstants.MYSQL:
                    states.setFieldState(TriState.TRUE, TriState.CONFORMING, TriState.CONFORMING, TriState.CONFORMING,
                            "database");
                    states.setFieldState(TriState.FALSE, TriState.CONFORMING, TriState.CONFORMING, TriState.TRUE,
                            "schema", "service");
                    break;
                case SqlDialectNameConstants.HSQLDB:
                default:
                    states.setFieldState(TriState.TRUE, TriState.CONFORMING, TriState.CONFORMING, TriState.CONFORMING,
                            "database");
                    states.setFieldState(TriState.FALSE, TriState.CONFORMING, TriState.CONFORMING, TriState.TRUE,
                            "schema", "service");
                    break;
            }
        }
    }

}
