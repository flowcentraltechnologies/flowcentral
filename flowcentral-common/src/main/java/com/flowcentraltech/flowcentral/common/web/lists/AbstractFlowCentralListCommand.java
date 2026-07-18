/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.common.web.lists;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralEditionConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.AbstractListCommand;
import com.tcdng.unify.core.list.ListParam;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.PageRequestContextUtil;
import com.tcdng.unify.web.ui.WebUIApplicationComponents;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Convenient abstract base class for flowcentral list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractFlowCentralListCommand<T extends ListParam> extends AbstractListCommand<T> {

    @Configurable
    private EnvironmentService environmentService;

    public AbstractFlowCentralListCommand(Class<T> paramType) {
        super(paramType);
    }

    protected final boolean isEnterprise() throws UnifyException {
        return FlowCentralEditionConstants.ENTERPRISE.equalsIgnoreCase(
                getContainerSetting(String.class, FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALLATION_TYPE));
    }

    protected final EnvironmentService environment() {
        return environmentService;
    }

    protected <U> U getPageAttribute(Class<U> clazz, String name) throws UnifyException {
        Page page = resolveRequestPage();
        return DataUtils.convert(clazz, page != null ? page.getAttribute(name) : null);
    }

    private Page resolveRequestPage() throws UnifyException {
        PageRequestContextUtil rcUtil = getComponent(PageRequestContextUtil.class,
                WebUIApplicationComponents.APPLICATION_PAGEREQUESTCONTEXTUTIL);
        Page contentPage = rcUtil.getContentPage();
        return contentPage == null ? rcUtil.getRequestPage() : contentPage;
    }

}
