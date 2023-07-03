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
package com.flowcentraltech.flowcentral.common.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralEditionConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.logging.FieldAudit;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageBean;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Convenient abstract base class for flowcentral page controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFlowCentralPageController<T extends AbstractPageBean> extends AbstractPageController<T> {

    @Configurable
    private EnvironmentService environmentService;

    public AbstractFlowCentralPageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    protected final boolean isEnterprise() throws UnifyException {
        return FlowCentralEditionConstants.ENTERPRISE.equalsIgnoreCase(getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALLATION_TYPE));
    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected void logUserEvent(String eventName, String... details) throws UnifyException {
        getEventLogger().logUserEvent(eventName, details);
    }

    protected void logUserEvent(String eventName, List<String> details) throws UnifyException {
        getEventLogger().logUserEvent(eventName, details);
    }

    protected void logUserEvent(EventType eventType, Class<? extends Entity> entityClass) throws UnifyException {
        getEventLogger().logUserEvent(eventType, entityClass);
    }

    protected void logUserEvent(EventType eventType, Entity record, boolean isNewRecord) throws UnifyException {
        getEventLogger().logUserEvent(eventType, record, isNewRecord);
    }

    protected <U extends Entity> void logUserEvent(EventType eventType, U oldRecord, U newRecord)
            throws UnifyException {
        getEventLogger().logUserEvent(eventType, oldRecord, newRecord);
    }

    protected void logUserEvent(EventType eventType, Class<? extends Entity> entityClass, Long recordId,
            List<FieldAudit> fieldAuditList) throws UnifyException {
        getEventLogger().logUserEvent(eventType, entityClass, recordId, fieldAuditList);
    }

}
