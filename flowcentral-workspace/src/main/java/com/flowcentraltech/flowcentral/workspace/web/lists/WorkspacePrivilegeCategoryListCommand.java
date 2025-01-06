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

package com.flowcentraltech.flowcentral.workspace.web.lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategoryQuery;
import com.flowcentraltech.flowcentral.organization.web.lists.AbstractOrganizationListCommand;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Workspace privilege category list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("workspaceprivilegecategorylist")
public class WorkspacePrivilegeCategoryListCommand extends AbstractOrganizationListCommand<ZeroParams> {

    private static final List<String> workspaceCategories = Collections
            .unmodifiableList(Arrays.asList(ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE,
                    ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                    ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE));

    public WorkspacePrivilegeCategoryListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        return getOrganizationModuleService().findPrivilegeCategories(
                (PrivilegeCategoryQuery) new PrivilegeCategoryQuery().codeIn(workspaceCategories));
    }

}
