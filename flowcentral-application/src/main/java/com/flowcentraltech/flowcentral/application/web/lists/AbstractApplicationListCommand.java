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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.list.ListParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for application list commands.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplicationListCommand<T extends ListParam> extends AbstractFlowCentralListCommand<T> {

    @Configurable
    private AppletUtilities au;

    public AbstractApplicationListCommand(Class<T> paramType) {
        super(paramType);
    }

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    protected AppletUtilities au() {
        return au;
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected List<? extends Listable> getListables(List<? extends Entity> srcList, String descField)
            throws UnifyException {
        List<ListData> resultList = new ArrayList<ListData>();
        for (Entity entity : srcList) {
            resultList
                    .add(new ListData(entity.getListKey(), DataUtils.getBeanProperty(String.class, entity, descField)));
        }

        return resultList;
    }

    protected List<? extends Listable> getListables(List<? extends Entity> srcList, String keyField, String descField)
            throws UnifyException {
        List<ListData> resultList = new ArrayList<ListData>();
        for (Entity entity : srcList) {
            resultList.add(new ListData(DataUtils.getBeanProperty(String.class, entity, keyField),
                    DataUtils.getBeanProperty(String.class, entity, descField)));
        }

        return resultList;
    }
}
