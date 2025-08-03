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

package com.flowcentraltech.flowcentral.notification.web.controllers;

import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Send test notification controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/notification/sendtestnotification")
@UplBinding("web/notification/upl/sendtestnotification.upl")
@ResultMappings({
        @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{sendNotificationPanel}" }) })
public class SendTestNotificationController extends AbstractNotificationPageController<SendTestNotificationPageBean> {

    public SendTestNotificationController() {
        super(SendTestNotificationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageTitle(resolveSessionMessage("$m{notification.sendtestnotification.title}"));
        SendTestNotificationPageBean pageBean = getPageBean();
        pageBean.setType(NotifType.EMAIL);
        switchType();
    }

    @Action
    public String sendNotification() throws UnifyException {
        SendTestNotificationPageBean pageBean = getPageBean();
        String body = null;
        String subject = null;
        String contact = null;
        final NotifType type = pageBean.getType();
        switch(type) {
            case SMS:
                contact = pageBean.getMobile();
                body = resolveApplicationMessage("$m{sendtestnotification.sms.body}");
                break;
            case SYSTEM:
                contact = pageBean.getUserLoginId();
                body = resolveApplicationMessage("$m{sendtestnotification.system.body}");
                break;
            case EMAIL:
                contact = pageBean.getEmail();
                subject = resolveApplicationMessage("$m{sendtestnotification.email.subject}");
                body = resolveApplicationMessage("$m{sendtestnotification.email.body}");
            default:
                break;            
        }
        
        NotifMessage.Builder nmb = NotifMessage.newBuilder(subject,  body);
        nmb.notifType(type);
        nmb.addTORecipient(contact, contact);
        nmb.addParam("messageTimestamp", notification().getNow().toString());
        notification().sendNotification(nmb.build());
        hintUser("$m{notification.sendtestnotification.sent}");
        return "refresh";
    }

    @Action
    public String switchType() throws UnifyException {
        SendTestNotificationPageBean pageBean = getPageBean();
        final NotifType type = pageBean.getType();
        setPageWidgetVisible("frmEmail", type.isEmail());
        setPageWidgetVisible("frmMobile", type.isSMS());
        setPageWidgetVisible("frmUserloginId", type.isSystem());
        return "refresh";
    }

}
