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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.FormMessage;

/**
 * CRUD object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCRUD<T extends AbstractTable<?, ?>> {

    public enum FormMode {
        CREATE,
        MAINTAIN
    };
    
    private final AppletUtilities au;

    private final SweepingCommitPolicy scp;

    private final T table;

    private final MiniForm createForm;

    private final MiniForm maintainForm;

    private MiniForm form;

    public AbstractCRUD(AppletUtilities au, SweepingCommitPolicy scp,
            T table, MiniForm createForm, MiniForm maintainForm) {
        this.au = au;
        this.scp = scp;
        this.table = table;
        this.createForm = createForm;
        this.maintainForm = maintainForm;
    }

    public AppletUtilities getAu() {
        return au;
    }

    public T getTable() {
        return table;
    }

    public MiniForm getForm() {
        return form;
    }

    public List<FormMessage> getValidationErrors() {
        return form.getCtx().getValidationErrors();
    }

    public void switchMode(FormMode mode) {
        form = FormMode.CREATE.equals(mode) ? createForm : maintainForm;
    }
}
