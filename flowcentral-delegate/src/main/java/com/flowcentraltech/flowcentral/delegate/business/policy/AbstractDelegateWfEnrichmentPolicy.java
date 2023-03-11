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

package com.flowcentraltech.flowcentral.delegate.business.policy;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractWfEnrichmentPolicy;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for delegate workflow enrichment policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDelegateWfEnrichmentPolicy extends AbstractWfEnrichmentPolicy {

    @Configurable
    private EnvironmentDelegateUtilities utilities;

    private String operation;
    
    public AbstractDelegateWfEnrichmentPolicy(String operation) {
        this.operation = operation;
    }

    public final void setUtilities(EnvironmentDelegateUtilities utilities) {
        this.utilities = utilities;
    }

    @Override
    public void enrich(ValueStoreWriter wfItemWriter, ValueStoreReader wfItemReader, String rule)
            throws UnifyException {
        Entity inst = (Entity) wfItemReader.getValueObject();
        ProcedureRequest req = new ProcedureRequest(operation);
        req.setEntity(utilities.resolveLongName(inst.getClass()));
        req.setPayload(utilities.encodeDelegateEntity(inst));
        JsonProcedureResponse resp =  sendToDelegateProcedureService(req);
        Object[] payload = resp.getPayload();
        Entity respInst = null;
        if (payload != null && payload.length == 1) {
            if (payload instanceof String[]) {
                respInst = DataUtils.fromJsonString(inst.getClass(), (String) payload[0]);
            } else {
                respInst = (Entity) payload[0];
            }
        }

        if (respInst != null) {
            new BeanValueStore(inst).copy(new BeanValueStore(respInst));
        }
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

    protected JsonProcedureResponse sendToDelegateProcedureService(ProcedureRequest req) throws UnifyException {
        String reqJSON = DataUtils.asJsonString(req, PrintFormat.NONE);
        String respJSON = sendToDelegateProcedureService(reqJSON);
        JsonProcedureResponse resp = DataUtils.fromJsonString(JsonProcedureResponse.class, respJSON);
        if (resp.error()) {
            // TODO Translate to local exception and throw
        }

        return resp;
    }

    protected abstract String sendToDelegateProcedureService(String jsonReq) throws UnifyException;

}
