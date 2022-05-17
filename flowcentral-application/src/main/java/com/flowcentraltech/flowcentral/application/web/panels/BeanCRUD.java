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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;

/**
 * Bean CRUD
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class BeanCRUD extends AbstractCRUD<BeanTable> {

    public BeanCRUD(AppletUtilities au, SweepingCommitPolicy scp, BeanTable table,
            MiniForm createForm, MiniForm maintainForm) {
        super(au, scp, table, createForm, maintainForm);
    }

}
