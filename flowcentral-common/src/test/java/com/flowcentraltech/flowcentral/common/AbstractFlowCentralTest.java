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
package com.flowcentraltech.flowcentral.common;

import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.AbstractUnifyComponentTest;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.DatabaseTransactionManager;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.system.entities.AbstractSequencedEntity;

/**
 * Convenient abstract base class for flowcentral tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFlowCentralTest extends AbstractUnifyComponentTest {

    @Override
    protected void doAddSettingsAndDependencies() throws Exception {
        super.doAddSettingsAndDependencies();
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_NAME, "flowCentral");
        addContainerSetting(UnifyCorePropertyConstants.APPLICATION_BOOT,
                CommonModuleNameConstants.APPLICATION_BOOTSERVICE);
    }

    @SuppressWarnings("unchecked")
    protected void deleteAll(Class<? extends Entity>... entityClasses) throws Exception {
        Database db = (Database) getComponent(ApplicationComponents.APPLICATION_DATABASE);
        DatabaseTransactionManager tm = (DatabaseTransactionManager) getComponent(
                ApplicationComponents.APPLICATION_DATABASETRANSACTIONMANAGER);
        tm.beginTransaction();
        try {
            for (Class<? extends Entity> type : entityClasses) {
                if (AbstractSequencedEntity.class.isAssignableFrom(type)) {
                    db.deleteAll(Query.of(type).addGreaterThan("id", 0L));
                } else {
                    db.deleteAll(Query.of(type).ignoreEmptyCriteria(true));
                }
            }
        } finally {
            tm.endTransaction();
        }
    }

    protected void deleteAll(Query<? extends Entity> query) throws Exception {
        Database db = (Database) getComponent(ApplicationComponents.APPLICATION_DATABASE);
        DatabaseTransactionManager tm = (DatabaseTransactionManager) getComponent(
                ApplicationComponents.APPLICATION_DATABASETRANSACTIONMANAGER);
        tm.beginTransaction();
        try {
            db.deleteAll(query);
        } finally {
            tm.endTransaction();
        }
    }

}
