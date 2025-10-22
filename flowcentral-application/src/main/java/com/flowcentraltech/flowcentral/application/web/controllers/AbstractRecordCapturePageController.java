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
package com.flowcentraltech.flowcentral.application.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AbstractRecordCapture;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Convenient abstract base class for record capture controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@UplBinding("web/application/upl/recordcapturepage.upl")
@ResultMappings({
        @ResultMapping(name = "refreshCapture", response = { "!refreshpanelresponse panels:$l{capturePanel}" }) })
public abstract class AbstractRecordCapturePageController<T extends AbstractRecordCapture, U extends AbstractRecordCapturePageBean<T>>
        extends AbstractFlowCentralPageController<U> {

    public AbstractRecordCapturePageController(Class<U> pageBeanClass) {
        super(pageBeanClass, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String loadCapture() throws UnifyException {
        final List<T> records = doLoad();
        getPageBean().setRecords(records);
        return "refreshCapture";
    }

    @Action
    public String saveCapture() throws UnifyException {
        doSave(getPageBean().getRecords());
        hintUser("$m{recordcapturepage.hint.recordsuccessfullysaved}");
        return noResult();
    }

    @Action
    public String clearCapture() throws UnifyException {
        getPageBean().clear();
        return "refreshCapture";
    }

    protected abstract List<T> doLoad() throws UnifyException;

    protected abstract void doSave(List<T> records) throws UnifyException;
}
