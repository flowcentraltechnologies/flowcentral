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

package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.web.panels.BaseDialogPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.FileAttachmentsInfo;

/**
 * Entity file attachments panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entityfileattachmentspanel")
@UplBinding("web/application/upl/entityfileattachmentspanel.upl")
public class EntityFileAttachmentsPanel extends BaseDialogPanel {

	
	@Override
	public void switchState() throws UnifyException {
		super.switchState();				
		if(getRequestAttribute(boolean.class,AppletRequestAttributeConstants.ATTACHMENT_DISABLED)) {					
			final EntityFileAttachments formFileAttachments = getValue(EntityFileAttachments.class);
			formFileAttachments.setDisabled(true);			
		}
	}
	
}
