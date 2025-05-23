/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.common.web.controllers;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Convenient abstract base class for page forwarders.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@ResultMappings({
        @ResultMapping(name = "forwardtopath", response = { "!forwardresponse pathBinding:$s{targetPath}" })})
public abstract class AbstractForwarderController<T extends AbstractForwarderPageBean>
        extends AbstractFlowCentralPageController<T> {

    public AbstractForwarderController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    protected String forwardToPath(String targetPath) throws UnifyException {
        AbstractForwarderPageBean pageBean = getPageBean();
        pageBean.setTargetPath(targetPath);
        return "forwardtopath";
    }

}
