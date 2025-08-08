/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.messaging.os.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityPolicy;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.EncodingUtils;

/**
 * OS messaging endpoint entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("osmessagingendpoint-policy")
public class OSMessagingEndpointPolicy extends BaseStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        OSMessagingEndpoint osMessagingEndpoint = (OSMessagingEndpoint) record;
        generateEncoded(osMessagingEndpoint);
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        OSMessagingEndpoint osMessagingEndpoint = (OSMessagingEndpoint) record;
        generateEncoded(osMessagingEndpoint);
        super.preUpdate(record, now);
    }

    private void generateEncoded(OSMessagingEndpoint osMessagingEndpoint) throws UnifyException {
        final String userName = getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_APPLICATION_OS_APPID);
        final String password = osMessagingEndpoint.getPassword();
        String basic = userName + ":" + password;
        String encodedAuthentication = EncodingUtils.getBase64String(basic);
        osMessagingEndpoint.setAuthorization(encodedAuthentication);
    }
}
