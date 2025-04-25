/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;

/**
 * Mini form.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class MiniForm {

    private MiniFormScope scope;

    private FormContext ctx;

    private FormTabDef formTabDef;
    
    public MiniForm(MiniFormScope scope, FormContext ctx, FormTabDef formTabDef) {
        this.scope = scope;
        this.ctx = ctx;
        this.formTabDef = formTabDef;
    }

    public MiniFormScope getScope() {
        return scope;
    }

    public FormContext getCtx() {
        return ctx;
    }

    public FormTabDef getFormTabDef() {
        return formTabDef;
    }

    public boolean isAllocateTabIndex() {
        return scope.isMainForm();
    }

    public boolean isMainForm() {
        return scope.isMainForm();
    }

    public Object getFormBean() {
        return ctx.getInst();
    }
}
