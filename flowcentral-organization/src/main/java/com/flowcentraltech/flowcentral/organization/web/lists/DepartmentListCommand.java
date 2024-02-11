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
package com.flowcentraltech.flowcentral.organization.web.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.organization.entities.Department;
import com.flowcentraltech.flowcentral.organization.entities.DepartmentQuery;
import com.flowcentraltech.flowcentral.organization.entities.MappedDepartmentQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Department list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("departmentlist")
public class DepartmentListCommand extends AbstractOrganizationListCommand<ZeroParams> {

    public DepartmentListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        List<Listable> listables = new ArrayList<Listable>();
        listables.addAll(
                getOrganizationModuleService().findMappedDepartments((MappedDepartmentQuery) new MappedDepartmentQuery()
                        .addNotEquals("id", DefaultApplicationConstants.DEVOPS_DEPARTMENT_ID)
                        .addSelect("id", "description").addOrder("description").ignoreEmptyCriteria(true)));
        Department devOpDepartment = getOrganizationModuleService().findDepartment(
                (DepartmentQuery) new DepartmentQuery().id(DefaultApplicationConstants.DEVOPS_DEPARTMENT_ID));
        if (devOpDepartment != null) {
            listables.add(devOpDepartment);
        }

        return listables;
    }

}
