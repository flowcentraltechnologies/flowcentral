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
package com.flowcentraltech.flowcentral.audit.web.widgets;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.widget.AbstractWidget;

/**
 * Entity audit snapshot view widget..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entityauditsnapshotview")
public class EntityAuditSnapshotViewWidget extends AbstractWidget {

    @Override
    public boolean isControl() {
        return false;
    }

    @Override
    public boolean isPanel() {
        return false;
    }

    public Long getEntityAuditKeysId() throws UnifyException {
        return (Long) getValue(Long.class);
    }

    @Override
    public boolean isSupportStretch() {
        return false;
    }
}
