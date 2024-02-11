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

package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralStandalonePanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for single form panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSingleFormPanel<T extends AbstractSingleFormBean> extends AbstractFlowCentralStandalonePanel
        implements SingleFormPanel<T> {

    @Override
    public void addPageAliases() throws UnifyException {
        super.addPageAliases();
        // TODO Move to super class
        getRequestContextUtil().addPageAlias(getId(), DataUtils.toArray(String.class, getAllWidgetIds()));
    }

    @SuppressWarnings("unchecked")
    protected final T getFormBean() throws UnifyException {
        return (T) getValueStore().getValueObject();
    }
}
