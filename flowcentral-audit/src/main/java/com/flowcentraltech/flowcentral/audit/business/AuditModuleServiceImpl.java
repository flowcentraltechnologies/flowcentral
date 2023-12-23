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
package com.flowcentraltech.flowcentral.audit.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.audit.constants.AuditModuleNameConstants;
import com.flowcentraltech.flowcentral.audit.data.EntityAuditConfigDef;
import com.flowcentraltech.flowcentral.audit.entities.EntityAuditConfig;
import com.flowcentraltech.flowcentral.audit.entities.EntityAuditConfigQuery;
import com.flowcentraltech.flowcentral.audit.entities.EntityAuditDetails;
import com.flowcentraltech.flowcentral.audit.entities.EntityAuditKeys;
import com.flowcentraltech.flowcentral.audit.entities.EntityAuditSnapshot;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.AuditSnapshot;
import com.flowcentraltech.flowcentral.common.data.EntityFieldAudit;
import com.flowcentraltech.flowcentral.common.data.FormattedAudit;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Default audit business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(AuditModuleNameConstants.AUDIT_MODULE_SERVICE)
public class AuditModuleServiceImpl extends AbstractFlowCentralService implements AuditModuleService {

    private static final String AUDITTRAIL_AUDITNO_FORMAT = "AT{yyyy}{MM}{DD}{N:10}";

    @Configurable
    private AppletUtilities appletUtilities;

    private FactoryMap<String, EntityAuditConfigDef> entityAuditConfigDefFactoryMap;

    public AuditModuleServiceImpl() {
        this.entityAuditConfigDefFactoryMap = new FactoryMap<String, EntityAuditConfigDef>(true)
            {
                @Override
                protected boolean stale(String name, EntityAuditConfigDef entityAuditConfigDef) throws Exception {
                    return environment().value(long.class, "versionNo", new EntityAuditConfigQuery()
                            .id(entityAuditConfigDef.getId())) > entityAuditConfigDef.getVersion();
                }

                @Override
                protected EntityAuditConfigDef create(String name, Object... args) throws Exception {
                    EntityAuditConfig entityAuditConfig = environment().find(new EntityAuditConfigQuery().name(name));
                    return new EntityAuditConfigDef(entityAuditConfig.getId(), entityAuditConfig.getVersionNo(),
                            entityAuditConfig.getSourceType(), entityAuditConfig.getName(),
                            entityAuditConfig.getDescription(), entityAuditConfig.getEntity(),
                            entityAuditConfig.getSearchFieldA(), entityAuditConfig.getSearchFieldB(),
                            entityAuditConfig.getSearchFieldC(), entityAuditConfig.getSearchFieldD());
                }
            };
    }

    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    @Override
    public boolean supportsAuditLog(AuditSourceType sourceType, String entity) {
        try {
            return environment().countAll(
                    new EntityAuditConfigQuery().sourceType(sourceType).entity(entity).status(RecordStatus.ACTIVE)) > 0;
        } catch (UnifyException e) {
            logSevere(e);
        }

        return false;
    }

    @Override
    public void log(AuditSnapshot auditSnapshot) {
        try {
            if (auditSnapshot.isWithSnapshots()) {
                List<String> configNames = environment().valueList(String.class, "name",
                        new EntityAuditConfigQuery().sourceType(auditSnapshot.getSourceType())
                                .entity(auditSnapshot.getEntity()).status(RecordStatus.ACTIVE));
                if (!DataUtils.isBlank(configNames)) {
                    EntityAuditDetails entityAuditDetails = new EntityAuditDetails();
                    entityAuditDetails.setAuditNo(appletUtilities.sequenceCodeGenerator().getNextSequenceCode(
                            AuditModuleNameConstants.AUDIT_MODULE_SERVICE, AUDITTRAIL_AUDITNO_FORMAT, getNow(), null));
                    entityAuditDetails.setEventTimestamp(auditSnapshot.getEventTimestamp());
                    entityAuditDetails.setEventType(auditSnapshot.getEventType());
                    entityAuditDetails.setRoleCode(auditSnapshot.getRoleCode());
                    entityAuditDetails.setSourceName(auditSnapshot.getSourceName());
                    entityAuditDetails.setUserIpAddress(auditSnapshot.getUserIpAddress());
                    entityAuditDetails.setUserLoginId(auditSnapshot.getUserLoginId());
                    entityAuditDetails.setUserName(auditSnapshot.getUserName());
                    Long entityAuditDetailId = (Long) environment().create(entityAuditDetails);

                    com.flowcentraltech.flowcentral.common.data.EntityAuditSnapshot rootSnapshot = auditSnapshot
                            .getSnapshots().get(0);
                    for (String configName : configNames) {
                        EntityAuditKeys entityAuditKeys = new EntityAuditKeys();
                        EntityAuditConfigDef entityAuditConfigDef = entityAuditConfigDefFactoryMap.get(configName);
                        entityAuditKeys.setEntityAuditConfigId(entityAuditConfigDef.getId());
                        entityAuditKeys.setEntityAuditDetailsId(entityAuditDetailId);
                        if (entityAuditConfigDef.isWithSearchFieldA()) {
                            String keyA = resolveKey(rootSnapshot, entityAuditConfigDef.getSearchFieldA());
                            entityAuditKeys.setKeyA(keyA);
                        }

                        if (entityAuditConfigDef.isWithSearchFieldB()) {
                            String keyB = resolveKey(rootSnapshot, entityAuditConfigDef.getSearchFieldB());
                            entityAuditKeys.setKeyB(keyB);
                        }

                        if (entityAuditConfigDef.isWithSearchFieldC()) {
                            String keyC = resolveKey(rootSnapshot, entityAuditConfigDef.getSearchFieldC());
                            entityAuditKeys.setKeyC(keyC);
                        }

                        if (entityAuditConfigDef.isWithSearchFieldD()) {
                            String keyD = resolveKey(rootSnapshot, entityAuditConfigDef.getSearchFieldD());
                            entityAuditKeys.setKeyD(keyD);
                        }

                        environment().create(entityAuditKeys);
                    }

                    FormattedAudit formattedAudit = appletUtilities.formatAudit(auditSnapshot);
                    EntityAuditSnapshot entityAuditSnapshot = new EntityAuditSnapshot();
                    entityAuditSnapshot.setEntityAuditDetailsId(entityAuditDetailId);
                    entityAuditSnapshot.setSnapshot( DataUtils.asJsonString(formattedAudit, PrintFormat.NONE));
                    environment().create(entityAuditSnapshot);
                }
            }
        } catch (UnifyException e) {
            logSevere(e);
        }
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private String resolveKey(com.flowcentraltech.flowcentral.common.data.EntityAuditSnapshot rootSnapshot,
            String searchFieldName) throws UnifyException {
        EntityFieldAudit entityFieldAudit = rootSnapshot.getEntityFieldAudit(searchFieldName);
        if (rootSnapshot.getEventType().isCreate()) {
            return (String) entityFieldAudit.getNewValue();
        }

        return (String) entityFieldAudit.getOldValue();
    }

}
