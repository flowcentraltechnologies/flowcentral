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

import com.flowcentraltech.flowcentral.application.web.panels.applet.EntityChoiceApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.BadgeInfo;

/**
 * Convenient abstract base class for entity choice controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@UplBinding("web/application/upl/entitychoicepage.upl")
@ResultMappings({})
public abstract class AbstractEntityChoiceController<T extends AbstractEntityChoicePageBean>
        extends AbstractAppletController<T> {

    private final BadgeInfo choiceBadgeInfo;

    private final String appletName;

    public AbstractEntityChoiceController(Class<T> pageBeanClass, BadgeInfo choiceBadgeInfo, String appletName) {
        super(pageBeanClass, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
        this.choiceBadgeInfo = choiceBadgeInfo;
        this.appletName = appletName;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        AbstractEntityChoicePageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            EntityChoiceApplet applet = new EntityChoiceApplet(getPage(), au(), appletName, choiceBadgeInfo);
            pageBean.setApplet(applet);
            if (pageBean.getAltCaption() == null) {
                setPageTitle(applet);
            }
        }
        
        final Restriction baseRestriction = getBaseRestriction();
        if (baseRestriction != null) {
            pageBean.getApplet().setBaseRestriction(baseRestriction);
        }
        
        pageBean.getApplet().applyFilterToSearch();
    }

    protected abstract Restriction getBaseRestriction() throws UnifyException;
}
