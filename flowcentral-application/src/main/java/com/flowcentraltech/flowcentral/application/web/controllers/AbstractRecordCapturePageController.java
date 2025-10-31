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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AbstractRecordCapture;
import com.flowcentraltech.flowcentral.application.data.RecordCaptureError;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Convenient abstract base class for record capture controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@UplBinding("web/application/upl/recordcapturepage.upl")
    @ResultMappings({ @ResultMapping(name = "refreshCapture",
        response = { "!refreshpanelresponse panels:$l{capturePanel footerActionPanel}" }) })
public abstract class AbstractRecordCapturePageController<T extends AbstractRecordCapture, U extends AbstractRecordCapturePageBean<T>>
        extends AbstractFlowCentralPageController<U> {

    public AbstractRecordCapturePageController(Class<U> pageBeanClass) {
        super(pageBeanClass, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String loadCapture() throws UnifyException {
        final List<T> records = doLoad();
        getPageBean().setRecords(records);
        setPageState();
        return "refreshCapture";
    }

    @Action
    public String saveCapture() throws UnifyException {
        final AbstractRecordCapturePageBean<T> pageBean = getPageBean();
        final boolean success = validate();
        if (success || pageBean.isAllowDraft()) {
            doSave(pageBean.getRecords());
            if (success) {
                hintUser("$m{recordcapturepage.hint.recordsuccessfullysaved}");
            } else {
                hintUser(MODE.WARNING, "$m{recordcapturepage.hint.recordsuccessfullysavederrors}");
            }
        } else {
            hintUser(MODE.ERROR, "$m{recordcapturepage.hint.recordvalidationerrors}");
        }
        
        return "refreshCapture";
    }

    @Action
    public String clearCapture() throws UnifyException {
        getPageBean().clear();
        setPageState();
        return "refreshCapture";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        loadCapture();
    }

    protected abstract List<T> doLoad() throws UnifyException;

    protected abstract void validate(RecordCaptureError error, T record) throws UnifyException;
    
    protected abstract void doSave(List<T> records) throws UnifyException;

    protected void setPageState() throws UnifyException {
        final boolean disabled = !getPageBean().isEditable() || DataUtils.isBlank(getPageBean().getRecords());
        setPageWidgetDisabled("saveCaptureBtn", disabled);
        setPageWidgetDisabled("clearCaptureBtn", disabled);
    }
    
    private boolean validate() throws UnifyException {
        final AbstractRecordCapturePageBean<T> pageBean = getPageBean();
        final List<T> records = pageBean.getRecords();
        List<RecordCaptureError> errors = Collections.emptyList();
        boolean success = true;
        if (!DataUtils.isBlank(records)) {
            errors = new ArrayList<RecordCaptureError>();
            for (T record: records) {
                RecordCaptureError error = new RecordCaptureError();
                validate(error, record);
                errors.add(error);
                success &= !error.isPresent();
            }            
        }

        pageBean.setErrors(errors);
        return success;
    }
}
