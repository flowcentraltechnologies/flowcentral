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
package com.flowcentraltech.flowcentral.system.entities;

import java.util.Base64;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * Credential entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("credential-policy")
public class CredentialPolicy extends BaseStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        Credential credential = (Credential) record;
        final String base64Encoded = calcBase64Encoded(credential);
        credential.setBase64Encoded(base64Encoded);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        Credential credential = (Credential) record;
        final String base64Encoded = calcBase64Encoded(credential);
        credential.setBase64Encoded(base64Encoded);
        super.preUpdate(record, now);
    }

    private String calcBase64Encoded(Credential credential) throws UnifyException {
        if (credential.getUserName() != null && credential.getPassword() != null) {
            return Base64.getEncoder()
                    .encodeToString((credential.getUserName() + ":" + credential.getPassword()).getBytes());
        }

        return null;
    }
}
