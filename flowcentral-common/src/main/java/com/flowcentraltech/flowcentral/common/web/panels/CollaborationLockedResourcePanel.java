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

package com.flowcentraltech.flowcentral.common.web.panels;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.CollaborationLockedResourceInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Panel for collaboration locked resource.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-collaborationlockedresourcepanel")
@UplBinding("web/common/upl/collaborationlockedresourcepanel.upl")
public class CollaborationLockedResourcePanel extends BaseDialogPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        CollaborationLockedResourceInfo collaborationLockedResourceInfo = getValue(
                CollaborationLockedResourceInfo.class);
        setRequestAttribute(FlowCentralRequestAttributeConstants.USER_LOGINID,
                collaborationLockedResourceInfo.getLockedByUserLoginId());
    }

}
