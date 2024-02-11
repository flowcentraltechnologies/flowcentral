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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.SingleFormBean;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Single form.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SingleForm {

    private final FormContext ctx;
    
    private final String panelName;
    
    private final SingleFormBean bean;
    
    private ValueStore beanValueStore;
    
    public SingleForm(FormContext ctx, String panelName, SingleFormBean bean) {
        this.ctx = ctx;
        this.panelName = panelName;
        this.bean = bean;
    }

    public FormContext getCtx() {
        return ctx;
    }

    public Object getFormBean() {
        return ctx.getInst();
    }

    public String getPanelName() {
        return panelName;
    }

    public SingleFormBean getBean() {
        return bean;
    }

    public ValueStore getBeanValueStore() {
        if (beanValueStore == null) {
            beanValueStore = new BeanValueStore(bean);
        }
        
        return beanValueStore;
    }
}
