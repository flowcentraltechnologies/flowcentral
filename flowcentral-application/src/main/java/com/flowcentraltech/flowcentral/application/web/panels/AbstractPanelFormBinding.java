/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;

/**
 * Abstract base class for form binding objects.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractPanelFormBinding {

    private final FormContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final String tabName;

    public AbstractPanelFormBinding(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName) {
        this.ctx = ctx;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.tabName = tabName;
    }

    public FormContext getFormCtx() {
        return ctx;
    }

    public AppletContext getAppletCtx() {
        return ctx.getAppletContext();
    }

    public SweepingCommitPolicy getSweepingCommitPolicy() {
        return sweepingCommitPolicy;
    }

    public String getTabName() {
        return tabName;
    }

    public List<FormMessage> getMessages() throws UnifyException {
        return ctx.getReviewMessages(tabName);
    }
    
    protected boolean isTabEditable() {
        return ctx.isTabEditable(tabName);
    }
}
